package network.messages;

import network.server.Server;

public class ErrorMessage extends Message{
    private final String text;
    public ErrorMessage(String text){
        super(MessageType.GENERIC_MESSAGE, Server.serverName);
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
