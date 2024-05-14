package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import it.polimi.ingsw.Controller.ServerController;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.client.RMIClientHandler;

public class RMIServer implements VirtualServer {

    //private final Controller controller;
    private Queue<Action> actionQueue;
    private boolean processingAction;
    private final Server server;
    private final int port;
    //private RMIConnection connection;
    private Map<String, RMIConnection> connections;

    //TODO: sistemare controller
    public RMIServer(Server server, int port, ServerController controller){
        this.server = server;
        this.port = port;
        connections = Collections.synchronizedMap(new HashMap<>());
        actionQueue = new LinkedList<>();
        processingAction = false;
        pickQueue();
        this.startServer();
    }

    @Override
    public void login(RMIClientHandler client, String username) throws RemoteException {
        RMIConnection c = new RMIConnection(server, client, username);
        connections.put(username, c);
        server.login(c, username);
    }

    @Override
    public boolean usernameTaken(String username) throws RemoteException {
        return server.usernameTaken(username);
    }

    public void pingConnection(String username) throws RemoteException{
        addToQueue(() -> connections.get(username).catchPing());
    }

    //TODO: se faccio implementare virtualServer a Connection ogni volta che do uno stub ad una nuova connessione creo una connection diversa e vado a comunicare diremttamente
    public void startServer(){
        try {
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind(Server.serverName, stub);
            System.out.println("RMI server bound.");
        } catch (RemoteException e) {
            System.out.println("Connection error");
        }
    }

    @Override
    public void flipCard(Card card, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).flipCard(card));
    }

    @Override
    public void placeStarterCard(Card card, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).placeStarterCard(card));
    }

    //@Override
    public void placeCard(Card card, int[] placingCoordinates, String username) {
        addToQueue(() -> connections.get(username).placeCard(card, placingCoordinates));
    }

    //@Override
    public void drawCard(String chosenDeck, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).drawCard(chosenDeck));
    }

    //@Override
    public void drawCardFromBoard(int index, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).drawCardFromBoard(index));
    }

    //@Override
    public void setAchievement(Achievement achievement, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).setAchievement(achievement));
    }

    private void addToQueue(Action action) {
        actionQueue.add(action);
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
        if (!actionQueue.isEmpty() && !processingAction) {
            Action nextAction = actionQueue.poll();
            processingAction = true;
            nextAction.execute();
            processingAction = false;
            if (!actionQueue.isEmpty()) {
                processQueue();
            }
        }
    }
    //TODO: cambia le queue in blockiing queue


}
