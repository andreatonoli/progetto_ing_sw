package network.messages;

import network.server.Server;

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
