package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.Server;

import java.util.ArrayList;

public class WinnerMessage extends Message{
    ArrayList<Player> winners;
    public WinnerMessage(ArrayList<Player> winners){
        super(MessageType.DECLARE_WINNER, Server.serverName);
        this.winners = winners;
    }
    public ArrayList<Player> getWinners() {
        return winners;
    }
    @Override
    public String toString(){
        return "WinnerMessage {" +
                "winners : " + winners +
                "}";
    }
}
