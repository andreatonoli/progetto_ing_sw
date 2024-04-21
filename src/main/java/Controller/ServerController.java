package Controller;

import model.Game;
import model.Player;
import network.messages.GenericMessage;
import network.server.Connection;
import network.server.Server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerController {
    //private Map<Player, Connection> obs; //TODO: cambia nome
    private transient Server server;
    public ServerController(Server server){
        this.server = server;
        //this.obs = Collections.synchronizedMap(new HashMap<>());
    }
    public Controller createLobby(String username, int numPlayers){
        Controller controller = new Controller(numPlayers, this);
        joinLobby(username, controller);
        return controller;
    }
    public boolean joinLobby(String username, Controller controller){
        return controller.joinGame(username);
    }
    public Server getServer(){
        return this.server;
    }
}
