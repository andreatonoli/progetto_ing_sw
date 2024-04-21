package network.client;

import Controller.Controller;
import model.Game;
import network.messages.Message;
import view.Ui;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//utiliziamo dal server per contattare il client
public interface RMIClientHandler extends Remote {
    int joinGame(List<Controller> activeGames) throws RemoteException;
    int setLobbySize() throws RemoteException;
    Ui getView() throws RemoteException;
}
