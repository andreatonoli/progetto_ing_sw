package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the user draws a card already having 3 cards in his hand.
 */
public class FullHandException extends Exception{
    public FullHandException(){
        super ();
    }
    @Override
    public String getMessage(){
        return "Player's hand is full";
    }
}
