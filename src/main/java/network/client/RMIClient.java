package network.client;

import model.player.Player;
import network.messages.CommonCardUpdateMessage;
import network.messages.Message;
import network.messages.StarterCardMessage;
import network.server.VirtualServer;
import view.Ui;
import model.card.Card;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class RMIClient extends UnicastRemoteObject implements RMIClientHandler {
    private String username;
    private Ui view;
    private VirtualServer server;
    private Card[] commonResources;
    private Card[] commonGold;
    private Card starterCard;
    private ArrayList<Player> opponents;
    //private HashMap<Integer, Card> board1;
    //private HashMap<Integer, Card> board2;
    //private HashMap<Integer, Card> board3;
    //private HashMap<Integer, Card> board4;

    public RMIClient(String username, String host, int port, Ui view) throws RemoteException{
        this.username = username;
        this.view = view;
        //TODO: capire dove metterli
        this.commonResources = new Card[2];
        this.commonGold = new Card[2];
        final String serverName = "GameServer";
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (VirtualServer) registry.lookup(serverName);
            while (server.usernameTaken(this.username)){
                System.out.println("Username is already taken, please choose another: ");
                this.username = view.askNickname();
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
    public void placeStarterCard(Card card){

    }

    /**
     * gets messages from the server and updates the view according to the message type
     * @param message sent from the server
     */
    @Override
    public void update(Message message) {
        switch (message.getType()){
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
                    this.placeStarterCard(starterCard);
                }
                break;
            case SCOREBOARD_UPDATE:
                //TODO: stampa scoreboard
                break;
            case GENERIC_MESSAGE:
                this.view.showText(message.toString());
                break;
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
