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

/**
 * This class represents the connection between the server and the client.
 * It extends the Connection class and is used by the RMI server.
 */
public class RMIConnection extends Connection {

    /**
     * This attribute is the client handler of the connection.
     */
    private final RMIClientHandler client;

    /**
     * This attribute is the id of the connection.
     */
    private final Integer id;

    /**
     * This attribute is the lobby of the connection.
     */
    private Controller lobby;

    /**
     * This attribute is the server of the connection.
     */
    private final transient Server server;

    /**
     * Link to the RMI server who created the connection and handles all the action coming from the client.
     */
    private final transient RMIServer serverHandler;

    /**
     * This attribute is the username of the connection.
     */
    private String username;

    /**
     * This attribute is the timer that catches the ping.
     */
    private Timer catchPing;

    /**
     * This attribute is the timer that sends the ping.
     */
    private Timer ping;

    /**
     * This attribute is true if it is the first time that the connection is pinging.
     */
    private boolean firstTime = true;

    /**
     * This attribute is true if the connection is disconnected.
     */
    private boolean disconnected = false;

    /**
     * This constructor creates a connection with the server, the client handler, the server handler and the id.
     * @param server is the server of the connection.
     * @param client is the client handler of the connection.
     * @param serverHandler is the server handler of the connection.
     * @param id is the id of the connection.
     */
    public RMIConnection(Server server, RMIClientHandler client, RMIServer serverHandler, Integer id){
        this.client = client;
        this.server = server;
        this.serverHandler = serverHandler;
        this.id = id;
        this.setClientId();
    }

    /**
     * This method sets the client id.
     */
    public void setClientId(){
        try {
            client.setClientId(id);
        } catch (RemoteException e){
            System.err.println("error in RMIConnection/setClientId");
        }
    }

    /**
     * This method starts the timer that pings the client.
     */
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

    /**
     * This method pings the client.
     */
    private void pingClient(){
        try {
            client.pingNetwork();
        } catch (RemoteException e) {
            System.err.println(username + " got disconnected");
        }
    }

