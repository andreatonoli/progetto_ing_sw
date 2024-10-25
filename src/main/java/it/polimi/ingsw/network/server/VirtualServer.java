package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.RMIClientHandler;
import it.polimi.ingsw.model.card.Card;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface is used to define the methods that the client can call on the server.
 */
public interface VirtualServer extends Remote {

    /**
     * This method is used to log in a client to the server.
     * @param client the client that wants to log in.
     * @throws RemoteException if the connection is lost.
     */
    void login(RMIClientHandler client) throws RemoteException;

    /**
     * This method sends the nickname of the client to the server.
     * @param nickname the nickname of the client.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void sendNickname(String nickname, Integer id) throws RemoteException;

    /**
     * This method handles the actions that the client wants to perform.
     * @param actionToPerform the action that the client wants to perform.
     * @param id the id of the client.
     * @param username the username of the client.
     * @param startingGamesId the list of the games that are waiting for players.
     * @param gamesWhitDisconnectionsId the list of the games that have disconnections.
     * @throws RemoteException if the connection is lost.
     */
    void handleAction(int actionToPerform, Integer id, String username, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException;

    /**
     * This method sets the size of the lobby the client wants to create.
     * @param size the size of the lobby.
     * @param id the id of the client.
     * @param username the username of the client.
     * @throws RemoteException if the connection is lost.
     */
    void setLobbySize(int size, Integer id, String username) throws RemoteException;

    /**
     * This method places the starter card of the client.
     * @param card the card that the client wants to place.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void placeStarterCard(Card card, Integer id) throws RemoteException;

    /**
     * This method places the card of the client.
     * @param card the card that the client wants to place.
     * @param placingCoordinates the coordinates where the client wants to place the card.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void placeCard(Card card, int[] placingCoordinates, Integer id) throws RemoteException;

    /**
     * This method draws a card from the deck.
     * @param chosenDeck the deck from which the client wants to draw the card.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void drawCard(String chosenDeck, Integer id) throws RemoteException;

    /**
     * This method draws a card from the board.
     * @param index the index of the card that the client wants to draw.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void drawCardFromBoard(int index, Integer id) throws RemoteException;

    /**
     * This method sets the personal achievements of the client.
     * @param achievement the achievement that the client wants to set.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void setAchievement(Achievement achievement, Integer id) throws RemoteException;

    /**
     * This method sets the personal color of the client.
     * @param color the color that the client wants to set.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void setColor(Color color, Integer id) throws RemoteException;

    /**
     * This method responds to the ping sent from the server.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void pingConnection(Integer id) throws RemoteException;

    /**
     * This method sends a chat message.
     * @param message the message that the client sent.
     * @param id the id of the client.
     * @param receiver the receiver of the message.
     * @throws RemoteException if the connection is lost.
     */
    void sendChatMessage(String message, Integer id, String receiver) throws RemoteException;

    /**
     * This method sends a global chat message.
     * @param message the message that the client sent.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void sendChatMessage(String message, Integer id) throws RemoteException;

    /**
     * This method removes a connection from the server.
     * @param id the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    void removeConnections(Integer id) throws RemoteException;

    /**
     * This method gives the client his personal id.
     * @return the id of the client.
     * @throws RemoteException if the connection is lost.
     */
    int getAvailableId() throws RemoteException;

}
