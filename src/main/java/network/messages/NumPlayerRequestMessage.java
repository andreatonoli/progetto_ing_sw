package network.messages;

import network.server.Server;

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
