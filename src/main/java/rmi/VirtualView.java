package rmi;

import view.Ui;
import java.rmi.Remote;

//utiliziamo dal server per contattare il client
public interface VirtualView extends Remote, Ui {

    void showUpdate(String update);
    String askNickname();
}
