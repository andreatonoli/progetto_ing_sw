package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class UsernameRequestMessage extends Message{
    boolean creation;
    int number;
    public UsernameRequestMessage(boolean creation, int number) {
        super(MessageType.USERNAME_REQUEST, Server.serverName);
        this.creation = creation;
        this.number = number;
    }

    public boolean isCreation() {
        return creation;
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
