package network.messages;
//TODO: completa il messaggio
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
