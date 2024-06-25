package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualServer;
import it.polimi.ingsw.view.Ui;
import it.polimi.ingsw.model.card.Card;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is the client of the RMI connection.
 */
public class RMIClient extends UnicastRemoteObject implements RMIClientHandler, ClientInterface {

    /**
     * The address of the server.
     */
    private final String address;

    /**
     * The port of the server.
     */
    private final int port;

    /**
     * The id of the client.
     */
    private Integer id;

    /**
     * The message queue.
     */
    private final BlockingQueue<Message> messageQueue;

    /**
     * The flag to check if the client is processing an action.
     */
    private boolean processingAction;

    /**
     * The username of the client.
     */
    private String username;

    /**
     * The game bean.
     */
    private GameBean game;

    /**
     * The view.
     */
    private final Ui view;

    /**
     * The skeleton of the server.
     */
    private VirtualServer server;

    /**
     * The player bean.
     */
    private PlayerBean player;

    /**
     * The list of the opponents.
     */
    private ArrayList<PlayerBean> opponents;

    /**
     * The timer to catch the ping.
     */
    private Timer catchPing;
    private Timer reconnectionTimer;

    /**
     * The reconnection thread. Every second it tries to reconnect to the server.
     */
    private final Thread reconnectionThread;

    /**
     * The constructor of the class.
     * @param host the address of the server.
     * @param port the port of the server.
     * @param view the view.
     * @throws RemoteException if there is an error in the connection.
     */
    public RMIClient(String host, int port, Ui view) throws RemoteException{
        this.view = view;
        this.address = host;
        this.port = port;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.setProperty("java.rmi.server.hostname", ip.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        //Initialization of player's attributes
        this.game = new GameBean();
        this.opponents = new ArrayList<>();
        messageQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        catchPing = new Timer();
        reconnectionThread = new Thread(() -> {
            reconnectionTimer = new Timer();
            reconnectionTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                     login();
                }
            }, 0, 1000);
        });
    }

    /**
     * This method tries to connect the client to the server.
     */
    public void login(){
        try {
            Registry registry = LocateRegistry.getRegistry(address, port);
            server = (VirtualServer) registry.lookup(Server.serverName);
            server.login(this);
            pickQueue();
        } catch (RemoteException | NotBoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method asks the user if he wants to join, create or reconnect to a game.
     */
    public void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException{
        cancelCatchPing();
        this.view.selectGame(startingGamesId, gamesWhitDisconnectionsId);
    }

    /**
     * Starts the reconnection thread.
     */
    public void reconnectAttempt(){
        reconnectionThread.start();
    }

    /**
     * This method sets the action to perform.
     * @param response is the action to perform.
     * @param startingGamesId is the list of the starting games' id.
     * @param gamesWithDisconnectionsId is the list of the games with disconnections id.
     */
    @Override
    public void setOnConnectionAction(int response, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId) {
        try {
            server.handleAction(response, this.id, username, startingGamesId, gamesWithDisconnectionsId);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method responds to the server ping.
     * @throws RemoteException if there is an error in the connection.
     */
    @Override
    public void pingNetwork() throws RemoteException {
        try {
            catchPing();
            server.pingConnection(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method catches the ping, resetting the timer. If the server doesn't respond in 5 seconds, it tries to reconnect.
     */
    public void catchPing(){
        catchPing.cancel();
        catchPing = new Timer();
        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                catchPing.cancel();
                reconnectAttempt();
            }
        }, 5000, 5000);
    }

    public void cancelCatchPing(){
        catchPing.cancel();
    }

    /**
     * This method sets the id of the client.
     * @param id the id of the client.
     */
    @Override
    public void setClientId(Integer id){
        this.id = id;
    }

    /**
     * This method asks the user to insert the username.
     * @throws RemoteException if there is an error in the connection.
     */
    public void askUsername() throws RemoteException{
        view.askNickname();
    }

    /**
     * This method sets the nickname of the client.
     * @param nickname the nickname of the client.
     */
    @Override
    public void setNickname(String nickname) {
        this.username = nickname;
        try {
            if (this.player != null) {
                player.setUsername(nickname);
            }
            else {
                this.player = new PlayerBean(this.username);
            }
            server.sendNickname(nickname, id);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method asks the user to insert the number of players in the lobby.
     * @throws RemoteException if there is an error in the connection.
     */
    public void askLobbySize() throws RemoteException{
        cancelCatchPing();
        this.view.askLobbySize();
    }

    /**
     * This method sets the size of the lobby.
     * @param size the size of the lobby.
     */
    public void setLobbySize(int size) {
        try {
            this.server.setLobbySize(size, this.id, username);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method set the color of the player.
     * @param color the color chosen by the player.
     * @throws RemoteException if there is an error in the connection.
     */
    public void setColor(Color color) throws RemoteException{
        this.server.setColor(color, id);
    }

    /**
     * This method sets the achievement of the player.
     * @param achievement the achievement chosen by the player.
     * @throws RemoteException if there is an error in the connection.
     */
    @Override
    public void chooseAchievement(Achievement achievement) {
        this.player.setAchievement(achievement);
        try {
            this.server.setAchievement(achievement, id);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + " in RMIClient.update");
        }
    }

    /**
     * Places the starter card.
     * @param side is the side where the starter card is placed.
     * @param starterCard is the starter card.
     */
    @Override
    public void placeStarterCard(boolean side, Card starterCard) {
        if (!side) {
            starterCard.setCurrentSide();
        }
        player.setStarterCard(starterCard);
        try {
            this.server.placeStarterCard(starterCard, id);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method sets the color of the player.
     * @param chosenColor the color chosen by the player.
     */
    @Override
    public void chooseColor(Color chosenColor) {
        player.setPionColor(chosenColor);
        try {
            this.setColor(chosenColor);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method places the card.
     * @param card the card to place.
     * @param placingCoordinates the coordinates where to place the card.
     */
    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (player.getState().equals(PlayerState.PLAY_CARD)) {
            try {
                server.placeCard(card, placingCoordinates, id);
                player.removeCardFromHand(card);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in placeCard");
            }
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    /**
     * Method to draw a card from the deck.
     * @param chosenDeck is the deck from which the card is drawn.
     */
    @Override
    public void drawCard(String chosenDeck) {
        if (player.getState().equals(PlayerState.DRAW_CARD)){
            try {
                server.drawCard(chosenDeck, id);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in drawCard");
            }
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    /**
     * Method to draw a card from the board.
     * @param index is the index of the card to draw.
     */
    @Override
    public void drawCardFromBoard(int index){
        if (player.getState().equals(PlayerState.DRAW_CARD)){
            try {
                server.drawCardFromBoard(index - 1, id);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in drawCardFromBoard");
            }
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    /**
     * Method to pick an action to perform from the queue.
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
     * Method to process the queue of messages.
     */
    private void processQueue() {
        if (!messageQueue.isEmpty() && !processingAction) {
            Message message = messageQueue.poll();
            processingAction = true;
            this.onMessage(message);
            processingAction = false;
            if (!messageQueue.isEmpty()) {
                processQueue();
            }
        }
    }

    /**
     * This method add a message coming from the server to the message queue.
     * @param message the message to add.
     */
    @Override
    public void update(Message message){
        messageQueue.add(message);
    }

    /**
     * This method handles the messages coming from the server.
     * @param message the message to handle.
     */
    public void onMessage(Message message) {
        String name;
        switch (message.getType()){
            case RECONNECTION:
                this.player = ((ReconnectionMessage) message).getPlayerBean();
                this.game = ((ReconnectionMessage) message).getGameBean();
                this.opponents = ((ReconnectionMessage) message).getOpponents();
                this.view.handleReconnection();
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
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
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, this.player.getHand(), 0, 3);
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
                if (!drawedCard.isNotBack()){
                    drawedCard.setCurrentSide();
                }
                player.setCardinHand(drawedCard);
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case SCORE_UPDATE:
                name = ((ScoreUpdateMessage) message).getName();
                if (username.equals(name)) {
                    player.setPoints(((ScoreUpdateMessage) message).getPoint());
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            p.setPoints(((ScoreUpdateMessage) message).getPoint());
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
                cancelCatchPing();
                this.view.declareWinners(winners);
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
     * This method sends a chat message to all the players of the lobby.
     * @param message the message to send.
     */
    @Override
    public void sendChatMessage(String message) {
        try {
            server.sendChatMessage(message, id);
        } catch (RemoteException e) {
            System.out.println(e.getMessage() + " sendChatMessage");
        }
    }

    /**
     * This method sends a chat message to a specific player.
     * @param receiver the receiver of the message.
     * @param message the message to send.
     */
    @Override
    public void sendChatMessage(String receiver, String message) {
        try {
            server.sendChatMessage(message, id, receiver);
        } catch (RemoteException e) {
            System.out.println(e.getMessage() + " sendChatMessage");
        }
    }

}
