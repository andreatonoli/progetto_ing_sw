package network.server;

public class SocketConnection extends Connection {
    //Client client
    Server server;
    public SocketConnection(Server server){
        this.server = server;
    }
}
