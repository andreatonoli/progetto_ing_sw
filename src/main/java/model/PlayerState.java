package model;

/**
 * PLAY_CARD => player is placing a card
 * DRAW_CARD => Player is drawing a card
 * NOT_IN_TURN => Player waiting his turn
 */
public enum PlayerState {
    PLAY_CARD,
    DRAW_CARD,
    NOT_IN_TURN
}
