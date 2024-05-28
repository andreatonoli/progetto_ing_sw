package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.Server;

import java.util.ArrayList;
import java.util.List;

public class WinnerMessage extends Message{
    ArrayList<String> winners = new ArrayList<>();
    public WinnerMessage(List<Player> winners){
        super(MessageType.DECLARE_WINNER, Server.serverName);
        for (Player winner : winners) {
            this.winners.add(winner.getUsername());
        }
    }
    public ArrayList<String> getWinners() {
        return winners;
    }
    @Override
    public String toString(){
        return "WinnerMessage {" +
                "winners : " + winners +
                "}";
    }
}
