package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.observer.Observer;

import java.util.List;

/**
 * This class is the abstract class that represents the connection between the server and the client.
 * It is extended by RMIConnection and SocketConnection.
 */
public abstract class Connection implements Observer {

    /**
     * This method sets the lobby of the connection.
     * @param controller is the controller that represents the lobby.
     */
    public abstract void setLobby(Controller controller);

    /**
     * This method returns the lobby of the connection.
     * @return the controller that represents the lobby.
     */
    public abstract Controller getLobby();

    /**
     * This method sends a message to the client.
     * @param message is the message that has to be sent.
     */
    public abstract void sendMessage(Message message);

    /**
     * This method asks the client whether it wants to create, join or reconnect to a lobby.
     * @param startingGamesId is the list of the id of the games that are starting.
     * @param gamesWhitDisconnectionsId is the list of the id of the games that have disconnections.
     */
    public abstract void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId);

    /**
     * This method handles the action of creating a game.
     */
    public abstract void createGame();

    /**
     * This method gets the username of the connection.
     * @return the username of the connection.
     */
    public abstract String getUsername();

    /**
     * This method sends a ping message to the client.
     */
    public abstract void ping();

    /**
     * This method reconnects the connection.
     * @param oldConnection is the old connection that belonged to the user and will be replaced by the new one.
     */
    public abstract void reconnect(Connection oldConnection);

    /**
     * This method sends a chat message to the client.
     * @param message is the message that has to be sent.
     * @param receiver is the connection that has to receive the message.
     */
    public abstract void sendChatMessage(String message, Connection receiver);

    /**
     * This method sends a chat message to the client.
     * @param message is the message that has to be sent.
     */
    public abstract void sendChatMessage(String message);

    /**
     * This method removes the connection from the server.
     * @param last is a boolean that is true if the connection is the last one to be removed.
     */
    public abstract void removeFromServer(boolean last);

    public abstract void waiting();
}
