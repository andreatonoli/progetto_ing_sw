package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

/**
 * Message used to send the starter card to the client.
 */
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
