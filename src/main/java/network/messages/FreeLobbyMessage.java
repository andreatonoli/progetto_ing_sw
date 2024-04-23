package network.messages;

import model.Game;
import network.server.Server;
import view.Ui;
import Controller.*;

import java.util.List;

public class FreeLobbyMessage extends Message{
    private List<Controller> startingGames;
    public FreeLobbyMessage(List<Controller> startingGames) {
        super(MessageType.FREE_LOBBY, Server.serverName);
        this.startingGames = startingGames;
    }

    public List<Controller> getStartingGames() {
        return startingGames;
    }

    @Override
    public String toString(){
        return "FreeLobbyMessage{" +
                "startingGames" + startingGames +
                '}';
    }
}
