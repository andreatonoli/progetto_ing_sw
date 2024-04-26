package network.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Controller.ServerController;
import model.card.Achievement;
import model.card.Card;
import network.client.RMIClientHandler;

public class RMIServer implements VirtualServer {

    //private final Controller controller;
    private Queue<Action> actionQueue;
    private boolean processingAction;
    private final Server server;
    private final int port;
    private RMIConnection connection;
    private final ServerController controller;
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

    public RMIServer(Server server, int port, ServerController controller){
        this.server = server;
        this.port = port;
        this.controller = controller;
        this.startServer();
        actionQueue = new LinkedList<>();
        processingAction = false;
    }


    @Override
    public void login(RMIClientHandler client, String username) throws RemoteException {
        connection = new RMIConnection(server, client, username);
        server.login(connection, username);
    }

    public void startServer(){
        try {
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(Server.serverName, stub);
            System.out.println("RMI server bound.");
        } catch (RemoteException e) {
            System.out.println("Connection error");
        }
    }
    public boolean usernameTaken(String username) throws RemoteException{
        return server.usernameTaken(username);
    }

    @Override
    public void flipCard(Card card) throws RemoteException {
        addToQueue(() -> connection.flipCard(card));
        //connection.flipCard(card);
    }

    @Override
    public void placeStarterCard(Card card) throws RemoteException {
        connection.placeStarterCard(card);
    }

    @Override
    public void setAchievement(Achievement achievement) throws RemoteException {
        connection.setAchievement(achievement);
    }

    private void addToQueue(Action action) {
        actionQueue.add(action);
        if (!processingAction){
            processQueue();
        }
    }

    private synchronized void processQueue() {
        if (!actionQueue.isEmpty() && !processingAction) {
            Action nextAction = actionQueue.poll();
            processingAction = true;
            nextAction.execute();
            processingAction = false;
            if (!actionQueue.isEmpty()) {
                processQueue();
            }
        }
    }

}
