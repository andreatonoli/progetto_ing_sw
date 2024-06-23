package it.polimi.ingsw.network.messages;

/**
 * Message used to notify the server that the player wants to login.
 */
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
