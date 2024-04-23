package network.messages;

import model.card.Card;
import network.server.Server;

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
