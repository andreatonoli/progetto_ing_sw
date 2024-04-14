package network.client;

import network.messages.LoginResponse;
import network.messages.Message;
import view.Ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
            System.out.println("Connection established"); //TOGLI
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            sendMessage(new LoginResponse(this.username));
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
                sendMessage(new LoginResponse(this.username));
                break;
            default:
                break;
        }
    }

}
