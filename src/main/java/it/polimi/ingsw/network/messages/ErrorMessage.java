package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to send an error message to the client.
 */
public class ErrorMessage extends Message{
    private final String text;
    public ErrorMessage(String text){
        super(MessageType.ERROR, Server.serverName);
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
