package network.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import network.server.VirtualServer;
import view.*;

public class Client{
    public static void main(String[] args){
        boolean tuiParam = false;
        for (String arg : args){
            if (arg.equals("--tui") || arg.equals("-t")){
                tuiParam = true;
                break;
            }
        }
        if (tuiParam){
           new Tui().run();
        }
        else{
            //TODO: avvio applicazione grafica
            new Tui().run(); //Placeholder
        }
    }
}
