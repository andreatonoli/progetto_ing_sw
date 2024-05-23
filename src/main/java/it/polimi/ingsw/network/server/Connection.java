package it.polimi.ingsw.network.server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.observer.Observer;

import java.rmi.RemoteException;
import java.util.List;
//TODO: scrivere metodi in comune tra le connection
public abstract class Connection implements Observer {
    private boolean isConnected;
    public boolean getConnectionStatus(){
        return isConnected;
    }
    public void setConnectionStatus(boolean status){
        this.isConnected = status;
    }
    public abstract void setLobby(Controller controller);
    public abstract Controller getLobby();
    public abstract void sendMessage(Message message);
    public abstract void joinGame(List<Controller> startingGames);
    public abstract void createGame();
    public abstract String getUsername();
    public abstract void ping();
    public abstract void reconnect(Connection oldConnection);
    public abstract void sendPublicMessage(String message);
    public abstract void sendPrivateMessage(String message, Connection receiver);

}
