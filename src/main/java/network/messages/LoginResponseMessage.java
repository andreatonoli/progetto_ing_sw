package network.messages;

import network.server.Server;
import view.Ui;

public class LoginResponseMessage extends Message{
    public LoginResponseMessage(String username){
        super(MessageType.LOGIN_RESPONSE, username);
    }
    @Override
    public String toString(){
        return "LoginResponse{" +
                "sender = " + getSender() +
                '}';
    }
}
