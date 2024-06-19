package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the user tries to draw from an empty deck.
 */
public class EmptyException extends Exception{
    public EmptyException(){ super(); }
    @Override
    public String getMessage(){
        return "The deck is empty";
    }
}
