package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIClientHandler extends Remote {
    void askUsername() throws RemoteException;
    void askLobbySize() throws RemoteException;
    void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) throws RemoteException;
    void update(Message message) throws RemoteException;
    void pingNetwork() throws RemoteException;
    void setClientId(Integer id) throws  RemoteException;
}
