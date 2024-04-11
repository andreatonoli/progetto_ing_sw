package rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import view.*;

public class RmiClient extends UnicastRemoteObject implements VirtualView {

    final VirtualServer server;
    //private Ui view;
    private final String nickname="pippo";
    public RmiClient(VirtualServer server) throws RemoteException{
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
            view = new Tui();
        }
        VirtualServer server = view.askServerInfo();
        new RmiClient(server).run();
    }
    @Override
    public void showUpdate(String update) throws RemoteException {
        //TODO
    }

    @Override
    public String askNickname() throws RemoteException{
        String nickname;
        System.out.println("input username: ");
        nickname = readInput();
        return nickname;
    }

    @Override
    public VirtualServer askServerInfo() throws RemoteException{
        return null;
    }
    public String readInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }


}
