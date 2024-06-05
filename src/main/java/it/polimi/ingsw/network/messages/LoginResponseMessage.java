package it.polimi.ingsw.network.messages;

public class LoginResponseMessage extends Message{
    public LoginResponseMessage(){
        super(MessageType.LOGIN_RESPONSE, null);
    }
    @Override
    public String toString(){
        return "LoginResponse{" +
                "sender = " + null +
                '}';
    }
}
