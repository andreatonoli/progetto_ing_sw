package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;

public class PlaceStarterRequestMessage extends Message{
    private final Card card;

    public PlaceStarterRequestMessage(String username, Card card){
        super(MessageType.PLACE_STARTER_CARD, username);
        this.card = card;
    }
    public Card getCard() {
        return this.card;
    }

    @Override
    public String toString() {
        return "PlaceRequestMessage{" +
                "card: " + card +
                '}';
    }
}
