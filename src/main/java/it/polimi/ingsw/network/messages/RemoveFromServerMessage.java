package it.polimi.ingsw.network.messages;

public class RemoveFromServerMessage extends Message {
    public RemoveFromServerMessage(String username){
        super(MessageType.REMOVE_FROM_SERVER, username);
    }
    @Override
    public String toString(){
        return "RemoveFromServer{" +
                "sender = " + getSender() +
                '}';
    }
}
