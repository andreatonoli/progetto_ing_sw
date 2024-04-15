package network.server;

import model.Game;
import network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SocketConnection extends Connection implements Runnable {
    //Client client
    Server server;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Message message;

    public SocketConnection(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public void run(){
        //TODO: capire quando chiudere connessione
        while(true){
            try {
                Message message = (Message) in.readObject();
                onMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public void onDisconnect(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.reset();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void joinGame(List<Game> activeGames) {
        sendMessage(new FreeLobbyMessage(activeGames));
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
                    System.out.println("Ciao carlo");
                    sendMessage(new UsernameRequestMessage());
                }
                else{
                    server.login(this, message.getSender());
                }
                break;
            case NUM_PLAYER_RESPONSE:
                server.createLobby(message.getSender(), ((NumPlayerResponseMessage) message).getSize());
                break;
            case LOBBY_INDEX:
                server.joinLobby(message.getSender(), ((LobbyIndexMessage) message).getChoice());
                break;
            default:
                break;
        }
    }
}
