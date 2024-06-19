package it.polimi.ingsw.model.enums;

/**
 * Enumeration that represents the various phases of the game.
 */
public enum GameState {
    /**
     * Waiting for players to fill the lobby.
     */
    WAIT_PLAYERS,

    /**
     * Triggered when the lobby is full.
     * Starting the game. The model now handles the setup phase.
     */
    START,

    /**
     * Triggered when the setup phase is finished.
     * The actual game. Every player has to place and draw a card.
     */
    IN_GAME,

    /**
     * Triggered when the extra round is finished.
     * The model calculates the achievement points and declare the winner(s).
     */
    END
}
