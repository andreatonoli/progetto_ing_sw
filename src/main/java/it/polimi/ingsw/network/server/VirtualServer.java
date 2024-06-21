package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.RMIClientHandler;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualServer extends Remote {
    void login(RMIClientHandler client) throws RemoteException;
    void sendNickname(String nickname, Integer id) throws RemoteException;
    void handleAction(int actionToPerform, Integer id, String username, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException;
    void setLobbySize(int size, Integer id, String username) throws RemoteException;

    void placeStarterCard(Card card, Integer id) throws RemoteException;

    void placeCard(Card card, int[] placingCoordinates, Integer id) throws RemoteException;

    void drawCard(String chosenDeck, Integer id) throws RemoteException;

    void drawCardFromBoard(int index, Integer id) throws RemoteException;

    void setAchievement(Achievement achievement, Integer id) throws RemoteException;

    void setColor(Color color, Integer id) throws RemoteException;

    void pingConnection(Integer id) throws RemoteException;

    void sendChatMessage(String message, Integer id, String receiver) throws RemoteException;

    void sendChatMessage(String message, Integer id) throws RemoteException;

    void removeConnections(Integer id) throws RemoteException;

    int getAvailableId() throws RemoteException;

}
