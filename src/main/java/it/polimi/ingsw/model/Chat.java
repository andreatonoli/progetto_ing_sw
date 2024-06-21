package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {

    public static final int CHATDIM = 25;

    /** players' list */
    private final ArrayList<Player> players;

    public Chat(Game game) {
        /** game's id */
        this.players = game.getPlayers();
    }

    /**
     * Sends a message to the receiver.
     * @param sender the player who sends the message.
     * @param receiver the player who receives the message.
     * @param global if the message is global.
     * @param message the message to send.
     * @throws PlayerNotFoundException if the receiver is not found.
     */
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
