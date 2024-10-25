package it.polimi.ingsw.model.enums;

/**
 * Enumerates the possible states of a player.
 */
public enum PlayerState {

    /**
     * Player has to place a card.
     */
    PLAY_CARD("place a card"),

    /**
     * Player has to draw a card.
     */
    DRAW_CARD("draw one of the visible cards or draw a card from a deck"),

    /**
     * Player is waiting for his turn.
     */
    NOT_IN_TURN("wait for your next turn");

    /**
     * Text linked to the state to better explain to the player what he has to do.
     */
    private final String text;

    /**
     * State constructor.
     * @param text text linked to the state to better explain to the player what he has to do.
     */
    PlayerState(String text){
        this.text=text;
    }

    @Override
    public String toString() {
        return text;
    }
}
