package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class NumPlayerRequestMessage extends Message{
    public NumPlayerRequestMessage(){
        super(MessageType.NUM_PLAYER_REQUEST, Server.serverName);
    }
    @Override
    public String toString(){
        return "NumPlayerRequestMessage{" +
                "sender: " + getSender() +
                '}';
    }
}
