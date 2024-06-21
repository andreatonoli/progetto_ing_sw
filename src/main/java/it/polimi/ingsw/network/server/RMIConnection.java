package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.network.messages.GenericMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.client.RMIClientHandler;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RMIConnection extends Connection {
    private final RMIClientHandler client;
    private Integer id;
    private Controller lobby;
    private final transient Server server;
    private final transient RMIServer serverHandler;
    private String username;
    private Timer catchPing;
    private Timer ping;
    private boolean firstTime = true;
    private boolean disconnected = false;

    public RMIConnection(Server server, RMIClientHandler client, RMIServer serverHandler, Integer id){
        this.client = client;
        this.server = server;
        this.serverHandler = serverHandler;
        this.id = id;
        this.setClientId();
        this.setConnectionStatus(true);
    }

    public void setClientId(){
        try {
            client.setClientId(id);
        } catch (RemoteException e){
            System.err.println("error in RMIConnection/setClientId");
        }
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
        cancelPing();
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
            if (game.getGameState().equals(GameState.START)){
                lobby.disconnectedWhileSetupping(this, username.equals(lobby.getPlayerInTurn()));
            }
        }
    }

    @Override
    public void reconnect(Connection oldConnection) {
        this.lobby = oldConnection.getLobby();
        this.lobby.addAction(new ActionMessage(this, () -> lobby.reconnectBackup(this)));
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

    public void setUsername(String username){
        this.username = username;
    }

    public void setId(Integer id){
        this.id = id;
        try {
            this.client.setId(id);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + " in RMIConnection/setId");
        }
    }

    @Override
    public void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        try{
            this.client.joinGame(startingGamesId, gamesWhitDisconnectionsId);
        } catch (RemoteException e){
            System.err.println(e.getMessage() + " in RMIConnection/joinGame");
        }
    }

    public void handleAction(int response, String username, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        if (response == -1) {
            this.createGame();
        } else if (startingGamesId.contains(response)) {
            if (server.usernameTaken(username)) {
                this.joinGame(startingGamesId, gamesWhitDisconnectionsId);
            } else {
                this.username = username;
                server.setClient(this, username);
                server.startPing(this);
                this.server.joinLobby(this.username, response);
            }
        } else if (gamesWhitDisconnectionsId.contains(response)) {
            if (!server.userNotDisconnected(username, response)) {
                this.joinGame(startingGamesId, gamesWhitDisconnectionsId);
            } else {
                this.username = username;
                server.startPing(this);
                this.server.reconnectPlayer(this, username);
            }
        }
    }

    @Override
    public void createGame(){
        try {
            this.client.askLobbySize();
        } catch (RemoteException e) {
            System.out.println(e.getMessage() + " in RMIConnection/createGame");
        }
    }

    public void setLobbySize(int lobbySize, String username){
        if (!server.usernameTaken(username)){
            server.setClient(this, username);
            server.startPing(this);
            this.server.createLobby(this.username, lobbySize);
            server.setClient(this, username);
        }
        else {
            try {
                client.askUsername();
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in RMIConnection/setLobbySize");
            }
        }
    }
    public void setNickname(String nickname) {
        this.username = nickname;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

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
            serverHandler.removeConnections(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waiting() {
        sendMessage(new GenericMessage("someone else is joining a game, please wait..."));
    }

    @Override
    public void update(Message message) {
        this.sendMessage(message);
    }

    public void sendNickname(String nickname) {
        this.username = nickname;
    }
}
