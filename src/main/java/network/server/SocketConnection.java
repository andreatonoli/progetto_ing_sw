package network.server;

import network.messages.LoginRequest;
import network.messages.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketConnection extends Connection implements Runnable {
    //Client client
    Server server;
    private Socket socket;
    Scanner in;
    PrintWriter out;
    public SocketConnection(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run(){
        //TODO: capire quando chiudere connessione
        sendMessage(new LoginRequest());
        //while(true){
        //
        //}
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
        out.write(String.valueOf(message));
    }
}
