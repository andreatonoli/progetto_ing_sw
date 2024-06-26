package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

import java.util.Arrays;

/**
 * Message used to send the cards in the hand of the player.
 */
public class CardInHandMessage extends Message{
    private final Card[] hand;
    public CardInHandMessage(Card[] hand){
        super(MessageType.CARD_HAND, Server.serverName);
        this.hand = new Card[3];
        System.arraycopy(hand, 0, this.hand, 0, 3);
    }

    public Card[] getHand(){
        return this.hand;
    }

    @Override
    public String toString() {
        return "CardInHandMessage{" +
                "cards: " + Arrays.toString(hand) +
                '}';
    }
}
