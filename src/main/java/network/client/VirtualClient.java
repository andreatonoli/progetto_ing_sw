package network.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

//utiliziamo dal server per contattare il client
public interface VirtualClient extends Remote {

    void showUpdate(String update) throws RemoteException;
    //String askNickname();
}
