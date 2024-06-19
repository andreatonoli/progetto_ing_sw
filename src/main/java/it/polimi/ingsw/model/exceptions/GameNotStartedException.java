package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the game tries to end without starting.
 */
public class GameNotStartedException extends Exception{
    public GameNotStartedException(){ super(); }
    @Override
    public String getMessage(){
        return "Game not started";
    }
}
