package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class represents the connection between the server and the client through a socket.
 */
public class SocketConnection extends Connection implements Runnable {

    /**
     * This attribute represents the server that the connection is connected to.
     */
    private Server server;

    /**
     * This attribute represents the socket of the connection.
     */
    private Socket socket;

    /**
     * This attribute represents the input stream of the connection.
     */
    private ObjectInputStream in;

    /**
     * This attribute represents the output stream of the connection.
     */
    private ObjectOutputStream out;

    /**
     * This attribute represents the lobby of the connection.
     */
    private Controller lobby;

    /**
     * This attribute represents the username of the connection.
     */
    private String username;

    /**
     * This attribute represents the timer that catches the ping.
     */
    private Timer catchPing;

    /**
     * This attribute represents the timer that sends the ping.
     */
    private Timer ping;

    /**
     * This attribute represents if the connection responds to the ping.
     */
    private boolean firstTime = true;

    /**
     * This attribute represents if the connection is disconnected.
     */
    private boolean disconnected = false;

    /**
     * This attribute represents the queue of the messages that where received by the connection.
     */
    private BlockingQueue<Message> messageQueue;

    /**
     * This attribute represents if the connection is processing an action.
     */
    private boolean processingAction;

    /**
     * This attribute represents the lock of the output stream.
     */
    private final Object outputLock = new Object();

