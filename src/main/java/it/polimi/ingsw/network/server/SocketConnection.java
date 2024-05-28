package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
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

public class SocketConnection extends Connection implements Runnable {
    private Server server;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Controller lobby;
    private String username;
    private Timer catchPing;
    private Timer ping;
    private boolean firstTime = true;
    private boolean go = true;
    private BlockingQueue<Message> messageQueue;
    private boolean processingAction;
    private final Object outputLock = new Object();

    public SocketConnection(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            this.setConnectionStatus(true);
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
        //TODO: capire quando chiudere connessione
        while(getConnectionStatus() && go) {
            readMessage();
        }
    }

    public void readMessage(){
        Message message;
        try {
            message = (Message) in.readObject();
            if (message.getType().equals(MessageType.CATCH_PING)){
                catchPing();
            }
            else{
                messageQueue.add(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection successfully ended");
            go = false;
            System.err.println(e.getMessage());
        }
    }

    private void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                processQueue();
            }
        }, 0, 500);
    }

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

    public void ping(){
        ping = new Timer();
        catchPing = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new PingMessage());
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

    public synchronized void catchPing(){
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

    public void cancelPing(){
        ping.cancel();
        catchPing.cancel();
    }

    public void onDisconnect(){
        if (!lobby.getGame().getGameState().equals(GameState.END)) {
            try {
                System.err.println(username + " got disconnected");
                in.close();
                out.close();
                socket.close();
                setConnectionStatus(false);
                lobby.getGame().getPlayerByUsername(username).setDisconnected(true);
                server.addDisconnectedPlayer(username);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void reconnect(Connection oldConnection) {
        this.lobby = oldConnection.getLobby();
        this.lobby.reconnectBackup(this/*, oldConnection*/);
    }

    @Override
    public void sendChatMessage(String message, Connection receiver) {
        lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, receiver, message)));
    }

    @Override
    public void sendChatMessage(String message) {
        lobby.addAction(new ActionMessage(this, () -> lobby.sendChatMessage(this, message)));
    }

    @Override
    public Controller getLobby(){
        return this.lobby;
    }

    @Override
    public void setLobby(Controller controller) {
        this.lobby = controller;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            synchronized (outputLock){
                out.writeObject(message);
                out.reset();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + " SocketConnection/sendMessage");
        }
    }

    @Override
    public void joinGame(List<Controller> activeGames) {
        sendMessage(new FreeLobbyMessage(activeGames.size()));
    }

    @Override
    public void createGame() {
        sendMessage(new NumPlayerRequestMessage());
    }

    public void onMessage(Message message){
        switch (message.getType()){
            case REMOVE_FROM_SERVER:
                this.cancelPing();
                int playersToDelete = lobby.getGame().getLobbySize()-1;
                lobby.getGame().setLobbySize(playersToDelete);
                server.removePlayers(username);
                if (playersToDelete == 0){
                    server.removeGame(lobby);
                }
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                break;
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
                if (server.usernameTaken(message.getSender())){
                    sendMessage(new UsernameRequestMessage());
                }
                else{
                    this.username = message.getSender();
                    server.login(this, this.username);
                }
                break;
            case NUM_PLAYER_RESPONSE:
                server.createLobby(message.getSender(), ((NumPlayerResponseMessage) message).getSize());
                break;
            case LOBBY_INDEX:
                server.joinLobby(message.getSender(), ((LobbyIndexMessage) message).getChoice());
                break;
            case PLACE_STARTER_CARD:
                lobby.addAction(new ActionMessage(this, () -> lobby.placeStarterCard(this, ((PlaceStarterRequestMessage) message).getCard())));
                break;
            case PLACE_CARD:
                PlaceMessage p = (PlaceMessage) message;
                lobby.addAction(new ActionMessage(this, () -> lobby.placeCard(this, p.getCard(), p.getCoordinates())));
                break;
            case DRAW_DECK:
                //TODO: passare deck tramite stringa è la soluzione migliore?
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
    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public void update(Message message) {
        sendMessage(message);
    }
}
