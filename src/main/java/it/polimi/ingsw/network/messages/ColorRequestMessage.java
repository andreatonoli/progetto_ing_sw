package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.server.Server;
import java.util.List;

public class ColorRequestMessage extends Message{
    private List<Color> colors;

    public ColorRequestMessage(List<Color> colors){
        super(MessageType.COLOR_REQUEST, Server.serverName);
        this.colors = colors;
    }
    public List<Color> getColors(){
        return colors;
    }
    @Override
    public String toString(){
        return "ColorRequestMessage{" +
                "colors: " + colors +
                '}';
    }
}
