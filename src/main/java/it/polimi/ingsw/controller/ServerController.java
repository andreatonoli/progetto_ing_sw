package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.network.server.Server;

public class ServerController {
    private final transient Server server;
    public ServerController(Server server){
        this.server = server;
    }
    public Controller createLobby(String username, int numPlayers, int id){
        Controller controller = new Controller(numPlayers, id);
        joinLobby(username, controller);
        return controller;
    }
    public boolean joinLobby(String username, Controller controller){
        Connection user = this.server.getClientFromName(username);
        return controller.joinGame(user);
    }
}
