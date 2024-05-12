package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.network.client.RMIClientHandler;
import it.polimi.ingsw.model.card.Card;

import java.rmi.Remote;
import java.rmi.RemoteException;

//per inserire i metodi che vogliamo che il client chiami del server
public interface VirtualServer extends Remote {
    void login(RMIClientHandler client, String username) throws RemoteException;
    boolean usernameTaken(String username) throws RemoteException;
    void flipCard(Card card) throws RemoteException;
    void placeStarterCard(Card card) throws RemoteException;
    void placeCard(Card card, int[] placingCoordinates) throws RemoteException;
    void drawCard(String chosenDeck) throws RemoteException;
    void drawCardFromBoard(int index) throws RemoteException;
    void setAchievement(Achievement achievement) throws RemoteException;
    void pingConnection() throws RemoteException;
}
