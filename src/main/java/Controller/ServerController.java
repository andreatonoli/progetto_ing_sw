package Controller;

import network.server.Connection;
import network.server.Server;

public class ServerController {
    private transient Server server;
    public ServerController(Server server){
        this.server = server;
    }
    public Controller createLobby(String username, int numPlayers){
        Controller controller = new Controller(numPlayers);
        joinLobby(username, controller);
        return controller;
    }
    public boolean joinLobby(String username, Controller controller){
        Connection user = this.server.getClientFromName(username);
        return controller.joinGame(user);
    }
}
