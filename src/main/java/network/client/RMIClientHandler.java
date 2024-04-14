package network.client;

import model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//utiliziamo dal server per contattare il client
public interface RMIClientHandler extends Remote {
    int joinGame(List<Game> activeGames) throws RemoteException;
    int setLobbySize() throws RemoteException;
}
