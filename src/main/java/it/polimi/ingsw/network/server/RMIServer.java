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
import it.polimi.ingsw.network.messages.Message;

public class RMIServer implements VirtualServer {
    private final BlockingQueue<Action> actionQueue;
    private boolean processingAction;
    private Integer lastId = 0;
    private final Server server;
    private final int port;
    private final Map<Integer, RMIConnection> connections;

    public RMIServer(Server server, int port){
        this.server = server;
        this.port = port;
        connections = Collections.synchronizedMap(new HashMap<>());
        actionQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        pickQueue();
        this.startServer();
    }

    @Override
    public synchronized int getAvailableId(){
        this.lastId++;
        return lastId;
    }
    
    @Override
    public void removeConnections(Integer id) throws RemoteException {
        connections.remove(id);
    }

    @Override
    public void login(RMIClientHandler client) throws RemoteException {
        Integer id = getAvailableId();
        RMIConnection c = new RMIConnection(server, client, this, id);
        connections.put(id, c);
        server.login(c);
    }

    @Override
    public void sendNickname(String nickname, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).setNickname(nickname));
    }

    @Override
    public void handleAction(int actionToPerform, Integer id, String username, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException {
        addToQueue(() -> connections.get(id).handleAction(actionToPerform, username, startingGamesId, gamesWhitDisconnectionsId));
    }

    @Override
    public void setLobbySize(int size, Integer id, String username) throws RemoteException {
        addToQueue(() -> connections.get(id).setLobbySize(size, username));
    }

    @Override
    public boolean usernameTaken(String username) throws RemoteException {
        return server.usernameTaken(username);
    }

    @Override
    public boolean userNotDisconnected(String username, int gameId) throws RemoteException {
        return server.userNotDisconnected(username, gameId);
    }

    public void pingConnection(Integer id) throws RemoteException{
        connections.get(id).catchPing();
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
    public void sendChatMessage(String message, Integer id, String receiver) throws RemoteException {
        addToQueue(() -> connections.get(id).sendChatMessage(message, connections.get(receiver)));
    }
    @Override
    public void sendChatMessage(String message, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).sendChatMessage(message));
    }

    @Override
    public void placeStarterCard(Card card, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).placeStarterCard(card));
    }

    @Override
    public void setColor(Color color, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).setColor(color));
    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates, Integer id) {
        addToQueue(() -> connections.get(id).placeCard(card, placingCoordinates));
    }

    @Override
    public void drawCard(String chosenDeck, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).drawCard(chosenDeck));
    }

    @Override
    public void drawCardFromBoard(int index, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).drawCardFromBoard(index));
    }

    @Override
    public void setAchievement(Achievement achievement, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).setAchievement(achievement));
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
