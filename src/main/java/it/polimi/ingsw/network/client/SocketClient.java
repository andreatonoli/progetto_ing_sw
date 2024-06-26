package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.Ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used to handle the client side of the socket connection.
 */
public class SocketClient implements ClientInterface {

    /**
     * The address of the server.
     */
    private final String address;

    /**
     * The port of the server.
     */
    private final int port;

    /**
     * The socket used to connect to the server.
     */
    private Socket socket;

    /**
     * The username of the client.
     */
    private String username;

    /**
     * The view of the client.
     */
    private final Ui view;

    /**
     * The input stream of the socket.
     */
    private ObjectInputStream in;

    /**
     * The output stream of the socket.
     */
    private ObjectOutputStream out;

    /**
     * The flag used to check if the client is disconnected.
     */
    private boolean disconnected;

    /**
     * The game bean used to store the game state and the common resources, gold and achievements.
     */
    private GameBean game;

    /**
     * The player bean used to store the player state and the player resources, hand and achievements.
     */
    private PlayerBean player;

    /**
     * The list of the opponents of the player.
     */
    private ArrayList<PlayerBean> opponents;

    /**
     * The message queue used to store the messages coming from the server.
     */
    private final BlockingQueue<Message> messageQueue;

    /**
     * The flag used to check if the client is processing an action.
     */
    private boolean processingAction;

    /**
     * The lock used to synchronize the output stream.
     */
    private final Object outputLock = new Object();

    /**
     * The timer used to catch the ping.
     */
    private Timer catchPing;
    private Timer reconnectionTimer;

    /**
     * The reconnection thread used to reconnect the client.
     */
    private Thread reconnectionThread;

    /**
     * The constructor of the class.
     * @param address the address of the server.
     * @param port the port of the server.
     * @param view the view of the client.
     */
    public SocketClient(String address, int port, Ui view){
        this.view = view;
        this.opponents = new ArrayList<>();
        this.game = new GameBean();
        messageQueue = new LinkedBlockingQueue<>();
        this.address = address;
        this.port = port;
        this.catchPing = new Timer();
    }

