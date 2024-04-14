package view;

import network.server.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
}
