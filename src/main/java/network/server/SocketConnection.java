package network.server;

import Controller.*;
import model.card.Card;
import network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SocketConnection extends Connection implements Runnable {
    //Client client
    private transient Server server;
    private transient Socket socket;
    private transient ObjectInputStream in;
    private transient ObjectOutputStream out;
    //private ClientController controller;
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
    //TODO: game controller?
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
    public void flipCard(Card card) {

    }

    @Override
    public void placeStarterCard(Card card) {

    }

    @Override
    public void update(Message message) {
        sendMessage(message);
    }
}
