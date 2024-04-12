package view;

import network.server.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Ui extends Remote {
    String askNickname() throws  RemoteException;
    VirtualServer askServerInfo() throws RemoteException;
}
