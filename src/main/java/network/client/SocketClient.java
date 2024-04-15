package network.client;

import network.messages.*;
import view.Ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    private String username;
    private Ui view;
    private Scanner stdin; //forse inutile
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public SocketClient(String username, String address, int port, Ui view){
        this.username = username;
        this.view = view;
        this.stdin = new Scanner(System.in); //probabilmente gli input arriveranno tutti dalla view
        this.startClient(address, port);
    }
    public void startClient(String address, int port){
        try {
            Socket socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            sendMessage(new LoginResponseMessage(this.username));
            //TODO: capire come far chiudere la connessione
            while(true){
                readMessage();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    //TODO: fare lettura parallela dei messaggi
    public void readMessage(){
        Message message;
        try {
            message = (Message) in.readObject();
            update(message);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(Message message){
        try {
            out.writeObject(message);
            out.reset();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    //TODO: Si può fare con override?
    public void update(Message message){
        switch (message.getType()){
            case USERNAME_REQUEST:
                System.out.println("Username is already taken, please choose another: ");
                this.username = this.view.askNickname();
                sendMessage(new LoginResponseMessage(this.username));
                break;
            case FREE_LOBBY:
                int freeLobbySize = ((FreeLobbyMessage) message).getStartingGames().size();
                int response = this.view.selectGame(((FreeLobbyMessage) message).getStartingGames());
                if (response == freeLobbySize){
                    int lobbySize = this.view.setLobbySize();
                    sendMessage(new NumPlayerResponseMessage(this.username, lobbySize));
                }
                else {
                    sendMessage(new LobbyIndexMessage(this.username, response));
                }
                break;
            case NUM_PLAYER_REQUEST:
                int lobbySize = this.view.setLobbySize();
                sendMessage(new NumPlayerResponseMessage(this.username, lobbySize));
                break;
            default:
                break;
        }
    }

}
