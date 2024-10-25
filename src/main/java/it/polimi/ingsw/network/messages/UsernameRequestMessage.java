package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to request a username to the client.
 */
public class UsernameRequestMessage extends Message{
    private final boolean creation;
    private final int number;
    public UsernameRequestMessage(boolean creation, int number) {
        super(MessageType.USERNAME_REQUEST, Server.serverName);
        this.creation = creation;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString(){
        return "LoginRequest{" +
                "creation = " + creation +
                "number = " + number +
                "sender = " + getSender() +
                '}';
    }
}
