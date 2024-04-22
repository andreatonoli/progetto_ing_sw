package Controller;

import network.server.Server;

public class ServerController {
    private transient Server server;
    public ServerController(Server server){
        this.server = server;
    }
    public Controller createLobby(String username, int numPlayers){
        Controller controller = new Controller(numPlayers, this.server);
        joinLobby(username, controller);
        return controller;
    }
    public boolean joinLobby(String username, Controller controller){
        return controller.joinGame(username);
    }
}
