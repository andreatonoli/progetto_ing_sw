package it.polimi.ingsw.network.messages;

public class AddToChatMessage extends Message{
    private final String message;
    private final String receiver;
    public AddToChatMessage(String sender, String receiver, String message) {
        super(MessageType.ADD_TO_CHAT, sender);
        this.message = message;
        this.receiver = receiver;
    }
    public AddToChatMessage(String sender, String message) {
        super(MessageType.ADD_TO_CHAT, sender);
        this.message = message;
        this.receiver = null;
    }
    public String getMessage(){
        return message;
    }
    public String getReceiver(){
        return receiver;
    }
    @Override
    public String toString(){
        return "ChatMessage{" +
                "message: " + message +
                "receiver: " + receiver +
                '}';
    }
}
