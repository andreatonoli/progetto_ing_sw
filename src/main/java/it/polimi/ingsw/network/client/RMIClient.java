package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.CardBack;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.VirtualServer;
import it.polimi.ingsw.network.server.Action;
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
    private BlockingQueue<Message> messageQueue;
    private Queue<Action> actionQueue;
    private boolean processingAction;
    private static final String serverName = "GameServer";
    private String username;
    private final Ui view;
    private VirtualServer server;
    private Card[] commonResources;
    private Card[] commonGold;
    private Card starterCard;
    private final Achievement[] commonAchievement;
    private PlayerBean player;
    private ArrayList<PlayerBean> opponents;

    public RMIClient(String username, String host, int port, Ui view) throws RemoteException{
        this.username = username;
        this.view = view;
        //Initialization of player's attributes
        this.player = new PlayerBean(username);
        this.opponents = new ArrayList<>();
        this.commonAchievement = new Achievement[2];
        this.commonResources = new Card[2];
        this.commonGold = new Card[2];
        messageQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        actionQueue = new LinkedList<>();
        pickQueue();
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (VirtualServer) registry.lookup(serverName);
            while (server.usernameTaken(this.username)){
                System.out.println("Username is already taken, please choose another: ");
                this.username = view.askNickname();
                player.setUsername(this.username);
            }
            server.login(this, this.username);
        } catch (RemoteException | NotBoundException e){
            System.out.println(e.getMessage());
        }
    }

    public int joinGame(int freeLobbies) throws RemoteException{
        return this.view.selectGame(freeLobbies);
    }

    public int setLobbySize() throws RemoteException{
        return this.view.setLobbySize();
    }
    public void flipCard(Card card) throws RemoteException{
        this.server.flipCard(card);
    }
    public void placeStarterCard(Card card) throws RemoteException{
        this.server.placeStarterCard(card);
    }
    public void setPrivateAchievement(Achievement toBeSet) throws RemoteException {
        this.server.setAchievement(toBeSet);
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
    private void addToQueue(Action action){
        actionQueue.add(action);
    }

    //TODO: ANDREAAAAAAAAAAAAAAAAAAAA aggiungi in queue con update
    @Override
    public void pingNetwork() throws RemoteException{
        addToQueue(() -> {
            try {
                server.pingConnection();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void update(Message message){
        messageQueue.add(message);
    }

    /**
     * gets messages from the messageQueue and updates the view according to the message type
     * @param message sent from the server
     */
    public void onMessage(Message message) {
        String name;
        switch (message.getType()){
            case OPPONENTS:
                ArrayList<String> playersName = ((OpponentsMessage) message).getPlayers();
                for (String s : playersName){
                    if(!s.equalsIgnoreCase(username)){
                        opponents.add(new PlayerBean(s));
                    }
                }
                break;
            case CARD_HAND:
                //Copied the message body into the player's cards
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, this.player.getHand(), 0, 3);
                //TODO: SISTEMARE
                //for (int i=0; i<player.getHand().length; i++){
                //    System.out.println(player.getCard(i));
                //}
                break;
            case COMMON_ACHIEVEMENT:
                System.arraycopy(((AchievementMessage) message).getAchievements(), 0, this.commonAchievement, 0, 2);
                break;
            case PRIVATE_ACHIEVEMENT:
                this.player.setAchievement(this.view.chooseAchievement(((AchievementMessage) message).getAchievements()));
                try {
                    this.setPrivateAchievement(player.getAchievement());
                } catch (RemoteException e) {
                    System.err.println(e.getMessage() + " in RMIClient.update");
                }
                break;
            case COMMON_GOLD_UPDATE:
                if(commonGold[0] == null){
                    commonGold[0] = ((CommonCardUpdateMessage) message).getCard();
                }
                else{
                    commonGold[1] = ((CommonCardUpdateMessage) message).getCard();
                }
                break;
            case COMMON_RESOURCE_UPDATE:
                if(commonResources[0] == null){
                    commonResources[0] = ((CommonCardUpdateMessage) message).getCard();
                }
                else{
                    commonResources[1] = ((CommonCardUpdateMessage) message).getCard();
                }
                break;
            case STARTER_CARD:
                //TODO: usare printStarterCard e non chiedere più di flippare ogni volta
                this.starterCard = ((StarterCardMessage) message).getCard();
                this.view.printCard(starterCard);
                boolean choice = this.view.askToFlip();
                if (choice){
                    try {
                        this.flipCard(starterCard);
                    } catch (RemoteException e) {
                        System.err.println(e.getMessage() + " in RMIClient.update");
                    }
                }
                else{
                    try{
                        this.placeStarterCard(starterCard);
                    }
                    catch (RemoteException e){
                        System.err.println(e.getMessage());
                    }
                }
                break;
            case SCORE_UPDATE:
                name = ((ScoreUpdateMessage) message).getName();
                if (username.equals(name)) {
                    player.addPoints(((ScoreUpdateMessage) message).getPoint());
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            player.addPoints(((ScoreUpdateMessage) message).getPoint());
                        }
                    }
                }
                this.view.printViewWithCommands(this.player.getBoard(),this.player.getHand(),this.username,this.commonResources,this.commonGold, this.commonAchievement, this.opponents,this.player.getChat());
                break;
            case PLAYER_STATE:
                PlayerState playerState = ((PlayerStateMessage) message).getState();
                name = ((PlayerStateMessage) message).getName();
                if (username.equals(name)) {
                    player.setState(playerState);
                }
                else {
                    //TODO: informare i giocatori che non stanno giocando il turno
                }
                //this.view.printViewWithCommands(this.player.getBoard(),this.player.getHand(),this.username,this.commonResources,this.commonGold, this.commonAchievement, this.opponents,this.player.getChat());
                break;
            case CARD_UPDATE:
                Card drawedCard = ((UpdateCardMessage) message).getCard();
                player.setCardinHand(drawedCard);
                this.view.printViewWithCommands(this.player.getBoard(),this.player.getHand(),this.username,this.commonResources,this.commonGold, this.commonAchievement, this.opponents,this.player.getChat());
                break;
            case DECK_UPDATE:
                Card first = ((UpdateDeckMessage) message).getCard();
                //TODO: aggiornare in view il deck
                break;
            case PLAYERBOARD_UPDATE:
                PlayerBoard playerBoard = ((PlayerBoardUpdateMessage) message).getpBoard();
                name = ((PlayerBoardUpdateMessage) message).getName();
                if (name.equalsIgnoreCase(username)){
                    player.setBoard(playerBoard);
                    this.view.printViewWithCommands(playerBoard,this.player.getHand(),this.username,this.commonResources,this.commonGold, this.commonAchievement, this.opponents,this.player.getChat());
                }
                else{
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            player.setBoard(playerBoard);
                        }
                    }
                }
                break;
            case GENERIC_MESSAGE:
                //TODO: messaggi che si accumulano finiscono qui e non so perchè
                this.view.showText(message.toString());
                break;
                //aggiungi case ping
            default:
                break;
        }
    }
    @Override
    public Ui getView(){
    //    return view;
        return null;
    }

    @Override
    public void sendChatMessage(String message) {

    }

    @Override
    public void sendChatMessage(String receiver, String message) {

    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (player.getState().equals(PlayerState.PLAY_CARD)) {
            try {
                server.placeCard(card, placingCoordinates);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in placeCard");
            }
        }
        else{
            //TODO: errore profondo
        }
    }

    @Override
    public void drawCard(String chosenDeck) {
        if (player.getState().equals(PlayerState.DRAW_CARD)){
            try {
                server.drawCard(chosenDeck);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in drawCard");
            }
        }
        else{
            //TODO: errore profondo
        }
    }

    @Override
    public void drawCardFromBoard(int index){
        if (player.getState().equals(PlayerState.DRAW_CARD)){
            try {
                server.drawCardFromBoard(index - 1);
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in drawCardFromBoard");
            }
        }
        else{
            //TODO: errore profondo
        }
    }

    @Override
    public ArrayList<PlayerBean> getPlayers() {
        return null;
    }
}
