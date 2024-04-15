package network.messages;

import network.server.Server;

public class LobbyIndexMessage extends Message{
    private int choice;
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
