package it.polimi.ingsw.network.messages;

/**
 * Message used to notify the server that the player wants to be removed from the server.
 */
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
