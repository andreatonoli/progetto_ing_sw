package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

public class CommonCardUpdateMessage extends Message{
    private final Card card;
    private final int index;
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
