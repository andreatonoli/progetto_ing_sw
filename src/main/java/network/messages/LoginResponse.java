package network.messages;
//TODO: completa il messaggio
public class LoginResponse extends Message{
    public LoginResponse(String username){
        super(MessageType.LOGIN_RESPONSE, username);
    }
    @Override
    public String toString(){
        return "LoginResponse{" +
                "sender = " + getSender() +
                '}';
    }
}
