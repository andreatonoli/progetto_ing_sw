package model;

import model.exceptions.PlayerNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {

    public static final int CHATDIM = 7;

    //id del game
    private Game game;

    //ogni player ha la sua chat
    private Player player;

    //lista dei player
    private ArrayList<Player> players;

    public Chat(Game game) {
        this.game = game;
        this.players = game.getPlayers();
    }

    public void forwardMessage(Player sender, Player receiver, boolean global, String message) throws PlayerNotFoundException{
        if (global){
            for (Player p : players){
                p.displayMessage(sender, message);
            }
        }
        else{
            if(!players.contains(receiver)){
                throw new PlayerNotFoundException();
            }
            sender.displayMessage(sender, message);
            receiver.displayMessage(receiver, message);
        }
    }
}
