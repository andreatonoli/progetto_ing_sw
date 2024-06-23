package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.GenericMessage;

import java.util.*;

/**
 * Server class is the main class of the server side of the application.
 * It contains all the information about the games and the players connected to the server.
 * It also contains the methods to create and join games, to reconnect players and to remove games and players.
 */
public class Server {

    /**
     * Sets max number of players per lobby.
     */
    public static final int MAX_PLAYERS_PER_LOBBY = 4;

    /**
     * Sets min number of players per lobby.
     */
    public static final int MIN_PLAYERS_PER_LOBBY = 2;

    /**
     * Sets the name of the server.
     */
    public final static String serverName = "GameServer";

    /**
     * Sets the port for the RMI server.
     */
    public final static int rmiPort = 50678;

    /**
     * Sets the port for the socket server.
     */
    public final static int socketPort = 50679;

    /**
     * List of the active lobbies' id.
     */
    private final List<Integer> idTaken;

    /**
     * List of the active games.
     */
    private final List<Controller> activeGames;

    /**
     * List of the games with one or more player disconnected.
     */
    private final List<Controller> gamesWithDisconnections;

    /**
     * List of the games' id with one or more player disconnected.
     */
    private final List<Integer> gamesWithDisconnectionsId;

    /**
     * List of the games that are waiting for players to start.
     */
    private final List<Controller> startingGames;

    /**
     * List of the games' id that are waiting for players to start.
     */
    private final List<Integer> startingGamesId;

    /**
     * Map of the players connected to the server.
     */
    private final Map<String, Connection> client;

    /**
     * Server controller.
     */
    private final ServerController controller;

    /**
     * List of the players that are disconnected from one game.
     */
    private final List<String> disconnectedPlayers;

    /**
     * Constructor of the class.
     * It initializes the server controller and the maps and lists of the games and the players.
     * Then starts the server.
     */
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

    /**
     * Starts the server.
     */
    public void startServer(){
        //TODO: controlla che per RMI sia strettamente necessaria questa cosa
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

    /**
     * Method to log in a player. Then it creates a new game if there are no games waiting for players.
     * @param client is the connection of the player that wants to log in.
     */
    public void login(Connection client) {
        if (this.startingGames.isEmpty() && this.gamesWithDisconnections.isEmpty()) {
            client.createGame();
        } else {
            client.joinGame(startingGamesId, gamesWithDisconnectionsId);
        }
    }

    /**
     * Method to start the ping of a player.
     * @param client is the connection of the player that wants to start the ping.
     */
    public void startPing(Connection client){
        new Thread(client::ping).start();
        System.err.println("user " + client.getUsername() + " connected");
    }

    /**
     * Method to store the connection of a player in the server.
     * @param c is the connection of the player.
     * @param u is the username of the player.
     */
    public void setClient(Connection c, String u){
        this.client.put(u, c);
    }

    /**
     * Method to check if a username is already taken.
     * @param username is the username to check.
     * @return true if the username is already taken, false otherwise.
     */
    public boolean usernameTaken(String username){
        if (username.isEmpty()){
            return true;
        }
        return client.containsKey(username);
    }

    /**
     * Method to check if a game contains a disconnected player with a certain username.
     * @param username is the username of the player to check.
     * @param gameId is the id of the game to check.
     * @return true if the game contains the disconnected player with that username, false otherwise.
     */
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
        return !check1 || !check2;
    }

    /**
     * Method to create a new game.
     * @param username is the username of the player that creates the game.
     * @param numPlayers is the number of players that the game will have.
     */
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

    /**
     * Method to join a game.
     * @param username is the username of the player that wants to join the game.
     * @param indexGame is the index of the game in the list of the games waiting for players that the player wants to join.
     */
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

    /**
     * Method that returns a certain client from the map of the clients.
     * @param name is the username of the client we want to take.
     * @return the connection of the client with the username passed as parameter.
     */
    public Connection getClientFromName(String name){
        return this.client.get(name);
    }

    /**
     * Add the player to the list of the disconnected players and the game to the list of the games with disconnections.
     * @param username is the username of the player that disconnected.
     * @param game is the game where the player disconnected.
     */
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

    /**
     * Method to reconnect a player to a game.
     * @param client is the connection of the player that wants to reconnect.
     * @param username is the username of the player that wants to reconnect.
     */
    public void reconnectPlayer(Connection client, String username){
        Connection oldConnection = this.client.replace(username, client);
        this.disconnectedPlayers.remove(username);
        client.reconnect(oldConnection);
    }

    /**
     * Method to remove a player from the server.
     * @param username is the username of the player that wants to be removed.
     */
    public void removePlayers(String username){
        this.disconnectedPlayers.remove(username);
        this.client.remove(username);
    }

    /**
     * Method to remove a game from the server.
     * @param game is the game that wants to be removed.
     */
    public void removeGame(Controller game){
        this.activeGames.remove(game);
        this.gamesWithDisconnections.remove(game);
        this.gamesWithDisconnectionsId.remove((Integer) game.getId());
        idTaken.set(game.getId(),-1);
        System.out.println("the game [" + game.getId() + "] ended");
    }

    /**
     * Method to remove a game from the list of the starting games.
     * @param game is the game that wants to be removed.
     */
    public void removeStartingGame(Controller game){
        this.startingGames.remove(game);
        this.startingGamesId.remove((Integer) game.getId());
        idTaken.set(game.getId(),-1);
        System.out.println("the game [" + game.getId() + "] got cancelled due to lack of players");
    }
}
