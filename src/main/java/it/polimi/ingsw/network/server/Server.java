package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.GenericMessage;

import java.util.*;

public class Server {
    public static final int MAX_PLAYERS_PER_LOBBY = 4; /** sets max number of players */
    public static final int MIN_PLAYERS_PER_LOBBY = 2; /** sets min number of players */
    public final static String serverName = "GameServer";
    public final static int rmiPort = 50678;
    public final static int socketPort = 50679;
    private final List<Integer> idTaken;
    private final List<Controller> activeGames;
    private final List<Controller> gamesWithDisconnections;
    private final List<Integer> gamesWithDisconnectionsId;
    private final List<Controller> startingGames;
    private final List<Integer> startingGamesId;
    private final Map<String, Connection> client;
    private final ServerController controller;
    private final List<String> disconnectedPlayers;
    public Server(){
        this.controller = new ServerController(this);
        this.client = Collections.synchronizedMap(new HashMap<>());
        this.gamesWithDisconnections = Collections.synchronizedList(new ArrayList<>());
        this.activeGames = Collections.synchronizedList(new ArrayList<>());
        this.startingGames = Collections.synchronizedList(new ArrayList<>());
        this.gamesWithDisconnectionsId = Collections.synchronizedList(new ArrayList<>());
        this.startingGamesId = Collections.synchronizedList(new ArrayList<>());
        this.idTaken = Collections.synchronizedList(new ArrayList<>());
        this.disconnectedPlayers = new ArrayList<>();
        //Starts the RMI server and the socket server
        startServer();
    }

    public void startServer(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert remote IP (leave empty for localhost)");
        String ip = scanner.nextLine();
        if (ip.isEmpty()){
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        } else {
            System.setProperty("java.rmi.server.hostname", ip);
        }
        new RMIServer(this, rmiPort);
        new SocketServer(this, socketPort);
    }

    public void login(Connection client) {
        if (this.startingGames.isEmpty() && this.gamesWithDisconnections.isEmpty()) {
            client.createGame();
        } else {
            client.joinGame(startingGamesId, gamesWithDisconnectionsId);
        }
    }

    public void startPing(Connection client){
        new Thread(client::ping).start();
        System.err.println("user " + client.getUsername() + " connected");
    }

    public void setClient(Connection c, String u){
        this.client.put(u, c);
    }

    public boolean usernameTaken(String username){
        if (username.isEmpty()){
            return true;
        }
        return client.containsKey(username);
    }

    public boolean userNotDisconnected(String username, int gameId){
        boolean check1 = false;
        for (Controller game : gamesWithDisconnections){
            if (game.getId() == gameId){
                for (Player p : game.getGame().getPlayers()){
                    if (p.getUsername().equals(username)) {
                        check1 = true;
                        break;
                    }
                }
            }
        }
        boolean check2 = disconnectedPlayers.contains(username);
        return check1 && check2;
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
        this.startingGamesId.add(gameId);
    }

    public void joinLobby(String username, int indexGame){
        try {
            Controller controller = this.startingGames.get(indexGame);
            boolean full = this.controller.joinLobby(username, controller);
            if (full) {
                this.activeGames.add(controller);
                this.startingGames.remove(controller);
                this.startingGamesId.remove((Integer) controller.getId());
            }
        } catch (Exception e){
            client.get(username).sendMessage(new GenericMessage("an error as occurred, please try again."));
            login(client.get(username));
            client.remove(username);
        }
    }

    public Connection getClientFromName(String name){
        return this.client.get(name);
    }

    public void addDisconnectedPlayer(String username, Controller game){
        boolean alreadyWithDisconnections = false;
        disconnectedPlayers.add(username);
        for (Controller g : gamesWithDisconnections){
            if (g.equals(game)) {
                alreadyWithDisconnections = true;
                break;
            }
        }
        if (!alreadyWithDisconnections) {
            gamesWithDisconnections.add(game);
            gamesWithDisconnectionsId.add(game.getId());
        }
    }

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
        this.gamesWithDisconnections.remove(game);
        this.gamesWithDisconnectionsId.remove((Integer) game.getId());
        idTaken.set(game.getId(),-1);
        System.out.println("the game [" + game.getId() + "] ended");
    }
    public void removeStartingGame(Controller game){
        this.startingGames.remove(game);
        this.startingGamesId.remove((Integer) game.getId());
        idTaken.set(game.getId(),-1);
        System.out.println("the game [" + game.getId() + "] got cancelled due to lack of players");
    }
}
