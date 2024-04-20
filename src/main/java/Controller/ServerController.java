package Controller;

import model.Game;
import model.Player;
import network.server.Connection;
import network.server.Server;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerController implements Serializable {
    private Map<Player, Connection> obs; //TODO: cambia nome
    private Server server;
    public ServerController(Server server){
        this.server = server;
        this.obs = Collections.synchronizedMap(new HashMap<>());
    }
    public Controller createLobby(String username, int numPlayers){
        Controller controller = new Controller(numPlayers);
        joinLobby(username, controller);
        return controller;
    }
    public boolean joinLobby(String username, Controller controller){
        Game game = controller.getGame();
        Player player = new Player(username, game);
        game.addPlayer(player);
        if (game.isFull()){
            game.startGame();
            return true;
        }
        return false;
    }
}
