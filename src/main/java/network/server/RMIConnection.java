package network.server;

import Controller.Controller;
import Controller.*;
import model.Game;
import network.client.RMIClientHandler;
import network.messages.Message;

import java.rmi.RemoteException;
import java.util.List;

public class RMIConnection extends Connection {
    private RMIClientHandler client;
    private final ServerController controller;
    private Server server;
    private String username;
    public RMIConnection(Server server, RMIClientHandler client, String username, ServerController controller){
        this.client = client;
        this.server = server;
        this.controller = controller;
        this.username = username;
        this.setConnectionStatus(true);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            this.client.getMessage(message);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + "RMIConnection/sendMessage");
        }
    }

    @Override
    public void joinGame(List<Controller> startingGames){
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
