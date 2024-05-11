package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardBack;
import it.polimi.ingsw.network.server.Server;

public class UpdateDeckMessage extends Message{
    private final CardBack card;
    public UpdateDeckMessage(CardBack card){
        super(MessageType.DECK_UPDATE, Server.serverName);
        this.card = card;
    }

    public Card getCard() {
        return this.card;
    }

    @Override
    public String toString() {
        return "UpdateDeckMessage{"+
                "CardBack : " + card +
                '}';
    }
}
