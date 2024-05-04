package network.messages;

import Controller.Controller;
import network.server.Server;

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
