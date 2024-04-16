package network.server;

import Controller.*;
import model.Game;
import model.GameState;
import model.Player;

import java.util.*;

public class Server {
    private List<Game> activeGames;
    private List<Game> startingGames;
    private final Map<Connection, String> client;
    private final ServerController controller;
    public final static String serverName = "GameServer";
    public final static int rmiPort = 1234;
    public final static int socketPort = 1235;
    public Server(){
        this.controller = new ServerController(this);
        this.client = Collections.synchronizedMap(new HashMap<>());
        this.activeGames = Collections.synchronizedList(new ArrayList<>());
        this.startingGames = Collections.synchronizedList(new ArrayList<>());
        //Starts the RMI server and the socket server
        startServer();
    }
    public static void main(String[] args){
        //Starts the main server
        new Server();
    }
    public void startServer(){
        new RMIServer(this, rmiPort, this.controller);
        new SocketServer(this, socketPort, this.controller);
    }
    //TODO: creare un handler in comune tra socket e RMI => inserirci booleano connesso/non connesso
    public void login(Connection client, String username){
        this.client.put(client, username);
        System.err.println("user " + username + " connected and ready to die");
        if (this.startingGames.isEmpty()){
            client.createGame();
        }
        else{
            client.joinGame(this.startingGames);
        }
    }

    public boolean usernameTaken(String username){
        return client.containsValue(username);
    }
    public void createLobby(String username, int numPlayers){
        this.startingGames.add(controller.createLobby(username, numPlayers));
    }
    public void joinLobby(String username, int indexGame){
        Game game = this.startingGames.get(indexGame);
        boolean full = controller.joinLobby(username, game);
        if (full){
            activeGames.add(game);
            startingGames.remove(game);
        }
    }

}
