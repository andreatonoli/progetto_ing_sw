package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to request the number of players of the lobby.
 */
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
