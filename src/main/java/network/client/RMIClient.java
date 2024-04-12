package network.client;

import network.server.VirtualServer;
import view.TuiHandler;
import view.Ui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements RMIClientHandler{
    private String username;
    private Ui view;

    public RMIClient(String username, String host, int port, Ui view){
        this.username = username;
        this.view = view;
        final String serverName = "GameServer";
        try {
            Registry registry = LocateRegistry.getRegistry(host, port); //passare l'host name del server
            VirtualServer server =(VirtualServer) registry.lookup(serverName);
            while (server.usernameTaken(this.username)){
                System.out.println("username is already taken, please choose another: ");
                this.username = view.askNickname();
            }
            server.login(this, username);
        } catch (RemoteException | NotBoundException e){
            System.out.println("server not found");
        }
    }

}
