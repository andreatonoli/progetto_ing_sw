package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class UsernameRequestMessage extends Message{
    public UsernameRequestMessage() {
        super(MessageType.USERNAME_REQUEST, Server.serverName);
    }
    @Override
    public String toString(){
        return "LoginRequest{" +
                "sender = " + getSender() +
                '}';
    }
}
