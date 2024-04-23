package network.messages;

import network.server.Server;

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
