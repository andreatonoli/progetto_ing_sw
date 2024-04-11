package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

//per inserire i metodi che vogliamo che il client chiami del server
public interface VirtualServer extends Remote {
    void login(VirtualView client) throws RemoteException;

}
