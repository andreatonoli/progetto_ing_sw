package network.server;

import java.io.IOException;
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

public class Server implements VirtualServer {

    private final Controller controller;
    private final static int CAPACITY = 10;
    private List<Game> activeGames;
    private final  Map<RMIClientHandler, String> client;
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

    public Server(Controller controller){
        this.controller=controller;
        this.client = Collections.synchronizedMap(new HashMap<>());
        this.activeGames = Collections.synchronizedList(new ArrayList<>());
    }

    //@Override
    public void login(RMIClientHandler client, String username) throws RemoteException {
        this.client.put(client, username);
        System.err.println("user "+ username + " connected and ready to die");
        //TODO: associare player instance al client
        //chiamare costruttore di player -> game.addPlayer
    }

    public static void main(String[] args) throws IOException {
        final String serverName = "GameServer";
        VirtualServer server = new Server(new Controller(new Game()));
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, stub);
        System.out.println("server bound. ");
    }

    public boolean usernameTaken(String username) throws RemoteException{
        return client.containsValue(username);
    }

}
