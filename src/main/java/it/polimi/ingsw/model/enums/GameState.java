package it.polimi.ingsw.model.enums;

/**
 * WAIT_PLAYERS => Waiting for the minimum amount of players to start the game
 * START => Starting the game, placing cards on the table and in player's hand, drawing the private and common achievement card
 * IN_GAME => Each player plays his turn till one of them reaches 20 points
 * END => Calculating achievement points
 */
public enum GameState {
    WAIT_PLAYERS,
    START,
    IN_GAME,
    END
}