    /**
     * This method is used to start the client. It connects to the server and starts the communication.
     * @param address the address of the server.
     * @param port the port of the server.
     */
    public void startClient(String address, int port){
        try {
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            disconnected = false;
            sendMessage(new LoginResponseMessage());
            reconnectionThread.interrupt();
            while(!disconnected){
                readMessage();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            this.onDisconnect();
            System.out.println("Connection successfully ended");
        }
    }

    /**
     * This method is used to read a message from the server.
     */
    public void readMessage(){
        Message message;
        try {
            message = (Message) in.readObject();
            if (message.getType().equals(MessageType.PING)){
                sendMessage(new CatchPingMessage(this.username));
                catchPing();
            }
            else{
                messageQueue.add(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            this.onDisconnect();
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method is used to send a message to the server.
     * @param message the message to send.
     */
    public void sendMessage(Message message){
        if (!disconnected) {
            try {
                synchronized (outputLock) {
                    out.writeObject(message);
                    out.reset();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This method is to start the client, trying to connect to the server and initializing the reconnection thread.
     */
    @Override
    public void login() {
        reconnectionThread = new Thread(() -> {
            reconnectionTimer = new Timer();
            reconnectionTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startClient(address, port);
                }
            }, 0, 1000);
        });
        processingAction = false;
        pickQueue();
        new Thread(() -> this.startClient(address, port)).start();
    }

    /**
     * This method is used to try to reconnect the client.
     */
    public void reconnectAttempt(){
        reconnectionThread.start();
    }

    /**
     * This method is used to resume the connection.
     * @param number the number of the player.
     * @param creation the flag used to check if the player is creating a new game.
     */
    public void resumeConnection(int number, boolean creation){
        if (creation){
            sendMessage(new NumPlayerResponseMessage(this.username, number));
        }
        else {
            sendMessage(new LobbyIndexMessage(this.username, number));
        }
    }

    /**
     * This method is used to set the action to perform on connection.
     * @param response is the action to perform.
     * @param startingGamesId is the list of the starting games' id.
     * @param gamesWithDisconnectionsId is the list of the games with disconnections id.
     */
    @Override
    public void setOnConnectionAction(int response, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId) {
        if (response == -2) {
            this.view.selectGame(startingGamesId, gamesWithDisconnectionsId);
        }
        if (response == -1) {
            this.view.askLobbySize();
        } else if (startingGamesId.contains(response)) {
            sendMessage(new LobbyIndexMessage(this.username, response));
        } else if (gamesWithDisconnectionsId.contains(response)) {
            sendMessage(new ReconnectLobbyIndexMessage(this.username, response, startingGamesId, gamesWithDisconnectionsId));
        }
    }

    /**
     * This method is used to catch the ping, resetting the timer. If the ping is not caught, the client is disconnected.
     */
    public void catchPing(){
        catchPing.cancel();
        catchPing = new Timer();
        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                catchPing.cancel();
                onDisconnect();
                view.reset();
                if (game.getState() == null || game.getState() != GameState.END){
                    reconnectAttempt();
                }
            }
        }, 5000, 5000);
    }

    public void cancelCatchPing(){
        catchPing.cancel();
    }

    /**
     * This method called when the client is disconnected.
     */
    public void onDisconnect(){
        try {
            this.disconnected = true;
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is used to set the nickname of the player.
     * @param nickname the nickname of the player.
     */
    @Override
    public void setNickname(String nickname) {
        this.username = nickname;
        if (this.player != null) {
            player.setUsername(nickname);
        }
        else {
            this.player = new PlayerBean(this.username);
        }
    }

    /**
     * This method is used to set the lobby size.
     * @param size the size of the lobby.
     */
    @Override
    public void setLobbySize(int size){
        sendMessage(new NumPlayerResponseMessage(this.username, size));
    }

    /**
     * This method is used to set the achievement chosen by the player.
     * @param achievement is the chosen achievement.
     */
    @Override
    public void chooseAchievement(Achievement achievement){
        this.player.setAchievement(achievement);
        sendMessage(new SetPrivateAchievementMessage(this.username, achievement));
    }

    /**
     * This method is used to set the color chosen by the player.
     * @param chosenColor is the chosen color.
     */
    @Override
    public void chooseColor(Color chosenColor){
        player.setPionColor(chosenColor);
        sendMessage(new ColorResponseMessage(username, chosenColor));
    }

    /**
     * This method is used to place the starter card.
     * @param side is the side of the card.
     * @param starterCard is the starter card.
     */
    @Override
    public void placeStarterCard(boolean side, Card starterCard){
        if (!side) {
            starterCard.setCurrentSide();
        }
        player.setStarterCard(starterCard);
        sendMessage(new PlaceStarterRequestMessage(username, starterCard));
    }

    /**
     * This method is used to place the card.
     * @param card is the card to place.
     * @param placingCoordinates are the coordinates where to place the card.
     */
    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (this.player.getState().equals(PlayerState.PLAY_CARD)){
            sendMessage(new PlaceMessage(username, card, placingCoordinates));
            player.removeCardFromHand(card);
        }
        else{
            update(new ErrorMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    /**
     * This method is used to draw a card.
     * @param chosenDeck is the deck from which the card is drawn.
     */
    @Override
    public void drawCard(String chosenDeck) {
        if (this.player.getState().equals(PlayerState.DRAW_CARD)) {
            sendMessage(new DrawMessage(username, chosenDeck));
        }
        else {
            update(new ErrorMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    /**
     * This method is used to draw a card from the board.
     * @param index is the index of the card to draw.
     */
    @Override
    public void drawCardFromBoard(int index) {
        if (this.player.getState().equals(PlayerState.DRAW_CARD)) {
            sendMessage(new DrawFromBoardMessage(username, index - 1));
        }
        else{
            update(new ErrorMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    /**
     * This method is used to pick a message from the queue.
     */
    private void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                processQueue();
            }
        }, 0, 500);
    }

    /**
     * This method is used to process the message queue.
     */
    private void processQueue() {
        if (!messageQueue.isEmpty() && !processingAction) {
            Message message = messageQueue.poll();
            processingAction = true;
            this.update(message);
            processingAction = false;
            if (!messageQueue.isEmpty()) {
                processQueue();
            }
        }
    }

    /**
     * This method is used to update the client based on the message received.
     * @param message the message to process.
     */
    public void update(Message message){
        String name;
        switch (message.getType()){
            case RECONNECTION:
                this.player = ((ReconnectionMessage) message).getPlayerBean();
                this.game = ((ReconnectionMessage) message).getGameBean();
                this.opponents = ((ReconnectionMessage) message).getOpponents();
                this.view.handleReconnection();
                this.view.printViewWithCommands(player, game, opponents);
                break;
            case USERNAME_REQUEST:
                int number = ((UsernameRequestMessage) message).getNumber();
                boolean creation = ((UsernameRequestMessage) message).isCreation();
                System.out.println("Username is already taken, please choose another: ");
                this.view.askNickname();
                resumeConnection(number, creation);
                break;
            case NUM_PLAYER_REQUEST:
                cancelCatchPing();
                this.view.askLobbySize();
                break;
            case AVAILABLE_LOBBY:
                List<Integer> startingGamesId = ((AvailableLobbiesMessage) message).getStartingGamesId();
                List<Integer> gamesWhitDisconnectionsId = ((AvailableLobbiesMessage) message).getGamesWhitDisconnectionsId();
                cancelCatchPing();
                this.view.selectGame(startingGamesId, gamesWhitDisconnectionsId);
                break;
            case GAME_STATE:
                GameState state = ((GameStateMessage) message).getState();
                game.setState(state);
                if (state.equals(GameState.IN_GAME)){
                    this.view.printViewWithCommands(player, game, opponents);
                }
                break;
            case OPPONENTS:
                ArrayList<String> playersName = ((OpponentsMessage) message).getPlayers();
                for (String s : playersName){
                    if(!s.equalsIgnoreCase(username)){
                        opponents.add(new PlayerBean(s));
                    }
                }
                break;
            case COMMON_GOLD_UPDATE:
                int index = ((CommonCardUpdateMessage) message).getIndex();
                game.setCommonGold(index, ((CommonCardUpdateMessage) message).getCard());
                break;
            case COMMON_RESOURCE_UPDATE:
                int index1 = ((CommonCardUpdateMessage) message).getIndex();
                game.setCommonResources(index1, ((CommonCardUpdateMessage) message).getCard());
                break;
            case COMMON_ACHIEVEMENT:
                System.arraycopy(((AchievementMessage) message).getAchievements(), 0, game.getCommonAchievement(), 0, 2);
                break;
            case DECK_UPDATE:
                Color color = ((UpdateDeckMessage) message).getColor();
                boolean isResource = ((UpdateDeckMessage) message).getIsResource();
                if (isResource){
                    game.setResourceDeckRetro(color);
                }
                else{
                    game.setGoldDeckRetro(color);
                }
                break;
            case STARTER_CARD:
                Card starterCard = ((StarterCardMessage) message).getCard();
                this.view.askSide(starterCard);
                break;
            case CARD_HAND:
                //Copied the message body into the player's cards
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, player.getHand(), 0, 3);
                break;
            case PRIVATE_ACHIEVEMENT:
                this.view.askAchievement(((AchievementMessage) message).getAchievements());
                break;
            case PLAYER_STATE:
                PlayerState playerState = ((PlayerStateMessage) message).getState();
                name = ((PlayerStateMessage) message).getName();
                if (username.equals(name)) {
                    player.setState(playerState);
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            p.setState(playerState);
                        }
                    }
                }
                break;
            case COLOR_REQUEST:
                this.view.askColor(((ColorRequestMessage) message).getColors());
                break;
            case COLOR_RESPONSE:
                Color setColor = ((ColorResponseMessage) message).getColor();
                name = message.getSender();
                if (name.equalsIgnoreCase(username)){
                    player.setPionColor(setColor);
                }
                else{
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equalsIgnoreCase(name)){
                            p.setPionColor(setColor);
                            break;
                        }
                    }
                }
                break;
            case PLAYERBOARD_UPDATE:
                PlayerBoard playerBoard = ((PlayerBoardUpdateMessage) message).getpBoard();
                name = ((PlayerBoardUpdateMessage) message).getName();
                if (name.equalsIgnoreCase(username)){
                    player.setBoard(playerBoard);
                    this.view.printViewWithCommands(this.player, this.game, this.opponents);
                }
                else{
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            p.setBoard(playerBoard);
                        }
                    }
                }
                break;
            case CARD_UPDATE:
                Card drawedCard = ((UpdateCardMessage) message).getCard();
                player.setCardinHand(drawedCard);
                if (!drawedCard.isNotBack()){
                    drawedCard.setCurrentSide();
                }
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case SCORE_UPDATE:
                name = ((ScoreUpdateMessage) message).getName();
                int points = ((ScoreUpdateMessage) message).getPoint();
                if (name.equalsIgnoreCase(username)){
                    player.setPoints(points);
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equalsIgnoreCase(name)){
                            p.setPoints(points);
                        }
                    }
                }
                break;
            case CHAT:
                player.setChat(((ChatMessage) message).getChat());
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case WAITING_RECONNECTION:
                name = ((WaitingReconnectionMessage) message).getUsername();
                if (username.equals(name)) {
                    player.setState(PlayerState.NOT_IN_TURN);
                }
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case DECLARE_WINNER:
                ArrayList<String> winners = ((WinnerMessage) message).getWinners();
                this.cancelCatchPing();
                this.view.declareWinners(winners);
                this.onDisconnect();
                break;
            case GENERIC_MESSAGE:
                if (game.getState().ordinal() > 1) {
                    this.view.setMessage(message.toString(), false);
                }
                else {
                    System.out.println(message);
                }
                break;
            case ERROR:
                this.view.setMessage(message.toString(), true);
                this.view.printViewWithCommands(player, game, opponents);
                break;
            default:
                break;
        }
    }

    /**
     * This method is used to send a chat message to the server.
     * @param message the message to send.
     */
    @Override
    public void sendChatMessage(String message) {
        sendMessage(new AddToChatMessage(username, message));
    }

    /**
     * This method is used to send a chat message to a specific player.
     * @param receiver is the receiver of the message.
     * @param message is the message to be sent.
     */
    @Override
    public void sendChatMessage(String receiver, String message) {
        sendMessage(new AddToChatMessage(username, receiver, message));
    }
}
