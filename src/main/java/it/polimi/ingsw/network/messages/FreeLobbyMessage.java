package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;
import Controller.*;

public class FreeLobbyMessage extends Message{
    /**
     * number of not started games
     */
    private final int lobbyNumber;
    public FreeLobbyMessage(int lobbyNumber) {
        super(MessageType.FREE_LOBBY, Server.serverName);
        this.lobbyNumber = lobbyNumber;
    }

    public int getLobbyNumber() {
        return lobbyNumber;
    }

    @Override
    public String toString(){
        return "FreeLobbyMessage{" +
                "lobbyNumber: " + lobbyNumber +
                '}';
    }
}
