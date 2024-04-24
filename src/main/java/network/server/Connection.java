package network.server;

import Controller.Controller;
import network.messages.Message;
import model.card.Card;
import observer.Observer;

import java.util.List;

public abstract class Connection implements Observer {
    private boolean isConnected;
    public boolean getConnectionStatus(){
        return isConnected;
    }
    public void setConnectionStatus(boolean status){
        this.isConnected = status;
    }
    public abstract void setLobby(Controller controller);
    public abstract void sendMessage(Message message);
    public abstract void joinGame(List<Controller> startingGames);
    public abstract void createGame();
    public abstract String getUsername();
}
