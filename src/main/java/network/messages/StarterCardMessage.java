package network.messages;

import model.card.Card;
import network.server.Server;

public class StarterCardMessage extends Message{
    private final Card card;
    public StarterCardMessage(Card card){
        super(MessageType.STARTER_CARD, Server.serverName);
        this.card = card;
    }

    public Card getCard() {
        return this.card;
    }

    @Override
    public String toString() {
        return "StarterCardMessage{"+
                "Card : " + card +
                '}';
    }
}