    /**
     * This constructor creates a socket connection.
     * @param server is the server that the connection is connected to.
     * @param socket is the socket of the connection.
     */
    public SocketConnection(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            this.messageQueue = new LinkedBlockingQueue<>();
            processingAction = false;
            pickQueue();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                    readMessage();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection successfully ended");
            System.err.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This method reads a message from the input stream.
     * @throws IOException if there is an error in the input stream.
     * @throws ClassNotFoundException if the class of the object received is not found.
     */
    public void readMessage() throws IOException, ClassNotFoundException {
        Message message;
        message = (Message) in.readObject();
        if (message.getType().equals(MessageType.CATCH_PING)){
            catchPing();
        }
        else{
            messageQueue.add(message);
        }
    }

    /**
     * This method picks a message from the queue and processes it.
     */
    private void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                processQueue();
            }
        }, 0, 500);
    }

    /**
     * This method processes the queue of the messages.
     */
    private void processQueue() {
        if (!messageQueue.isEmpty() && !processingAction) {
            Message message = messageQueue.poll();
            processingAction = true;
            this.onMessage(message);
            processingAction = false;
            if (!messageQueue.isEmpty()) {
                processQueue();
            }
        }
    }

    /**
     * This method starts the timer that sends a ping message to the client.
     */
    public void ping(){
        ping = new Timer();
        catchPing = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new PingMessage());
            }
        }, 0, 4000);
        if (firstTime) {
            firstTime = false;
            catchPing.schedule(new TimerTask() {
                @Override
                public void run() {
                    onDisconnect();
                    cancelPing();
                }
            }, 5000, 5000);
        }
    }

    /**
     * This method catches the ping message.
     */
    public synchronized void catchPing(){
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
     * This method cancels the ping message.
     */
    public void cancelPing(){
        ping.cancel();
        catchPing.cancel();
    }

    /**
     * This method is run when the client disconnects.
     */
    public void onDisconnect() throws NullPointerException{
        try {
            try {
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
                    server.addDisconnectedPlayer(username, lobby);
                    if (game.getPlayerInTurn().getUsername().equals(username)) {
                        lobby.disconnectedWhileInTurn(username);
                    }
                    System.err.println(username + " got disconnected");
                    if (game.getGameState().equals(GameState.START)){
                        lobby.disconnectedWhileSetupping(this, username.equals(lobby.getPlayerInTurn()));
                    }
                }
            in.close();
            out.close();
            socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } catch (NullPointerException ignored){}
    }

    /**
     * This method reconnects the connection.
     * @param oldConnection is the old connection that belonged to the user and will be replaced by the new one.
     */
    @Override
    public void reconnect(Connection oldConnection) {
        this.lobby = oldConnection.getLobby();
        lobby.addAction(new ActionMessage(this, () -> this.lobby.reconnectBackup(this/*, oldConnection*/)));
        this.disconnected = false;
    }

    /**
     * This method sends a chat message to a specific player.
     * @param message is the message that has to be sent.
     * @param receiver is the connection that has to receive the message.
     */
    @Override
    public void sendChatMessage(String message, Connection receiver) {
        lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, receiver, message)));
    }

    /**
     * This method sends a global chat message.
     * @param message is the message that has to be sent.
     */
    @Override
    public void sendChatMessage(String message) {
        lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, message)));
    }

    /**
     * This method gets the lobby of the connection.
     * @return the lobby of the connection.
     */
    @Override
    public Controller getLobby(){
        return this.lobby;
    }

    /**
     * This method sets the lobby of the connection.
     * @param controller is the lobby that has to be set.
     */
    @Override
    public void setLobby(Controller controller) {
        this.lobby = controller;
    }

    /**
     * This method sends a message to the client.
     * @param message is the message that has to be sent.
     */
    @Override
    public void sendMessage(Message message) {
        if(!disconnected) {
            try {
                synchronized (outputLock) {
                    out.writeObject(message);
                    out.reset();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage() + " SocketConnection/sendMessage");
            }
        }
    }

    /**
     * This method asks the client whether it wants to create, join or reconnect to a lobby.
     * @param startingGamesId is the list of the id of the games that are starting.
     * @param gamesWhitDisconnectionsId is the list of the id of the games that have disconnections.
     */
    @Override
    public void joinGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
        sendMessage(new AvailableLobbiesMessage(startingGamesId, gamesWhitDisconnectionsId));
    }

    /**
     * This method handles the action of creating a game.
     */
    @Override
    public void createGame() {
        sendMessage(new NumPlayerRequestMessage());
    }

    /**
     * This method processes the message that was received.
     * @param message is the message that has to be processed.
     */
    public void onMessage(Message message){
        switch (message.getType()){
            case ADD_TO_CHAT:
                String receiver = ((AddToChatMessage) message).getReceiver();
                if (receiver == null) {
                    sendChatMessage(((AddToChatMessage) message).getMessage());
                }
                else{
                    sendChatMessage(((AddToChatMessage) message).getMessage(), server.getClientFromName(receiver));
                }
                break;
            case LOGIN_RESPONSE:
                server.login(this);
                break;
            case NUM_PLAYER_RESPONSE:
                if (server.usernameTaken(message.getSender())){
                    sendMessage(new UsernameRequestMessage(true, ((NumPlayerResponseMessage) message).getSize()));
                }
                else {
                    this.username = message.getSender();
                    server.setClient(this, username);
                    server.startPing(this);
                    server.createLobby(username, ((NumPlayerResponseMessage) message).getSize());
                }
                break;
            case LOBBY_INDEX:
                if (server.usernameTaken(message.getSender())){
                    sendMessage(new UsernameRequestMessage(false, ((LobbyIndexMessage) message).getChoice()));
                }
                else {
                    this.username = message.getSender();
                    server.setClient(this, username);
                    server.startPing(this);
                    server.joinLobby(username, ((LobbyIndexMessage) message).getChoice());
                }
                break;
            case RECONNECT_LOBBY_INDEX:
                String user = message.getSender();
                int room = ((ReconnectLobbyIndexMessage) message).getChoice();
                if (server.userNotDisconnected(user, room)) {
                    List<Integer> startingGamesId = ((ReconnectLobbyIndexMessage) message).getStartingGamesId();
                    List<Integer> gamesWhitDisconnectionsId = ((ReconnectLobbyIndexMessage) message).getGamesWithDisconnectionsId();
                    sendMessage(new GenericMessage("there is no player disconnected in game "+ room + " with that name.\n"));
                    sendMessage(new AvailableLobbiesMessage(startingGamesId, gamesWhitDisconnectionsId));
                }
                else {
                    this.username = user;
                    server.startPing(this);
                    server.reconnectPlayer(this, user);
                }
                break;
            case PLACE_STARTER_CARD:
                lobby.addAction(new ActionMessage(this, () -> lobby.placeStarterCard(this, ((PlaceStarterRequestMessage) message).getCard())));
                break;
            case PLACE_CARD:
                PlaceMessage p = (PlaceMessage) message;
                lobby.addAction(new ActionMessage(this, () -> lobby.placeCard(this, p.getCard(), p.getCoordinates())));
                break;
            case DRAW_DECK:
                String deck = ((DrawMessage) message).getDeck();
                lobby.addAction(new ActionMessage(this, () -> lobby.drawCard(this, deck)));
                break;
            case DRAW_BOARD:
                lobby.addAction(new ActionMessage(this, () -> lobby.drawCardFromBoard(this, ((DrawFromBoardMessage) message).getIndex())));
                break;
            case SET_ACHIEVEMENT:
                Achievement chosenAchievement = ((SetPrivateAchievementMessage) message).getAchievement();
                lobby.addAction(new ActionMessage(this, () -> lobby.chooseObj(this, chosenAchievement)));
                break;
            case COLOR_RESPONSE:
                Color chosenColor = ((ColorResponseMessage) message).getColor();
                lobby.addAction(new ActionMessage(this, () -> lobby.setColor(this, chosenColor)));
                break;
            case GENERIC_MESSAGE:
                System.err.println(message);
                break;
            default:
                break;
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
     * This method sends the update received from the model to the client.
     */
    @Override
    public void update(Message message) {
        sendMessage(message);
    }

    /**
     * This method removes the connection from the server.
     */
    @Override
    public void removeFromServer(boolean last) {
        cancelPing();
        server.removePlayers(username);
        if (last){
            server.removeGame(lobby);
        }
        System.err.println(username + " got disconnected");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the client that it has to wait.
     */
    @Override
    public void waiting() {
        sendMessage(new GenericMessage("someone else is joining a game, please wait..."));
    }
}
