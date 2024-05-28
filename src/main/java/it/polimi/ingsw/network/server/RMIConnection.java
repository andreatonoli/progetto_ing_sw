package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.client.RMIClientHandler;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RMIConnection extends Connection {
    private final RMIClientHandler client;
    private Controller lobby;
    private final transient Server server;
    private final String username;
    private Timer catchPing;
    private Timer ping;
    private boolean firstTime = true;

    public RMIConnection(Server server, RMIClientHandler client, String username){
        this.client = client;
        this.server = server;
        this.username = username;
        this.setConnectionStatus(true);
    }

    public void ping(){
        ping = new Timer();
        catchPing = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!lobby.getGame().getGameState().equals(GameState.END)){
                    pingClient();
                }
                else {
                    cancelPing();
                }
            }
        }, 0, 5000);
        if (firstTime) {
            firstTime = false;
            catchPing.schedule(new TimerTask() {
                @Override
                public void run() {
                    cancelPing();
                    onDisconnect();
                }
            }, 8000, 8000);
        }
    }

    private void pingClient(){
        try {
            client.pingNetwork();
        } catch (RemoteException e) {
            System.err.println(username + " got disconnected");
        }
    }

    public void catchPing(){
        if (!lobby.getGame().getGameState().equals(GameState.END)){
            catchPing.cancel();
            catchPing = new Timer();
            catchPing.schedule(new TimerTask() {
                @Override
                public void run() {
                    cancelPing();
                    onDisconnect();
                }
            }, 8000, 8000);
        }
        else{
            cancelPing();
        }
    }

    public void cancelPing(){
        ping.cancel();
        catchPing.cancel();
    }

    public void onDisconnect(){
        lobby.getGame().getPlayerByUsername(username).setDisconnected(true);
        setConnectionStatus(false);
        server.addDisconnectedPlayer(username);
    }

    //TODO è da mettere in coda??
    @Override
    public void reconnect(Connection oldConnection) {
        this.lobby = oldConnection.getLobby();
        this.lobby.addAction(new ActionMessage(this, () -> lobby.reconnectBackup(this/*, oldConnection*/)));
    }

    @Override
    public void setLobby(Controller controller) {
        this.lobby = controller;
    }

    @Override
    public Controller getLobby(){
        return this.lobby;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            this.client.update(message);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + "RMIConnection/sendMessage");
        }
    }

    @Override
    public void joinGame(List<Controller> startingGames){
        try{
            int response = this.client.joinGame(startingGames.size());
            if (response == startingGames.size()){
                this.server.createLobby(this.username, this.client.setLobbySize());
            }
            else{
                this.server.joinLobby(this.username, response);
            }
        } catch (RemoteException e){
            System.err.println(e.getMessage() + " in RMIConnection/joinGame");
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
    @Override
    public String getUsername(){
        return this.username;
    }

    //TODO: sostituire invocazioni del controller con aggiunta in coda
    public void placeStarterCard(Card card) {
        this.lobby.addAction(new ActionMessage(this, () -> lobby.placeStarterCard(this, card)));
    }
    public void setAchievement(Achievement achievement){
        this.lobby.addAction(new ActionMessage(this,() -> lobby.chooseObj(this, achievement)));
    }
    public void setColor(Color color){
        this.lobby.addAction(new ActionMessage(this, () -> this.lobby.setColor(this, color)));
    }
    public void placeCard(Card card, int[] placingCoordinates){
        this.lobby.addAction(new ActionMessage(this, () -> lobby.placeCard(this, card, placingCoordinates)));
    }
    public void drawCard(String chosenDeck){
        this.lobby.addAction(new ActionMessage(this, () -> lobby.drawCard(this, chosenDeck)));
    }
    public void drawCardFromBoard(int index){
        this.lobby.addAction(new ActionMessage(this, () -> lobby.drawCardFromBoard(this, index)));

    }
    @Override
    public void sendChatMessage(String message, Connection receiver) {
        this.lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, receiver, message)));
    }
    @Override
    public void sendChatMessage(String message) {
        this.lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, message)));
    }
    @Override
    public void update(Message message) {
        this.sendMessage(message);
    }
}
