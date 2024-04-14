package network.server;

import model.Game;
import network.messages.Message;

import java.util.List;

public abstract class Connection {
    public abstract void sendMessage(Message message);
    public abstract int joinGame(List<Game> startingGames);
    public abstract int setLobbySize();
}
