package network.server;

import network.client.RMIClientHandler;
import network.messages.Message;

public class RMIConnection extends Connection {
    RMIClientHandler client;
    Server server;
    public RMIConnection(Server server, RMIClientHandler client){
        this.client = client;
        this.server = server;
    }

    @Override
    public void sendMessage(Message message) {

    }
}
