package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class CatchPingMessage extends Message{
    public CatchPingMessage(String username) {
        super(MessageType.CATCH_PING, username);
    }

    @Override
    public String toString(){
        return "CatchPingMessage{" +
                "sender: " + getSender() +
                '}';
    }
}
