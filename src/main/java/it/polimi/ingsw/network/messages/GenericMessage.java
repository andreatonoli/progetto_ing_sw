package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to send a generic message to the client.
 */
public class GenericMessage extends Message{
    private final String text;
    public GenericMessage(String text){
        super(MessageType.GENERIC_MESSAGE, Server.serverName);
        this.text = text;
    }
    public String toString(){
        return text;
    }
}
