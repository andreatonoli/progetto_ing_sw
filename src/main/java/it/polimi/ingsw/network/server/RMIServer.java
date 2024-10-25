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

/**
 * This class is the RMI server that handles the connection between the server and the client.
 */
public class RMIServer implements VirtualServer {

    /**
     * List of actions that the server has to perform.
     */
    private final BlockingQueue<Action> actionQueue;

    /**
     * Boolean that represents if the server is processing an action.
     */
    private boolean processingAction;

    /**
     * Integer that represents the last id assigned to a connection.
     */
    private Integer lastId = 0;

    /**
     * Link to the main server.
     */
    private final Server server;

    /**
     * Integer that represents the port of the server.
     */
    private final int port;

    /**
     * Map that links the connections to their id.
     */
    private final Map<Integer, RMIConnection> connections;

    /**
     * Constructor of the class.
     * @param server is the main server.
     * @param port is the port of the server.
     */
    public RMIServer(Server server, int port){
        this.server = server;
        this.port = port;
        connections = Collections.synchronizedMap(new HashMap<>());
        actionQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        pickQueue();
        this.startServer();
    }

    /**
     * This method returns the last id assigned to a connection.
     * @return the last id assigned to a connection.
     */
    @Override
    public synchronized int getAvailableId(){
        this.lastId++;
        return lastId;
    }

    /**
     * This method removes the connection from the server.
     * @param id is the id of the connection that has to be removed.
     */
    @Override
    public void removeConnections(Integer id) throws RemoteException {
        connections.remove(id);
    }

    /**
     * This method handles the connection of a client.
     * @param client is the client that has to be connected.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void login(RMIClientHandler client) throws RemoteException {
        Integer id = getAvailableId();
        RMIConnection c = new RMIConnection(server, client, this, id);
        connections.put(id, c);
        server.login(c);
    }

    /**
     * This method sets the nickname of the client in the connection.
     * @param nickname is the nickname that has to be set.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void sendNickname(String nickname, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).sendNickname(nickname));
    }

    /**
     * This method calls the connection method that perform the action.
     * @param actionToPerform is the action that has to be performed.
     * @param id is the id of the connection.
     * @param username is the username of the connection.
     * @param startingGamesId is the list of the id of the games that are starting.
     * @param gamesWhitDisconnectionsId is the list of the id of the games that have disconnections.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void handleAction(int actionToPerform, Integer id, String username, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException {
        addToQueue(() -> connections.get(id).handleAction(actionToPerform, username, startingGamesId, gamesWhitDisconnectionsId));
    }

    /**
     * This method sets the lobby of the connection.
     * @param size is the size of the lobby.
     * @param id is the id of the connection.
     * @param username is the username of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void setLobbySize(int size, Integer id, String username) throws RemoteException {
        addToQueue(() -> connections.get(id).setLobbySize(size, username));
    }

    /**
     * This method respond to the ping.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    public void pingConnection(Integer id) throws RemoteException{
        connections.get(id).catchPing();
    }

    /**
     * Start the rmi server.
     */
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

    /**
     * This method sends a chat message to the server.
     * @param message is the message that has to be sent.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void sendChatMessage(String message, Integer id, String receiver) throws RemoteException {
        addToQueue(() -> connections.get(id).sendChatMessage(message, server.getClientFromName(receiver)));
    }

    /**
     * This method sends a chat message to the client.
     * @param message is the message that has to be sent.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void sendChatMessage(String message, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).sendChatMessage(message));
    }

    /**
     * This method place the starter card.
     * @param card is the card that has to be placed.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void placeStarterCard(Card card, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).placeStarterCard(card));
    }

    /**
     * This method sets the color of the player.
     * @param color is the color that has to be set.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void setColor(Color color, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).setColor(color));
    }

    /**
     * This method permits the player to place a card.
     * @param card is the card that has to be placed.
     * @param placingCoordinates are the coordinates where the card has to be placed.
     * @param id is the id of the connection.
     */
    @Override
    public void placeCard(Card card, int[] placingCoordinates, Integer id) {
        addToQueue(() -> connections.get(id).placeCard(card, placingCoordinates));
    }

    /**
     * This method permits the player to draw a card.
     * @param chosenDeck is the deck from which the card has to be drawn.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void drawCard(String chosenDeck, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).drawCard(chosenDeck));
    }

    /**
     * This method permits the player to draw a card from the board.
     * @param index is the index of the card that has to be drawn.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void drawCardFromBoard(int index, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).drawCardFromBoard(index));
    }

    /**
     * This method permits the player to set his personal achievement.
     * @param achievement is the achievement that has to be set.
     * @param id is the id of the connection.
     * @throws RemoteException if there is a connection error.
     */
    @Override
    public void setAchievement(Achievement achievement, Integer id) throws RemoteException {
        addToQueue(() -> connections.get(id).setAchievement(achievement));
    }

    /**
     * Adds the actions to the queue.
     * @param action is the action that has to be added.
     */
    private void addToQueue(Action action) {
        actionQueue.add(action);
    }

    /**
     * This method sets a timer that picks the action from the queue.
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
     * This method processes the action in the queue.
     */
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
