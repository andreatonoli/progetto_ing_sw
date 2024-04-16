package Controller;

import model.Game;
import model.Player;
import network.server.Server;

public class ServerController {

    private Server server;
    public ServerController(Server server){
        this.server = server;
    }
    public Game createLobby(String username, int numPlayers){
        Game game = new Game(numPlayers);
        Player player = new Player(username, game);
        game.addPlayer(player);
        return game;
    }
    public boolean joinLobby(String username, Game game){
        Player player = new Player(username, game);
        game.addPlayer(player);
        if (game.isFull()){
            game.startGame();
            return true;
        }
        return false;
    }
}
