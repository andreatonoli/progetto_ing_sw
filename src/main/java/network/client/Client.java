package network.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import network.server.VirtualServer;
import view.*;

public class Client{
    public Client(){
        System.out.println("Premi [0] per giocare con la TUI.\nPremi un qualsiasi altro numero per giocare con la GUI");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        if (choice == 0){
            new Tui().run();
        }
        else{
            new Tui().run(); //TODO: mettere la gui
        }
    }
}
