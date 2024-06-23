package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to notify the client that the server is waiting for the reconnection of at least one player.
 */
public class WaitingReconnectionMessage extends Message {

    private final String username;

    public WaitingReconnectionMessage(String username){
        super(MessageType.WAITING_RECONNECTION, Server.serverName);
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public String toString() {
        return "WaitingReconnectionMessage{" +
                "name : " + username +
                '}';
    }

}
