package model;

/**
 * VISIBLE => coverable corner
 * NOT_VISIBLE => corner covered by another card
 * OCCUPIED => corner that covers another card
 */
public enum CornerState {
    VISIBLE,
    NOT_VISIBLE
}
