package network.server;

import model.Game;
import network.client.RMIClientHandler;
import network.messages.Message;

import java.rmi.RemoteException;
import java.util.List;

public class RMIConnection extends Connection {
    private RMIClientHandler client;
    private Server server;
    private String username;
    public RMIConnection(Server server, RMIClientHandler client, String username){
        this.client = client;
        this.server = server;
        this.username = username;
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void joinGame(List<Game> startingGames){
        try{
            int response = this.client.joinGame(startingGames);
            if (response == startingGames.size()){
                this.server.createLobby(this.username, this.client.setLobbySize());
            }
            else{
                this.server.joinLobby(this.username, response);
            }
        } catch (RemoteException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void createGame(){
        try {
            this.server.createLobby(this.username, this.client.setLobbySize());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

}
