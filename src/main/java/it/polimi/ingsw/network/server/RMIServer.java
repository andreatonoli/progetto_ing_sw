package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.RMIClientHandler;

public class RMIServer implements VirtualServer {
    private final BlockingQueue<Action> actionQueue;
    private boolean processingAction;
    private final Server server;
    private final int port;
    private final Map<String, RMIConnection> connections;

    public RMIServer(Server server, int port){
        this.server = server;
        this.port = port;
        connections = Collections.synchronizedMap(new HashMap<>());
        actionQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        pickQueue();
        this.startServer();
    }

    public void removeFromServer(String username) throws RemoteException {
        connections.get(username).cancelPing();
        int playersToDelete = connections.get(username).getLobby().getGame().getLobbySize()-1;
        addToQueue(() -> connections.get(username).getLobby().getGame().setLobbySize(playersToDelete));
        if (playersToDelete == 0) {
            server.removeGame(connections.get(username).getLobby());
        }
        addToQueue(() -> connections.remove(username));
        addToQueue(() -> server.removePlayers(username));
    }

    @Override
    public void login(RMIClientHandler client) throws RemoteException {
        RMIConnection c = new RMIConnection(server, client);
        String username = server.login(c);
        connections.put(username, c);
    }

    @Override
    public boolean usernameTaken(String username) throws RemoteException {
        return server.usernameTaken(username);
    }

    @Override
    public boolean userNotDisconnected(String username, int gameId) throws RemoteException {
        return server.userNotDisconnected(username, gameId);
    }

    public void pingConnection(String username) throws RemoteException{
        connections.get(username).catchPing();
    }

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
    public void sendChatMessage(String message, String sender, String receiver) throws RemoteException {
        addToQueue(() -> connections.get(sender).sendChatMessage(message, connections.get(receiver)));
    }
    @Override
    public void sendChatMessage(String message, String sender) throws RemoteException {
        addToQueue(() -> connections.get(sender).sendChatMessage(message));
    }

    @Override
    public void placeStarterCard(Card card, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).placeStarterCard(card));
    }

    @Override
    public void setColor(Color color, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).setColor(color));
    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates, String username) {
        addToQueue(() -> connections.get(username).placeCard(card, placingCoordinates));
    }

    @Override
    public void drawCard(String chosenDeck, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).drawCard(chosenDeck));
    }

    @Override
    public void drawCardFromBoard(int index, String username) throws RemoteException {
        addToQueue(() -> connections.get(username).drawCardFromBoard(index));
    }

    @Override
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

}
