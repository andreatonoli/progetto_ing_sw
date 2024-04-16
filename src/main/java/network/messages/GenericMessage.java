package network.messages;

public class GenericMessage extends Message{
    String text;
    public GenericMessage(String sender, String text){
        super(MessageType.GENERIC_MESSAGE, sender);
        this.text = text;
    }
    public String toString(){
        return text;
    }
}
