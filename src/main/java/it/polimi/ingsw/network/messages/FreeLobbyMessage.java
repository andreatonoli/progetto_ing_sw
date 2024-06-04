package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

import java.util.List;

public class FreeLobbyMessage extends Message{
    /**
     * number of not started games
     */
    private final List<Integer> startingGamesId;
    private final List<Integer> gamesWhitDisconnectionsId;
    public FreeLobbyMessage(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
        super(MessageType.FREE_LOBBY, Server.serverName);
        this.startingGamesId = startingGamesId;
        this.gamesWhitDisconnectionsId = gamesWhitDisconnectionsId;
    }

    public List<Integer> getstartingGamesId() {
        return startingGamesId;
    }

    public List<Integer> getgamesWhitDisconnectionsId() {
        return gamesWhitDisconnectionsId;
    }

    @Override
    public String toString(){
        return "FreeLobbyMessage{" +
                "startingGamesId: " + startingGamesId +
                "gamesWhitDisconnectionsId: " + gamesWhitDisconnectionsId +
                '}';
    }
}
