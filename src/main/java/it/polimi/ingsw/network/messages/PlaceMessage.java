package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;

public class PlaceMessage extends Message{
    private Card card;
    private int[] coordinates;

    public PlaceMessage(String sender, Card card, int[] coordinates){
        super(MessageType.PLACE_CARD, sender);
        this.card = card;
        this.coordinates = coordinates;
    }

    public Card getCard(){
        return this.card;
    }

    public int[] getCoordinates(){
        return this.coordinates;
    }

    @Override
    public String toString() {
        return "PlaceMessage{" +
                "card: " + card +
                "coordinates: " + coordinates +
                '}';
    }
}
