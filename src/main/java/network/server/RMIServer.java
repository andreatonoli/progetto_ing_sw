package network.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Controller.Controller;
import model.Game;
import network.client.RMIClientHandler;

public class RMIServer implements VirtualServer {

    //private final Controller controller;
    private final Server server;
    private final int port;
    private final static int CAPACITY = 10;
    private final BlockingQueue<String> updates = new ArrayBlockingQueue<>(CAPACITY);

    //da usare tramite updates.put(valore) cosi che aggora tutti i cliet
   // private void broadcastUpdateThread() throws InterruptedException {
   //     while(true){
   //         String update = updates.take(); //da modificare come serve
   //         synchronized (this.clients){
   //             for (var c : clients){
   //                 c.showUpdate(update);
   //             }
   //         }
   //     }
   // }

    public RMIServer(Server server, int port){
        this.server = server;
        this.port = port;
        this.startServer();
    }

    //@Override
    public void login(RMIClientHandler client, String username) throws RemoteException {
        server.login(client, username);
    }

    public void startServer(){
        try {
            final String serverName = "GameServer";
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(serverName, stub);
            System.out.println("RMI server bound.");
        } catch (RemoteException e) {
            System.out.println("Connection error");
        }
    }
    public boolean usernameTaken(String username) throws RemoteException{
        return server.usernameTaken(username);
    }

}
