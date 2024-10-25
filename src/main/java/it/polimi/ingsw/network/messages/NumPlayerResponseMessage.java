package it.polimi.ingsw.network.messages;

/**
 * Message used to send the server the size of the lobby the player wants to create.
 */
public class NumPlayerResponseMessage extends Message{
    private final int size;
    public NumPlayerResponseMessage(String username, int size){
        super(MessageType.NUM_PLAYER_RESPONSE, username);
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
    @Override
    public String toString(){
        return "NumPlayerResponseMessage{" +
                "sender: " + getSender() +
                "size: " + this.size +
                '}';
    }
}
