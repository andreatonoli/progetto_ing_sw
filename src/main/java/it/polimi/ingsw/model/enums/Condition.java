package it.polimi.ingsw.model.enums;

/**
 * Enumerates the possible conditions that a card can have to give points to the player.
 */
public enum Condition {
    
    /**
     * Gives 2 points for every corner covered by the placed card.
     */
    CORNER,

    /**
     * Gives 1 point for every uncovered required item in the player's board.
     */
    ITEM,

    /**
     * There are no conditions.
     */
    NOTHING
}
