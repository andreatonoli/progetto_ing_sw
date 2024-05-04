package network.server;

import model.card.Achievement;
import network.client.RMIClientHandler;
import model.card.Card;

import java.rmi.Remote;
import java.rmi.RemoteException;

//per inserire i metodi che vogliamo che il client chiami del server
public interface VirtualServer extends Remote {
    void login(RMIClientHandler client, String username) throws RemoteException;
    boolean usernameTaken(String username) throws RemoteException;
    void flipCard(Card card) throws RemoteException;
    void placeStarterCard(Card card) throws RemoteException;
    void setAchievement(Achievement achievement) throws RemoteException;
    void pingConnection() throws RemoteException;
}
