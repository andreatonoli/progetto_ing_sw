package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.Ui;

import java.rmi.Remote;
import java.rmi.RemoteException;

//utiliziamo dal server per contattare il client
public interface RMIClientHandler extends Remote {
    int joinGame(int freeLobbies) throws RemoteException;
    int setLobbySize() throws RemoteException;
    Ui getView() throws RemoteException;
    void update(Message message) throws RemoteException;
    void pingNetwork() throws RemoteException;
}
