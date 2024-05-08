package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class GenericMessage extends Message{
    String text;
    public GenericMessage(String text){
        super(MessageType.GENERIC_MESSAGE, Server.serverName);
        this.text = text;
    }
    public String toString(){
        return text;
    }
}
