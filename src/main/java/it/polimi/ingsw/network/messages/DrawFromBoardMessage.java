package it.polimi.ingsw.network.messages;

/**
 * Message used to notify the server that the player wants to draw a card from the board.
 */
public class DrawFromBoardMessage extends Message{
    private final int index;
    public DrawFromBoardMessage(String sender, int index){
        super(MessageType.DRAW_BOARD, sender);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "DrawFromBoardMessage{" +
                "index: " + index +
                '}';
    }
}
