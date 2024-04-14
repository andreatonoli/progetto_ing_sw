package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private final Server server;
    private final int port;
    public SocketServer(Server server, int port){
        this.server = server;
        this.port = port;
        this.startServer();
    }
    public void startServer(){
        ServerSocket serverSocket;
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(this.port);
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
