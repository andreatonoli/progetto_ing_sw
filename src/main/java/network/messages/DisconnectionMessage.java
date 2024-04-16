package network.messages;

public class DisconnectionMessage extends Message{
    public DisconnectionMessage(String username){
        super(MessageType.GENERIC_MESSAGE, username);
    }
    @Override
    public String toString(){
        return "Player " + this.getSender() + "disconnected";
    }
}
