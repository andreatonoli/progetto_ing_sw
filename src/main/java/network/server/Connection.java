package network.server;

import model.Game;
import network.messages.Message;

import java.util.List;

public abstract class Connection {
    private boolean isConnected;
    public boolean getConnectionStatus(){
        return isConnected;
    }
    public void setConnectionStatus(boolean status){
        this.isConnected = status;
    }
    public abstract void sendMessage(Message message);
    public abstract void joinGame(List<Game> startingGames);
    public abstract void createGame();
}
