package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.Game;
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
    private final transient RMIServer serverHandler;
    private String username;
    private Timer catchPing;
    private Timer ping;
    private boolean firstTime = true;
    private boolean disconnected = false;

    public RMIConnection(Server server, RMIClientHandler client, RMIServer serverHandler){
        this.client = client;
        this.server = server;
        this.serverHandler = serverHandler;
        this.setConnectionStatus(true);
    }

    public void ping(){
        ping = new Timer();
        catchPing = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                pingClient();
            }
        }, 0, 4000);
        if (firstTime) {
            firstTime = false;
            catchPing.schedule(new TimerTask() {
                @Override
                public void run() {
                    cancelPing();
                    onDisconnect();
                }
            }, 5000, 5000);
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
        catchPing.cancel();
        catchPing = new Timer();
        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                cancelPing();
                onDisconnect();
            }
        }, 5000, 5000);
    }

    public void cancelPing(){
        ping.cancel();
        catchPing.cancel();
    }

    //TODO: potrebbe conflittare con le operazioni del controller
    public void onDisconnect(){
        Game game = lobby.getGame();
        if (game.getGameState().equals(GameState.WAIT_PLAYERS)){
            server.removePlayers(username);
            game.removePlayer(username);
            if (game.getPlayers().isEmpty()){
                server.removeStartingGame(lobby);
            }
            else {
                lobby.getConnectedPlayersMessage();
            }
        }
        else if (!game.getGameState().equals(GameState.END)) {
            this.disconnected = true;
            game.getPlayerByUsername(username).setDisconnected(true);
            setConnectionStatus(false);
            server.addDisconnectedPlayer(username,lobby);
            if (game.getPlayerInTurn().getUsername().equals(username)){
                lobby.disconnectedWhileInTurn(username);
            }
        }
    }

    @Override
    public void reconnect(Connection oldConnection) {
        this.lobby = oldConnection.getLobby();
        this.lobby.addAction(new ActionMessage(this, () -> lobby.reconnectBackup(this/*, oldConnection*/)));
        this.disconnected = false;
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
        if (!this.disconnected) {
            try {
                this.client.update(message);
            } catch (RemoteException e) {
                System.err.println(e.getMessage() + "RMIConnection/sendMessage");
            }
        }
    }

    @Override
    public void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        try{
            int response = this.client.joinGame(startingGamesId, gamesWhitDisconnectionsId);
            if (response == -1){
                this.createGame();
            }
            else if (startingGamesId.contains(response)){
                this.username = client.askUsername();
                if (server.usernameTaken(username)){
                    this.joinGame(startingGamesId, gamesWhitDisconnectionsId);
                }
                else{
                    serverHandler.setEntry(username, this);
                    server.setClient(this, username);
                    server.startPing(this);
                    this.server.joinLobby(this.username, response);
                }
            }
            else if (gamesWhitDisconnectionsId.contains(response)){
                this.username = client.askUsername(response);
                if (!server.userNotDisconnected(username, response)){
                    this.joinGame(startingGamesId, gamesWhitDisconnectionsId);
                }
                else{
                    serverHandler.setEntry(username, this);
                    server.startPing(this);
                    this.server.reconnectPlayer(this, username);
                }
            }
        } catch (RemoteException e){
            System.err.println(e.getMessage() + " in RMIConnection/joinGame");
        }
    }

    @Override
    public void createGame(){
        try {
            this.username = client.askUsername();
            serverHandler.setEntry(username, this);
            server.setClient(this, username);
            server.startPing(this);
            this.server.createLobby(this.username, this.client.setLobbySize());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        server.setClient(this, username);
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
    public void removeFromServer(boolean last) {
        cancelPing();
        onDisconnect();
        System.err.println(username + " got disconnected");
        server.removePlayers(username);
        if (last){
            server.removeGame(lobby);
        }
        try {
            serverHandler.removeConnections(username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Message message) {
        this.sendMessage(message);
    }
}
