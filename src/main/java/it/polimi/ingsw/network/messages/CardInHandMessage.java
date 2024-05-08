package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

public class CardInHandMessage extends Message{
    private Card[] hand;
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
                "cards: " + hand +
                '}';
    }
}
