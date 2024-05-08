package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.network.server.Server;

public class DisconnectedPlayerMessage extends Message {
    private final String username;
    private final Controller game;
    public DisconnectedPlayerMessage(String username, Controller game){
        super(MessageType.DISCONNECTION, Server.serverName);
        this.username = username;
        this.game = game;
    }
    @Override
    public String toString(){
        return "DisconnectedPlayerMessage{" +
                "game = " + this.game +
                ", username = " + this.username +
                '}';
    }
}
