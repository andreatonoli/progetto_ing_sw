package network.messages;

import network.server.Server;

public class LoginRequest extends Message{
    public LoginRequest() {
        super(MessageType.LOGIN_REQUEST, Server.serverName);
    }
    @Override
    public String toString(){
        return "LoginRequest{" +
                "sender = " + getSender() +
                '}';
    }
}
