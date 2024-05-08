package it.polimi.ingsw.network.server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SocketConnection extends Connection implements Runnable {
    private Server server;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Controller lobby;
    private String username;

    public SocketConnection(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            this.setConnectionStatus(true);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public void run(){
        //TODO: capire quando chiudere connessione
        while(this.getConnectionStatus()){
            try {
                Message message = (Message) in.readObject();
                onMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                this.onDisconnect();
                System.err.println(e.getMessage());
                System.out.println("Connection successfully ended");
                return;
            }
        }
    }

    public void onDisconnect(){
        try {
            in.close();
            out.close();
            socket.close();
            this.setConnectionStatus(false);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void setLobby(Controller controller) {
        this.lobby = controller;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.reset();
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
            case FLIP_CARD:
                lobby.flipCard(this, ((FlipRequestMessage) message).getCard());
                break;
            case PLACE_STARTER_CARD:
                lobby.placeStarterCard(this, ((PlaceStarterRequestMessage) message).getCard());
                break;
            case SET_ACHIEVEMENT:
                lobby.chooseObj(this, ((SetPrivateAchievementMessage) message).getAchievement());
                break;
            case GENERIC_MESSAGE:
                System.err.println(message);
            default:
                break;
        }
    }
    public String getUsername(){
        return this.username;
    }

    @Override
    public void setAchievement(Achievement achievement) {
        this.lobby.chooseObj(this, achievement);
    }

    @Override
    public void update(Message message) {
        sendMessage(message);
    }
}
