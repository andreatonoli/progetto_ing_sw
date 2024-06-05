package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class WaitingReconnectionMessage extends Message {

    public String username;

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
