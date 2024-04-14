package network.server;

import model.Game;
import network.client.RMIClientHandler;
import network.messages.Message;

import java.rmi.RemoteException;
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
    public int joinGame(List<Game> startingGames){
        try{
            return this.client.joinGame(startingGames);
        } catch (RemoteException e){
            System.err.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public int setLobbySize(){
        try {
            return this.client.setLobbySize();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }

}
