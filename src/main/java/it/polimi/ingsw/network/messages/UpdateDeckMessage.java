package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.model.enums.Color;

public class UpdateDeckMessage extends Message{
    private final Color color;
    private final boolean isResource;
    public UpdateDeckMessage(Color backColor, boolean resource){
        super(MessageType.DECK_UPDATE, Server.serverName);
        this.color = backColor;
        this.isResource = resource;
    }

    public Color getColor() {
        return this.color;
    }
    public boolean getIsResource(){
        return isResource;
    }

    @Override
    public String toString() {
        return "UpdateDeckMessage{"+
                "colorBack : " + color +
                "isResource: " + isResource +
                '}';
    }
}
