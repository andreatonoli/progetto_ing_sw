package rmi;

import java.rmi.Remote;

//utiliziamo dal server per contattare il client
public interface VirtualView extends Remote {

    void showUpdate(String update);
}
