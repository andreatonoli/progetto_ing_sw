package Controller;
import model.*;

import java.util.LinkedList;

/*Metodi da aggiungere
    addInHand : Player => metodo per aggiungere carta alla mano
 */
public class Controller {
    /**
     *Picks the top card of the deck and calls addInHand to give it to the player
     * @param player who wants to draw a card
     * @param deck from which the player choose to pick a card
     */
    public void drawResource(Player player, LinkedList<Card> deck){
        if (canDraw(player, deck)){
            Card drawedCard = deck.getFirst();
            player.addInHand(drawedCard);
            deck.removeFirst();
        }
    }
    /**
     * checks if the player can draw a card from a specified deck.
     * @param player to control
     * @return the possibility to draw a card
     */
    public boolean canDraw(Player player, LinkedList<Card> deck){
        //player cannot draw a card if the deck is empty, his hand is full (dim >= 3), hasn't already played a card or if it's not his turn
        if (deck.isEmpty()){
            return false;
        }
        if (player.getPlayerState().equals(PlayerState.NOT_IN_TURN) || player.getPlayerState().equals(PlayerState.PLAY_CARD)){
            return false;
        }
        for (int i = 0; i < 3; i++) {
            if (player.getCardInHand()[i] == null){
                return true;
            }
        }
        return true;
    }
}
