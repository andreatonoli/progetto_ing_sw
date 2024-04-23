package network.messages;

public class ErrorMessage extends Message{
    private String text;
    public ErrorMessage(String username, String text){
        super(MessageType.GENERIC_MESSAGE, username);
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
