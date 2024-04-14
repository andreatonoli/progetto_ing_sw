package view;

import model.Game;
import network.server.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(List<Game> activeGames);

    int setLobbySize();
}
