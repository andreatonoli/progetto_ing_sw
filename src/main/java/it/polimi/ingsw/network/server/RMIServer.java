package it.polimi.ingsw.network.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

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
    private RMIConnection connection;
    private final ServerController controller;

    public RMIServer(Server server, int port, ServerController controller){
        this.server = server;
        this.port = port;
        this.controller = controller;
        actionQueue = new LinkedList<>();
        processingAction = false;
        pickQueue();
        this.startServer();
    }

    public void pingConnection() throws RemoteException{
        addToQueue(() -> connection.catchPing());
    }
    @Override
    public void login(RMIClientHandler client, String username) throws RemoteException {
        connection = new RMIConnection(server, client, username);
        server.login(connection, username);
    }
    //TODO: se faccio implementare virtualServer a Connection ogni volta che do uno stub ad una nuova connessione creo una connection diversa e vado a comunicare direttamente con quella
    public void startServer(){
        try {
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(Server.rmiPort);
            registry.bind(Server.serverName, stub);
            System.out.println("RMI server bound.");
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Connection error");
        }
    }
    public boolean usernameTaken(String username) throws RemoteException{
        return server.usernameTaken(username);
    }

    @Override
    public void flipCard(Card card) throws RemoteException {
        addToQueue(() -> connection.flipCard(card));
    }

    @Override
    public void placeStarterCard(Card card) throws RemoteException {
        addToQueue(() -> connection.placeStarterCard(card));
    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        addToQueue(() -> connection.placeCard(card, placingCoordinates));
    }

    @Override
    public void drawCard(String chosenDeck) throws RemoteException {
        addToQueue(() -> connection.drawCard(chosenDeck));
    }

    @Override
    public void drawCardFromBoard(int index) throws RemoteException {
        addToQueue(() -> connection.drawCardFromBoard(index));
    }

    @Override
    public void setAchievement(Achievement achievement) throws RemoteException {
        addToQueue(() -> connection.setAchievement(achievement));
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
