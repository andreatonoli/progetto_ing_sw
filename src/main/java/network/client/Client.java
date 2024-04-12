package network.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import network.server.VirtualServer;
import view.*;

public class Client extends UnicastRemoteObject implements VirtualClient {

    final VirtualServer server;
    //private Ui view;
    private final String nickname="pippo";
    public Client(VirtualServer server) throws RemoteException{
        this.server=server;
    }

    private void run() throws RemoteException {
        this.server.login(this);
        this.runCli();
    }

    private void runCli() throws RemoteException{
        //Scanner scan = new Scanner(System.in);
        System.out.println("Ciao mamma");
        //while (true) {
        //    System.out.print("> ");
            //TODO inserire i comandi che da il client
        //}
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        boolean tuiParam=false;
        for (String arg : args){
            if (arg.equals("--cli") || arg.equals("-c")){
                tuiParam=true;
                break;
            }
        }
        Ui view = null;
        if (tuiParam){
            view = new Tui();
        }
        else{
            //TODO: avvio applicazione grafica
            view = new Tui(); //Placeholder
        }
        VirtualServer server = view.askServerInfo();
        //new Client(server).run();
    }
    @Override
    public void showUpdate(String update) throws RemoteException {
        //TODO
    }
}
