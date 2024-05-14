package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.network.server.Server;

import java.util.ArrayList;

public class OpponentsMessage extends Message{
    ArrayList<String> players;
    public OpponentsMessage(ArrayList<String> players){
        super(MessageType.OPPONENTS, Server.serverName);
        this.players = players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
    @Override
    public String toString(){
        return "OpponentsMessage{" +
                "opponents: " + players +
                '}';
    }
}
