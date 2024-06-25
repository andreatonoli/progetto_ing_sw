package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exceptions.FullLobbyExeption;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.network.server.Server;

public class ServerController {

    /**
     * Reference to the server.
     */
    private final transient Server server;

    /**
     * Lock used to synchronize the creation of a new game.
     */
    private final Object gameCreationLock = new Object();

    /**
     * Constructor of the class.
     * @param server is the reference to the server.
     */
    public ServerController(Server server){
        this.server = server;
    }

    /**
     * Method used to create a new lobby.
     * @param username is the name of the player that creates the lobby.
     * @param numPlayers is the number of players that will play the game.
     * @param id is the id of the game.
     * @return the controller of the game.
     */
    public Controller createLobby(String username, int numPlayers, int id){
        synchronized (gameCreationLock) {
            Controller controller = new Controller(numPlayers, id);
            joinLobby(username, controller);
            return controller;
        }
    }

    /**
     * Method used to join a lobby.
     * @param username is the name of the player that joins the lobby.
     * @param controller is the controller of the game.
     * @return true if the game will become full, false otherwise.
     */
    public synchronized boolean joinLobby(String username, Controller controller){
        Connection user = this.server.getClientFromName(username);
        boolean check = false;
        try {
            check = controller.joinGame(user);
        } catch (FullLobbyExeption e) {
            server.removePlayers(username);
            user.cancelPing();
            server.login(user);
        }
        return check;
    }
}
