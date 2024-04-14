package network.server;

import model.Game;
import network.client.RMIClientHandler;
import network.messages.Message;

import java.util.List;

public class RMIConnection extends Connection {
    RMIClientHandler client;
    Server server;
    public RMIConnection(Server server, RMIClientHandler client){
        this.client = client;
        this.server = server;
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public int joinGame(List<Game> startingGames) {
        return this.client.joinGame(startingGames);
    }
}
