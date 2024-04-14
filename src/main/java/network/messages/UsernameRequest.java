package network.messages;

import network.server.Server;

public class UsernameRequest extends Message{
    public UsernameRequest() {
        super(MessageType.USERNAME_REQUEST, Server.serverName);
    }
    @Override
    public String toString(){
        return "LoginRequest{" +
                "sender = " + getSender() +
                '}';
    }
}
