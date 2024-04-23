package network.messages;

public class NumPlayerResponseMessage extends Message{
    private int size;
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
