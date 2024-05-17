package it.polimi.ingsw.network.server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ServerController;

import java.util.*;

public class Server {
    public static final int MAX_PLAYERS_PER_LOBBY = 4; /** sets max number of players */
    public static final int MIN_PLAYERS_PER_LOBBY = 2; /** sets min number of players */
    public final static String serverName = "GameServer";
    public final static int rmiPort = 1234;
    public final static int socketPort = 1235;
    private List<Controller> activeGames;
    private List<Controller> startingGames;
    private final Map<String, Connection> client;
    private final ServerController controller;
    public Server(){
        this.controller = new ServerController(this);
        this.client = Collections.synchronizedMap(new HashMap<>());
        this.activeGames = Collections.synchronizedList(new ArrayList<>());
        this.startingGames = Collections.synchronizedList(new ArrayList<>());
        //Starts the RMI server and the socket server
        startServer();
    }
    public void startServer(){
        new RMIServer(this, rmiPort, this.controller);
        new SocketServer(this, socketPort, this.controller);
    }
    public void login(Connection client, String username){
        this.client.put(username, client);
        System.err.println("user " + username + " connected");
        new Thread(client::ping).start();
        if (this.startingGames.isEmpty()){
            client.createGame();
        }
        else{
            client.joinGame(this.startingGames);
        }
    }

    public boolean usernameTaken(String username){
        return client.containsKey(username);
    }
    public void createLobby(String username, int numPlayers){
        this.startingGames.add(controller.createLobby(username, numPlayers));
    }
    public void joinLobby(String username, int indexGame){
        Controller controller = this.startingGames.get(indexGame);
        boolean full = this.controller.joinLobby(username, controller);
        if (full){
            activeGames.add(controller);
            startingGames.remove(controller);
        }
    }
    public Connection getClientFromName(String name){
        return this.client.get(name);
    }

}
