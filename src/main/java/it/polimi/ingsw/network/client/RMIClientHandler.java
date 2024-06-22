package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface is used to define the methods that the server can call on the client.
 */
public interface RMIClientHandler extends Remote {

    /**
     * This method is used to ask the username to the client.
     * @throws RemoteException if there is a connection problem.
     */
    void askUsername() throws RemoteException;

    /**
     * This method is used to ask the lobby size to the client.
     * @throws RemoteException if there is a connection problem.
     */
    void askLobbySize() throws RemoteException;

    /**
     * This method is used to ask the player if he wants to join, create or reconnect to a game.
     * @throws RemoteException if there is a connection problem.
     */
    void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException;

    /**
     * This method add a message coming from the server to the message queue.
     * @param message the message to add.
     */
    void update(Message message) throws RemoteException;

    /**
     * This method is used to ping the client.
     * @throws RemoteException if there is a connection problem.
     */
    void pingNetwork() throws RemoteException;

    /**
     * This method is used to set the client id.
     * @param id the id to set.
     * @throws RemoteException if there is a connection problem.
     */
    void setClientId(Integer id) throws  RemoteException;
}
