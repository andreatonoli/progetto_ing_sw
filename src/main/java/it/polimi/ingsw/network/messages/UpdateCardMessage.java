package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

/**
 * Message used to update a card in the hand of the client.
 */
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
