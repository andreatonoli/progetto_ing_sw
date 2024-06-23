package it.polimi.ingsw.network.messages;

/**
 * Message used to notify the server that the player wants to draw from a deck
 */
public class DrawMessage extends Message{
    private final String deck;

    public DrawMessage(String sender, String chosenDeck){
        super(MessageType.DRAW_DECK, sender);
        this.deck = chosenDeck;
    }
    public String getDeck(){
        return deck;
    }
    @Override
    public String toString() {
        return "DrawMessage{" +
                "deck: " + deck +
                '}';
    }
}
