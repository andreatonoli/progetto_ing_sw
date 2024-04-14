package network.server;

import model.Game;
import network.messages.UsernameRequest;
import network.messages.Message;

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
    public SocketConnection(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public int joinGame(List<Game> activeGames) {
        return 0;
    }
    public void onMessage(Message message){
        switch (message.getType()){
            case LOGIN_RESPONSE:
                if (server.usernameTaken(message.getSender())){
                    sendMessage(new UsernameRequest());
                }
                else{
                    server.login(this, message.getSender());
                }
                break;
            default:
                break;
        }
    }
}
