package network.messages;

import model.card.Card;

public class FlipRequestMessage extends Message{
    private Card card;
    public FlipRequestMessage(String username, Card card){
        super(MessageType.FLIP_CARD, username);
        this.card = card;
    }

    public Card getCard(){
        return this.card;
    }

    @Override
    public String toString() {
        return "FlipRequestMessage{" +
                "card: " + card +
                '}';
    }
}
