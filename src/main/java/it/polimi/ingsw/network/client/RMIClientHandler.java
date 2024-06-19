package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.Ui;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//utiliziamo dal server per contattare il client
public interface RMIClientHandler extends Remote {
    String askUsername() throws RemoteException;
    String askUsername(int lobby) throws RemoteException;
    int joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException;
    int setLobbySize() throws RemoteException;
    void update(Message message) throws RemoteException;
    void pingNetwork() throws RemoteException;
}
