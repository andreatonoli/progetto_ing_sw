package network.messages;

import model.card.Card;
import network.server.Server;

public class UpdateCardMessage extends Message {
    private final Card card;
    public UpdateCardMessage(Card card){
        super(MessageType.CARD_UPDATE, Server.serverName);
        this.card = card;
    }

    public Card getCard() {
        return this.card;
    }

    @Override
    public String toString() {
        return "UpdateCardMessage{"+
                "Card : " + card +
                '}';
    }
}
