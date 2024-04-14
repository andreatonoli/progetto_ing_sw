package network.server;

import Controller.Controller;
import model.Game;
import network.client.RMIClientHandler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server {
    private List<Game> activeGames;
    private final Map<Connection, String> client;
    public final static int rmiPort = 1234;
    public final static int socketPort = 1235;
    public Server(){
        this.client = Collections.synchronizedMap(new HashMap<>());
        this.activeGames = Collections.synchronizedList(new ArrayList<>());
        //Starts the RMI server and the socket server
        startServer();
    }
    public static void main(String[] args) throws IOException {
        //Starts the main server
        new Server();
    }
    public void startServer(){
        new RMIServer(this, rmiPort);
        new SocketServer(this, socketPort);
    }
    //TODO: creare un handler in comune tra socket e RMI => inserirci booleano connesso/non connesso
    public void login(Connection client, String username) throws RemoteException {
        this.client.put(client, username);
        System.err.println("user "+ username + " connected and ready to die");
        //TODO: associare player instance al client

        //chiamare costruttore di player -> game.addPlayer
    }

    public boolean usernameTaken(String username) throws RemoteException{
        return client.containsValue(username);
    }
}
