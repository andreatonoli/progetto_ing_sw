package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the game tries to start without being full.
 */
public class NotEnoughPlayersException extends Exception{
    public NotEnoughPlayersException() { super(); }
    @Override
    public String getMessage(){
        return "Not enough players to start the game";
    }
}
