package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.RMIClientHandler;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    void login(RMIClientHandler client) throws RemoteException;

    boolean usernameTaken(String username) throws RemoteException;

    boolean userNotDisconnected(String username, int gameId) throws RemoteException;

    void placeStarterCard(Card card, String username) throws RemoteException;

    void placeCard(Card card, int[] placingCoordinates, String username) throws RemoteException;

    void drawCard(String chosenDeck, String username) throws RemoteException;

    void drawCardFromBoard(int index, String username) throws RemoteException;

    void setAchievement(Achievement achievement, String username) throws RemoteException;

    void setColor(Color color, String username) throws RemoteException;

    void pingConnection(String username) throws RemoteException;

    void sendChatMessage(String message, String sender, String receiver) throws RemoteException;

    void sendChatMessage(String message, String sender) throws RemoteException;

    void removeConnections(String username) throws RemoteException;

}
