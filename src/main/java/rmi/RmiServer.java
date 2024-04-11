package rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import view.*;

import Controller.Controller;
import model.Game;

public class RmiServer implements VirtualServer {

    private final Controller controller;
    private final static int CAPACITY = 10;
    private final  Map<VirtualView, String> client;
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

    public RmiServer(Controller controller){
        this.controller=controller;
        this.client = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public synchronized void login(VirtualView client) throws RemoteException {
        //TODO sincronizza
        String nickname;
        nickname=client.askNickname();
        while(this.client.containsValue(nickname))
        {
            System.out.println("username already taken");
            nickname=client.askNickname();
        }
        System.out.println(nickname);
        this.client.put(client,nickname);
    }

    public static void main(String[] args) throws IOException {
        final String serverName = "GameServer";
        VirtualServer server = new RmiServer(new Controller(new Game()));
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, stub);
        System.out.println("server bound. ");
    }

}
