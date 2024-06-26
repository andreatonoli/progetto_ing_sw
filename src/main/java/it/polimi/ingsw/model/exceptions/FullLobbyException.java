package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when a user tries to join a lobby which is already full of players.
 */
public class FullLobbyException extends Exception{
    public FullLobbyException(){
        super();
    }
    public String getMessage(){
        return "the game is already full";
    }
}
