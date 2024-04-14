package network.client;

import view.Ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    private final String username;
    private Ui view;
    private Scanner stdin; //forse inutile
    private Scanner in;
    private PrintWriter out;
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
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
