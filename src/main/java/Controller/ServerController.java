package Controller;

import model.Game;
import model.Player;
import network.server.Server;

public class ServerController {

    private Server server;
    public ServerController(Server server){
        this.server = server;
    }
    public Controller createLobby(String username, int numPlayers){
        Controller controller = new Controller(numPlayers);
        Game game = controller.getGame();
        Player player = new Player(username, game);
        game.addPlayer(player);
        return controller;
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
