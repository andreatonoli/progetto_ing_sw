package it.polimi.ingsw.network.messages;

public class ReconnectLobbyIndexMessage extends Message {
    int choice;
    public ReconnectLobbyIndexMessage(String username, int choice){
        super(MessageType.RECONNECT_LOBBY_INDEX, username);
        this.choice = choice;
    }
    public int getChoice(){
        return choice;
    }
    @Override
    public String toString(){
        return "VerifyUsername{" +
                "choice: " + choice +
                "sender = " + getSender() +
                '}';
    }

}
