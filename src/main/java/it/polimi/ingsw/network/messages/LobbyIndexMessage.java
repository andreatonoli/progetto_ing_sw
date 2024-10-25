package it.polimi.ingsw.network.messages;

/**
 * Message used to send the index of the lobby chosen by the player.
 */
public class LobbyIndexMessage extends Message{
    private final int choice;
    public LobbyIndexMessage(String username, int choice){
        super(MessageType.LOBBY_INDEX, username);
        this.choice = choice;
    }
    public int getChoice(){
        return choice;
    }


    @Override
    public String toString(){
        return "LobbyIndexMessage{" +
                "choice: " + choice +
                "sender: " + getSender() +
                '}';
    }
}
