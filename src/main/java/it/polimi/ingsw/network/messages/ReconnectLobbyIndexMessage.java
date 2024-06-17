package it.polimi.ingsw.network.messages;

import java.util.List;

public class ReconnectLobbyIndexMessage extends Message {
    int choice;
    private final List<Integer> startingGamesId;
    private final List<Integer> gamesWhitDisconnectionsId;
    public ReconnectLobbyIndexMessage(String username, int choice, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        super(MessageType.RECONNECT_LOBBY_INDEX, username);
        this.choice = choice;
        this.startingGamesId = startingGamesId;
        this.gamesWhitDisconnectionsId = gamesWhitDisconnectionsId;
    }
    public int getChoice(){
        return choice;
    }

    public List<Integer> getstartingGamesId() {
        return startingGamesId;
    }

    public List<Integer> getgamesWhitDisconnectionsId() {
        return gamesWhitDisconnectionsId;
    }

    @Override
    public String toString(){
        return "VerifyUsername{" +
                "choice: " + choice +
                "sender = " + getSender() +
                "startingGamesId: " + startingGamesId +
                "gamesWhitDisconnectionsId: " + gamesWhitDisconnectionsId +
                '}';
    }

}
