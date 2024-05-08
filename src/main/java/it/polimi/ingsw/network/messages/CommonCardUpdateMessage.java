package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.server.Server;

public class CommonCardUpdateMessage extends Message{
    private final Card card;
    public CommonCardUpdateMessage(MessageType mType, Card card){
        super(mType, Server.serverName);
        this.card = card;
    }
    public Card getCard(){
        return this.card;
    }
    @Override
    public String toString() {
        return "CommonGoldCardUpdateMessage{" +
                "Card : " + card +
                '}';
    }
}
