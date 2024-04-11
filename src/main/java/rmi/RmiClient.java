package rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements VirtualView {

    final VirtualServer server;
    private final String nickname="pippo";
    public RmiClient(VirtualServer server) throws RemoteException{
        this.server=server;
    }

    private void run() throws RemoteException {
        this.server.login(this);
        this.runCli();
    }

    private void runCli(){
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            //TODO inserire i comandi che da il client
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "GameServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234); //passare l'host name del server
        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        new RmiClient(server);
    }

    @Override
    public void showUpdate(String update) {
        //TODO
    }

    @Override
    public String askNickname() {
        return null;
    }

}
