package network.server;

import network.messages.Message;

public abstract class Connection {
    public abstract void sendMessage(Message message);
}
