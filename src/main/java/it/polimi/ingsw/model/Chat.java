package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Chat class represents the chat of the game.
 */
public class Chat implements Serializable {

    /** chat dimension */
    public static final int CHATDIM = 25;

    /** players' list */
    private final ArrayList<Player> players;

    /**
     * Constructor for the Chat class.
     * @param game the game to which the chat belongs.
     */
    public Chat(Game game) {
        this.players = game.getPlayers();
    }

    /**
     * Forwards a message to the receiver.
     * @param sender the player who sent the message.
     * @param receiver the player who will receive the message.
     * @param global true if the message is global, false otherwise.
     * @param message the message to be forwarded.
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
