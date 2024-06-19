package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the card the user wants to draw from the board does not exist.
 */
public class CardNotFoundException extends Exception{
    public CardNotFoundException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Selected card not found";
    }
}
