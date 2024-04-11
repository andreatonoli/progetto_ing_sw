package rmi;

import view.Ui;
import java.rmi.Remote;
import java.rmi.RemoteException;

//utiliziamo dal server per contattare il client
public interface VirtualView extends Remote, Ui{

    void showUpdate(String update) throws RemoteException;
    //String askNickname();
}
