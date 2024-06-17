package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exceptions.FullLobbyExeption;
import it.polimi.ingsw.network.messages.FreeLobbyMessage;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.network.server.Server;

public class ServerController {
    private final transient Server server;
    private final Object gameCreationLock = new Object();
    public ServerController(Server server){
        this.server = server;
    }
    public Controller createLobby(String username, int numPlayers, int id){
        synchronized (gameCreationLock) {
            Controller controller = new Controller(numPlayers, id);
            joinLobby(username, controller);
            return controller;
        }
    }
    public synchronized boolean joinLobby(String username, Controller controller){
        Connection user = this.server.getClientFromName(username);
        boolean check = false;
        try {
            check = controller.joinGame(user);
        } catch (FullLobbyExeption e) {
            server.removePlayers(username);
            server.login(user);
        }
        return check;
    }
}
