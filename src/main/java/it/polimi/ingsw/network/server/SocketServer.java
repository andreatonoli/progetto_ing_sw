package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible for creating a server socket and accepting incoming connections.
 * When a connection is accepted, a new SocketConnection is created and executed.
 */
public class SocketServer {

    /**
     * Link to the main server.
     */
    private final Server server;

    /**
     * Port on which the server is listening.
     */
    private final int port;

    /**
     * Constructor for the class.
     * Then starts the server.
     * @param server Link to the main server.
     * @param port Port on which the server is listening.
     */
    public SocketServer(Server server, int port){
        this.server = server;
        this.port = port;
        this.startServer();
    }

    /**
     * This method creates a new server socket and starts listening for incoming connections.
     * When a connection is accepted, a new SocketConnection is created and executed.
     */
    public void startServer(){
        ServerSocket serverSocket;
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("Started Socket connection.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        while(true){
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketConnection(this.server, socket));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
    }
}
