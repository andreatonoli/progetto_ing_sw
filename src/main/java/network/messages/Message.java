package network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private MessageType type;
    private String sender;
    public Message(MessageType type, String sender){
        this.type = type;
        this.sender = sender;
    }
    public MessageType getType(){
        return this.type;
    }
    public String getSender(){
        return this.sender;
    }
    @Override
    public String toString(){
        return "Message{" +
                "sender = " + this.sender +
                ", type = " + this.type +
                '}';
    }
}
