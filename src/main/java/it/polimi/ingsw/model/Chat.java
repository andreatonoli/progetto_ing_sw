package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {

    public static final int CHATDIM = 7;

    /** game's id */
    private Game game;

    /** players' list */
    private final ArrayList<Player> players;

    public Chat(Game game) {
        this.game = game;
        this.players = game.getPlayers();
    }

    /** this method is useful to send messages */

    public void forwardMessage(Player sender, Player receiver, boolean global, String message) throws PlayerNotFoundException {
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
            receiver.displayMessage(sender, message);
        }
    }
}
