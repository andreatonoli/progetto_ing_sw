package network.client;

import model.card.Achievement;
import model.enums.PlayerState;
import model.player.Player;
import network.messages.*;
import network.server.Action;
import network.server.VirtualServer;
import view.Ui;
import model.card.Card;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RMIClient extends UnicastRemoteObject implements RMIClientHandler {
    private Queue<Message> messageQueue;
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
    //private HashMap<Integer, Card> board1;
    //private HashMap<Integer, Card> board2;
    //private HashMap<Integer, Card> board3;
    //private HashMap<Integer, Card> board4;

    public RMIClient(String username, String host, int port, Ui view) throws RemoteException{
        this.username = username;
        this.view = view;
        //Initialization of player's attributes
        this.player = new PlayerBean(username);
        this.commonAchievement = new Achievement[2];
        this.commonResources = new Card[2];
        this.commonGold = new Card[2];
        messageQueue = new LinkedList<>();
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
    private void addToQueue(Action action) {
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
        addToQueue(() -> {
            try {
                server.pingConnection();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * gets messages from the messageQueue and updates the view according to the message type
     * @param message sent from the server
     */
    public void onMessage(Message message) {
        switch (message.getType()){
            case CARD_HAND:
                //Copied the message body into the player's cards
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, this.player.getHand(), 0, 3);
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
            case SCOREBOARD_UPDATE:
                //this.view.printView(this.player.getBoard(),this.player.getHand(),this.username,this.commonResources,this.commonGold, this.commonAchievement, this.opponents,this.player.getChat());
                break;
            case PLAYER_STATE:
                PlayerState playerState = ((PlayerStateMessage) message).getState();
                //this.view.printPlayerState(playerState);
                break;
            case GENERIC_MESSAGE:
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

}
