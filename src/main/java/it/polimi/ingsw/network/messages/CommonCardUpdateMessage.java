package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

/**
 * Message used to update the common cards on the table.
 */
public class CommonCardUpdateMessage extends Message{
    private final Card card;
    private final int index;

    /**
     * Creates a CommonCardUpdateMessage instance.
     * @param mType the message type. It represents whether the card sent is a gold card or a resource card.
     * @param card the card to update.
     * @param index the index where to set the card.
     */
    public CommonCardUpdateMessage(MessageType mType, Card card, int index){
        super(mType, Server.serverName);
        this.card = card;
        this.index = index;
    }
    public Card getCard(){
        return this.card;
    }

    public int getIndex(){
        return index;
    }
    @Override
    public String toString() {
        return "CommonCardUpdateMessage{" +
                "Card : " + card +
                "Index : " + index +
                '}';
    }
}
