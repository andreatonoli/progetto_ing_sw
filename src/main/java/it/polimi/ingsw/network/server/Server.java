package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;

import java.util.*;

public class Server {
    public static final int MAX_PLAYERS_PER_LOBBY = 4; /** sets max number of players */
    public static final int MIN_PLAYERS_PER_LOBBY = 2; /** sets min number of players */
    public final static String serverName = "GameServer";
    public final static int rmiPort = 1234;
    public final static int socketPort = 1235;
    private List<Integer> idTaken;
    private List<Controller> activeGames;
    private List<Controller> startingGames;
    private final Map<String, Connection> client;
    private final ServerController controller;
    private List<String> disconnectedPlayers;
    public Server(){
        this.controller = new ServerController(this);
        this.client = Collections.synchronizedMap(new HashMap<>());
        this.activeGames = Collections.synchronizedList(new ArrayList<>());
        this.startingGames = Collections.synchronizedList(new ArrayList<>());
        this.idTaken = Collections.synchronizedList(new ArrayList<Integer>());
        this.disconnectedPlayers = new ArrayList<>();
        //Starts the RMI server and the socket server
        startServer();
    }

    public void startServer(){
        new RMIServer(this, rmiPort);
        new SocketServer(this, socketPort);
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
        int gameId = 0;
        boolean founded = false;
        if (idTaken.isEmpty()){
            idTaken.add(1);
        }
        else {
            for (int i=0; i<idTaken.size(); i++){
                if (idTaken.get(i) == -1){
                    founded = true;
                    idTaken.set(i,1);
                    gameId = i;
                    break;
                }
            }
            if (!founded){
                gameId = idTaken.size();
                idTaken.add(1);
            }
        }
        this.startingGames.add(controller.createLobby(username, numPlayers, gameId));
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

    public void addDisconnectedPlayer(String username){
        disconnectedPlayers.add(username);
    }
    //TODO finire
    //TODO: riscrivere meglio
    //TODO: per ogni nuovo game salvarsi il suo id e usarlo per riconnettersi. se no bisogna cercare l'username per tutti i game iniziati
    public void reconnectPlayer(Connection client, String username){
        Connection oldConnection = this.client.replace(username, client);
        this.disconnectedPlayers.remove(username);
        client.reconnect(oldConnection);
    }

    public void removePlayers(String username){
        this.disconnectedPlayers.remove(username);
        this.client.remove(username);
    }
    public void removeGame(Controller game){
        this.activeGames.remove(game);
        idTaken.set(game.getId(),-1);
    }
}
