package network.messages;

import model.card.Card;
import network.server.Server;

import java.rmi.ServerError;

public class CardInHandMessage extends Message{
    Card[] hand;
    public CardInHandMessage(Card[] hand){
        super(MessageType.CARD_HAND, Server.serverName);
        hand = new Card[3];
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
