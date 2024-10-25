package it.polimi.ingsw.network.server;

/**
 * Represents a message that contains an action to be executed.
 */
public record ActionMessage(Connection applicant, Action command) {
}
