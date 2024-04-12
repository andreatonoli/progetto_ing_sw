package view;

import network.server.VirtualServer;

import java.rmi.RemoteException;

public class TuiHandler implements Ui{

    @Override
    public String askNickname() throws RemoteException {
        return null;
    }

    @Override
    public VirtualServer askServerInfo() throws RemoteException {
        return null;
    }
}
