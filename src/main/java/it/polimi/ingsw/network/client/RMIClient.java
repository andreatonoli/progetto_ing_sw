package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.VirtualServer;
import it.polimi.ingsw.view.Ui;
import it.polimi.ingsw.model.card.Card;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIClient extends UnicastRemoteObject implements RMIClientHandler, ClientInterface {
    private final BlockingQueue<Message> messageQueue;
    private boolean processingAction;
    private static final String serverName = "GameServer";
    private String username;
    private int selector = -2;
    private GameBean game;
    private final Ui view;
    private VirtualServer server;
    private PlayerBean player;
    private ArrayList<PlayerBean> opponents;

    public RMIClient(String host, int port, Ui view) throws RemoteException{
        this.view = view;
        //Initialization of player's attributes
        this.game = new GameBean();
        this.opponents = new ArrayList<>();
        messageQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        pickQueue();
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (VirtualServer) registry.lookup(serverName);
        } catch (RemoteException | NotBoundException e){
            System.out.println(e.getMessage());
        }
    }

    public void login(){
        try {
            server.login(this);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public String askUsername() throws RemoteException{
        this.view.askNickname();
        while (server.usernameTaken(this.username)) {
            System.out.println("Username is already taken, please choose another: ");
            this.view.askNickname();
        }
        return username;
    }

    public String askUsername(int lobby) throws RemoteException{
        view.askNickname();
        if(!server.userNotDisconnected(username, lobby)) {
            System.out.println("there is no player disconnected in game " + lobby + " with that name.");
        }
        else {
            this.player = new PlayerBean(username);
        }
        return username;
    }

    public int joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException{
        do{
            this.view.selectGame(startingGamesId, gamesWhitDisconnectionsId);
        } while (selector == -2);
        return selector;
    }

    public int setLobbySize() throws RemoteException{
        this.view.askLobbySize();
        return selector;
    }

    public void placeStarterCard(Card card) throws RemoteException{
        this.server.placeStarterCard(card, username);
    }
    public void setPrivateAchievement(Achievement toBeSet) throws RemoteException {
        this.server.setAchievement(toBeSet, username);
    }

    public void setColor(Color color) throws RemoteException{
        this.server.setColor(color, username);
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
            this.onMessage(message);
            processingAction = false;
            if (!messageQueue.isEmpty()) {
                processQueue();
            }
        }
    }

    @Override
    public void pingNetwork() throws RemoteException {
        try {
            server.pingConnection(username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Message message){
        messageQueue.add(message);
    }
    /**
     * gets messages from the messageQueue and updates the view according to the message type.
     * @param message sent from the server.
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
                this.view.declareWinners(winners);
                //try {
                //    server.removeFromServer(username);
                //} catch (RemoteException e) {
                //    System.out.println(e.getMessage() + " in removeFromServer");
                //}
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
    public void setOnConnectionAction(int response, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId) {
        selector = response;
    }

    @Override
    public void setLobbySize(int size) {
        selector = size;
    }

    @Override
    public void placeStarterCard(boolean side, Card starterCard) {
        if (!side) {
            starterCard.setCurrentSide();
        }
        player.setStarterCard(starterCard);
        try {
            this.placeStarterCard(starterCard);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void chooseAchievement(Achievement achievement) {
        this.player.setAchievement(achievement);
        try {
            this.setPrivateAchievement(player.getAchievement());
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + " in RMIClient.update");
        }
    }

    @Override
    public void chooseColor(Color chosenColor) {
        player.setPionColor(chosenColor);
        try {
            this.setColor(chosenColor);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void sendChatMessage(String message) {
        try {
            server.sendChatMessage(message, username);
        } catch (RemoteException e) {
            System.out.println(e.getMessage() + " sendChatMessage");
        }
    }

    @Override
    public void sendChatMessage(String receiver, String message) {
        try {
            server.sendChatMessage(message, username, receiver);
        } catch (RemoteException e) {
            System.out.println(e.getMessage() + " sendChatMessage");
        }
    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (player.getState().equals(PlayerState.PLAY_CARD)) {
            try { //TODO: imparare a leggere una griglia per capire se così traspongo giuste le coordinate
                server.placeCard(card, placingCoordinates, username);
                player.removeCardFromHand(card);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in placeCard");
            }
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    @Override
    public void drawCard(String chosenDeck) {
        if (player.getState().equals(PlayerState.DRAW_CARD)){
            try {
                server.drawCard(chosenDeck, username);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in drawCard");
            }
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }

    @Override
    public void drawCardFromBoard(int index){
        if (player.getState().equals(PlayerState.DRAW_CARD)){
            try {
                server.drawCardFromBoard(index - 1, username);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in drawCardFromBoard");
            }
        }
        else{
            update(new GenericMessage("\nThere's a time and place for everything! But not now.\n"));
        }
    }
}
