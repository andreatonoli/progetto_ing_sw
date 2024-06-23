package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to send a ping to the client.
 */
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
