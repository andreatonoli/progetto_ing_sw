package it.polimi.ingsw.network.messages;

public class DrawMessage extends Message{
    private String deck;

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
