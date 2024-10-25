package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

import java.util.List;

/**
 * Message used to send the available lobbies to the client.
 * It contains the id of the games that are not started yet and the id of the games that have disconnections.
 */
public class AvailableLobbiesMessage extends Message{

    /**
     * Number of not started games.
     */
    private final List<Integer> startingGamesId;

    /**
     * Number of games with disconnections.
     */
    private final List<Integer> gamesWhitDisconnectionsId;

    public AvailableLobbiesMessage(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
        super(MessageType.AVAILABLE_LOBBY, Server.serverName);
        this.startingGamesId = startingGamesId;
        this.gamesWhitDisconnectionsId = gamesWhitDisconnectionsId;
    }

    public List<Integer> getStartingGamesId() {
        return startingGamesId;
    }

    public List<Integer> getGamesWhitDisconnectionsId() {
        return gamesWhitDisconnectionsId;
    }

    @Override
    public String toString(){
        return "AvailableLobbiesMessage{" +
                "startingGamesId: " + startingGamesId +
                "gamesWhitDisconnectionsId: " + gamesWhitDisconnectionsId +
                '}';
    }
}
