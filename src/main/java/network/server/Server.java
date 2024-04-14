package network.server;

import Controller.Controller;
import model.Game;
import model.GameState;
import model.Player;

import java.util.*;

public class Server {
    private List<Game> activeGames;
    private List<Game> startingGames;
    private final Map<Connection, String> client;
    public final static String serverName = "GameServer";
    public final static int rmiPort = 1234;
    public final static int socketPort = 1235;
    public Server(){
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
        new RMIServer(this, rmiPort);
        new SocketServer(this, socketPort);
    }
    //TODO: creare un handler in comune tra socket e RMI => inserirci booleano connesso/non connesso
    public void login(Connection client, String username){
        this.client.put(client, username);
        System.err.println("user "+ username + " connected and ready to die");
        Game game;
        if (this.startingGames.isEmpty()){
            game = new Game(client.setLobbySize());
            this.startingGames.add(game);
        }
        else{
            int gameIndex = client.joinGame(startingGames);
            if (gameIndex == startingGames.size()){
                this.startingGames.add(new Game(client.setLobbySize()));
            }
            game = startingGames.get(gameIndex);
        }
        Player player = new Player(username, game);
        game.addPlayer(player);
        if (game.isFull()){
            game.startGame();
            activeGames.add(game);
            startingGames.remove(game);
        }
    }

    public boolean usernameTaken(String username){
        return client.containsValue(username);
    }
}
