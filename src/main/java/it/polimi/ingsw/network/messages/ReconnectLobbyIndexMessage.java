package it.polimi.ingsw.network.messages;

import java.util.List;

/**
 * Message used to request the server to reconnect to a specific lobby.
 */
public class ReconnectLobbyIndexMessage extends Message {
    private final int choice;
    private final List<Integer> startingGamesId;
    private final List<Integer> gamesWithDisconnectionsId;
    public ReconnectLobbyIndexMessage(String username, int choice, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        super(MessageType.RECONNECT_LOBBY_INDEX, username);
        this.choice = choice;
        this.startingGamesId = startingGamesId;
        this.gamesWithDisconnectionsId = gamesWithDisconnectionsId;
    }
    public int getChoice(){
        return choice;
    }

    public List<Integer> getStartingGamesId() {
        return startingGamesId;
    }

    public List<Integer> getGamesWithDisconnectionsId() {
        return gamesWithDisconnectionsId;
    }

    @Override
    public String toString(){
        return "VerifyUsername{" +
                "choice: " + choice +
                "sender = " + getSender() +
                "startingGamesId: " + startingGamesId +
                "gamesWithDisconnectionsId: " + gamesWithDisconnectionsId +
                '}';
    }

}
