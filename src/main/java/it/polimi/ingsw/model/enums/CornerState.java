package it.polimi.ingsw.model.enums;

/**
 * Enumerates the possible states of a corner.
 */
public enum CornerState {

    /**
     * Corner that is coverable by a card and which symbol is counted in the symbolCount.
     */
    VISIBLE,

    /**
     * Corner that cannot be used to place a card on.
     */
    NOT_VISIBLE,

    /**
     * Corner that is covered by a card.
     */
    OCCUPIED
}
