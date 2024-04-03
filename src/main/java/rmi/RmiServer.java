package rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Controller.Controller;
import model.Game;

public class RmiServer implements VirtualServer {

    final Controller controller;
    final List<VirtualView> clients = new ArrayList<>();
    final BlockingQueue<String> updates = new ArrayBlockingQueue<>(10);

    //da usare tramite updates.put(valore) cosi che aggora tutti i cliet
    private void broadcastUpdateThread() throws InterruptedException {
        while(true){
            String update = updates.take(); //da modificare come serve
            synchronized (this.clients){
                for (var c : clients){
                    c.showUpdate(update);
                }
            }
        }
    }

    public RmiServer(Controller controller){
        this.controller=controller;
    }

    @Override
    public synchronized void connect(VirtualView client) throws RemoteException {
        this.clients.add(client);
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
