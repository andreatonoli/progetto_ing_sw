package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * This class is the abstract class for all the messages that are exchanged between the server and the client.
 * It contains the type of the message and the sender of the message.
 */
public abstract class Message implements Serializable {
    private final MessageType type;
    private final String sender;
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