    /**
     * This method catches the ping.
     */
    public void catchPing(){
        catchPing.cancel();
        catchPing = new Timer();
        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                onDisconnect();
                cancelPing();
            }
        }, 5000, 5000);
    }

    /**
     * This method cancels the ping.
     */
    public void cancelPing(){
        ping.cancel();
        catchPing.cancel();
    }

    /**
     * This method is called when the client disconnects. Handles the disconnection server side.
     */
    public void onDisconnect(){
        cancelPing();
        Game game = lobby.getGame();
        if (game.getGameState().equals(GameState.WAIT_PLAYERS)) {
            server.removePlayers(username);
            game.removePlayer(username);
            if (game.getPlayers().isEmpty()) {
                server.removeStartingGame(lobby);
            } else {
                lobby.getConnectedPlayersMessage();
            }
        } else if (!game.getGameState().equals(GameState.END)) {
            this.disconnected = true;
            game.getPlayerByUsername(username).setDisconnected(true);
            server.addDisconnectedPlayer(username, lobby);
            if (game.getPlayerInTurn().getUsername().equals(username)) {
                lobby.disconnectedWhileInTurn(username);
            }
            if (game.getGameState().equals(GameState.START)) {
                lobby.disconnectedWhileSetupping(this, username.equals(lobby.getPlayerInTurn()));
            }
        }
    }

    /**
     * This method reconnects the connection.
     * @param oldConnection is the old connection that belonged to the user and will be replaced by the new one.
     */
    @Override
    public void reconnect(Connection oldConnection) {
        this.lobby = oldConnection.getLobby();
        this.lobby.addAction(new ActionMessage(this, () -> lobby.reconnectBackup(this)));
        this.disconnected = false;
    }

    /**
     * This method sets the lobby of the connection.
     * @param controller is the lobby of the connection.
     */
    @Override
    public void setLobby(Controller controller) {
        this.lobby = controller;
    }

    /**
     * This method returns the lobby of the connection.
     * @return the lobby of the connection.
     */
    @Override
    public Controller getLobby(){
        return this.lobby;
    }

    /**
     * This method sends a message to the client.
     * @param message is the message that has to be sent.
     */
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

    /**
     * This method asks the client whether it wants to create, join or reconnect to a lobby.
     * @param startingGamesId is the list of the id of the games that are starting.
     * @param gamesWhitDisconnectionsId is the list of the id of the games that have disconnections.
     */
    @Override
    public void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        try{
            this.client.joinGame(startingGamesId, gamesWhitDisconnectionsId);
        } catch (RemoteException e){
            System.err.println(e.getMessage() + " in RMIConnection/joinGame");
        }
    }

    /**
     * This method handles the action of creating, joining or reconnecting to a game.
     */
    public void handleAction(int response, String username, List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId){
        if (response == -2) {
            this.joinGame(startingGamesId, gamesWhitDisconnectionsId);
        }
        else if (response == -1) {
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
            if (server.userNotDisconnected(username, response)) {
                this.joinGame(startingGamesId, gamesWhitDisconnectionsId);
            } else {
                this.username = username;
                server.startPing(this);
                this.server.reconnectPlayer(this, username);
            }
        }
    }

    /**
     * This method handles the action of creating a game.
     */
    @Override
    public void createGame(){
        try {
            this.client.askLobbySize();
        } catch (RemoteException e) {
            System.out.println(e.getMessage() + " in RMIConnection/createGame");
        }
    }

    /**
     * This method sets the lobby size.
     * @param lobbySize is the size of the lobby.
     * @param username is the username of the connection.
     */
    public void setLobbySize(int lobbySize, String username){
        if (!server.usernameTaken(username)){
            server.setClient(this, username);
            server.startPing(this);
            this.server.createLobby(this.username, lobbySize);
        }
        else {
            try {
                client.askUsername();
            } catch (RemoteException e) {
                System.out.println(e.getMessage() + " in RMIConnection/setLobbySize");
            }
        }
    }

    /**
     * This method gets the username of the connection.
     * @return the username of the connection.
     */
    @Override
    public String getUsername(){
        return this.username;
    }

    /**
     * THis methods permit to place the starter card.
     * @param card is the starter card that has to be placed.
     */
    public void placeStarterCard(Card card) {
        this.lobby.addAction(new ActionMessage(this, () -> lobby.placeStarterCard(this, card)));
    }

    /**
     * This method sets the chosen achievement.
     * @param achievement is the chosen achievement.
     */
    public void setAchievement(Achievement achievement){
        this.lobby.addAction(new ActionMessage(this,() -> lobby.chooseObj(this, achievement)));
    }

    /**
     * This method sets the chosen color.
     * @param color is the chosen color.
     */
    public void setColor(Color color){
        this.lobby.addAction(new ActionMessage(this, () -> this.lobby.setColor(this, color)));
    }

    /**
     * This method place the card at the given coordinates.
     * @param card is the card that has to be placed.
     * @param placingCoordinates are the coordinates where the card has to be placed.
     */
    public void placeCard(Card card, int[] placingCoordinates){
        this.lobby.addAction(new ActionMessage(this, () -> lobby.placeCard(this, card, placingCoordinates)));
    }

    /**
     * This method draws a card from the deck.
     * @param chosenDeck is the deck from which the card has to be drawn.
     */
    public void drawCard(String chosenDeck){
        this.lobby.addAction(new ActionMessage(this, () -> lobby.drawCard(this, chosenDeck)));
    }

    /**
     * This method draws a card from the board.
     * @param index is the index of the card that has to be drawn.
     */
    public void drawCardFromBoard(int index){
        this.lobby.addAction(new ActionMessage(this, () -> lobby.drawCardFromBoard(this, index)));

    }

    /**
     * This method sends a chat message from the client to a specific player.
     * @param message is the message that has to be sent.
     * @param receiver is the connection that has to receive the message.
     */
    @Override
    public void sendChatMessage(String message, Connection receiver) {
        this.lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, receiver, message)));
    }

    /**
     * This method sends a chat message from the client.
     * @param message is the message that has to be sent.
     */
    @Override
    public void sendChatMessage(String message) {
        this.lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, message)));
    }

    /**
     * This method removes the connection from the server.
     * @param last is a boolean that is true if the connection is the last one to be removed.
     */
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

    /**
     * This method tells the client that it has to wait for someone else to finish their connection phase.
     */
    @Override
    public void waiting() {
        sendMessage(new GenericMessage("someone else is joining a game, please wait..."));
    }

    /**
     * This method updates the client with a message.
     * @param message is the message to be sent.
     */
    @Override
    public void update(Message message) {
        this.sendMessage(message);
    }

    /**
     * This method sets the nickname of the client on the connection.
     * @param nickname is the nickname of the client.
     */
    public void sendNickname(String nickname) {
        this.username = nickname;
    }
}
