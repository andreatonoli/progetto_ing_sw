package network.messages;

import network.server.Server;
import view.Ui;

public class LoginResponseMessage extends Message{
    private Ui view;
    public LoginResponseMessage(String username, Ui view){
        super(MessageType.LOGIN_RESPONSE, username);
        this.view = view;
    }
    public Ui getView(){
        return view;
    }
    @Override
    public String toString(){
        return "LoginResponse{" +
                "sender = " + getSender() +
                '}';
    }
}
