package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class PingMessage extends Message{
    public PingMessage() {
        super(MessageType.PING, Server.serverName);
    }

    @Override
    public String toString(){
        return "PingMessage{" +
                "sender = " + getSender() +
                '}';
    }
}
