package network.client;

import java.util.*;

import network.server.VirtualServer;
import view.Ui;
import model.Game;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements RMIClientHandler{
    private String username;
    private Ui view;
    private VirtualServer server;

    public RMIClient(String username, String host, int port, Ui view) throws RemoteException{
        this.username = username;
        this.view = view;
        final String serverName = "GameServer";
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (VirtualServer) registry.lookup(serverName);
            while (server.usernameTaken(this.username)){
                System.out.println("Username is already taken, please choose another: ");
                this.username = view.askNickname();
            }
            server.login(this, this.username);
        } catch (RemoteException | NotBoundException e){
            System.out.println(e.getMessage());
        }
    }

    public Game selectGame(List<Game> activeGames){
        System.out.println("select one of the following game's lobby");
        for (int i=1; i<=activeGames.size(); i++){
            System.out.println("lobby " + i);
        }
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        return activeGames.get(scanner.nextInt());
    }

}
