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

public class SocketClient implements ClientInterface {
    private final String address;
    private final int port;
    private Socket socket;
    private String username;
    private final Ui view;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean disconnected;
    private GameBean game;
    private PlayerBean player;
    private ArrayList<PlayerBean> opponents;
    private final BlockingQueue<Message> messageQueue;
    private boolean processingAction;
    private final Object outputLock = new Object();
    private Timer catchPing;
    private Thread reconnectionThread;

    public SocketClient(String address, int port, Ui view){
        this.view = view;
        this.opponents = new ArrayList<>();
        this.game = new GameBean();
        messageQueue = new LinkedBlockingQueue<>();
        this.address = address;
        this.port = port;
        this.catchPing = new Timer();
    }

    @Override
    public void login() {
        reconnectionThread = new Thread(() -> {
            Timer reconnectionTimer = new Timer();
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

    private void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                processQueue();
            }
        }, 0, 500);
    }

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
                this.view.askNickname();
                this.view.askLobbySize();
                break;
            case FREE_LOBBY:
                List<Integer> startingGamesId = ((FreeLobbyMessage) message).getstartingGamesId();
                List<Integer> gamesWhitDisconnectionsId = ((FreeLobbyMessage) message).getgamesWhitDisconnectionsId();
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
                System.err.println(username);
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
                this.view.declareWinners(winners);
                sendMessage(new RemoveFromServerMessage(username));
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

    @Override
    public void setOnConnectionAction(int response, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        if (response == -2) {
           this.view.selectGame(startingGamesId, gamesWithDisconnectionsId);
        }
        this.view.askNickname();
        if (response == -1){
            this.view.askLobbySize();
        }
        else if (startingGamesId.contains(response)){
            sendMessage(new LobbyIndexMessage(this.username, response));
        }
        else if (gamesWithDisconnectionsId.contains(response)){
            sendMessage(new ReconnectLobbyIndexMessage(this.username, response, startingGamesId, gamesWithDisconnectionsId));
        }
    }

    @Override
    public void setLobbySize(int size){
        sendMessage(new NumPlayerResponseMessage(this.username, size));
    }

    @Override
    public void placeStarterCard(boolean side, Card starterCard){
        if (!side) {
            starterCard.setCurrentSide();
        }
        player.setStarterCard(starterCard);
        sendMessage(new PlaceStarterRequestMessage(username, starterCard));
    }

    @Override
    public void chooseAchievement(Achievement achievement){
        this.player.setAchievement(achievement);
        sendMessage(new SetPrivateAchievementMessage(this.username, achievement));
    }

    @Override
    public void chooseColor(Color chosenColor){
        player.setPionColor(chosenColor);
        sendMessage(new ColorResponseMessage(username, chosenColor));
    }

    @Override
    public void sendChatMessage(String message) {
        sendMessage(new AddToChatMessage(username, message));
    }

    @Override
    public void sendChatMessage(String receiver, String message) {
        sendMessage(new AddToChatMessage(username, receiver, message));
    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (this.player.getState().equals(PlayerState.PLAY_CARD)){
            sendMessage(new PlaceMessage(username, card, placingCoordinates));
            player.removeCardFromHand(card);
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    @Override
    public void drawCard(String chosenDeck) {
        if (this.player.getState().equals(PlayerState.DRAW_CARD)) {
            sendMessage(new DrawMessage(username, chosenDeck));
        }
        else {
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    @Override
    public void drawCardFromBoard(int index) {
        if (this.player.getState().equals(PlayerState.DRAW_CARD)) {
            sendMessage(new DrawFromBoardMessage(username, index - 1));
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    public void reconnectAttempt(){
        reconnectionThread.start();
    }

    public void catchPing(){
        catchPing.cancel();
        catchPing = new Timer();
        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                catchPing.cancel();
                onDisconnect();
                view.reset();
                reconnectAttempt();
            }
        }, 5000, 5000);
    }

    public void resumeConnection(int number, boolean creation){
        if (creation){
            sendMessage(new NumPlayerResponseMessage(this.username, number));
        }
        else {
            sendMessage(new LobbyIndexMessage(this.username, number));
        }
    }
}
