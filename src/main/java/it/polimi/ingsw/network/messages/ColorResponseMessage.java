package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Color;

/**
 * Message used to send the color chosen by the player.
 */
public class ColorResponseMessage extends Message{
    private final Color color;
    public ColorResponseMessage(String username, Color color){
        super(MessageType.COLOR_RESPONSE, username);
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
    @Override
    public String toString() {
        return "ColorResponseMessage{" +
                "color: " + color +
                '}';
    }
}
