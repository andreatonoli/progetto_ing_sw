package network.messages;

import model.Game;
import network.server.Server;

import java.util.List;

public class FreeLobbyMessage extends Message{
    private List<Game> startingGames;
    public FreeLobbyMessage(List<Game> startingGames) {
        super(MessageType.FREE_LOBBY, Server.serverName);
        this.startingGames = startingGames;
    }
    @Override
    public String toString(){
        return "FreeLobbyMessage{" +
                "startingGames" + startingGames +
                '}';
    }
}
