package network.server;

public class SocketServer {
    private final Server server;
    private final int port;
    public SocketServer(Server server, int port){
        this.server = server;
        this.port = port;
    }
}
