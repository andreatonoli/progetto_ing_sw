package it.polimi.ingsw.model.enums;

/**
 * Enum that represents the conditions shown on the gold card. Those conditions are needed to calculate the points obtained
 * when placing a gold card.
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
