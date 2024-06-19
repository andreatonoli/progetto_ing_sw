package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the user tries to place two cards on the same spot.
 */
public class AlreadyUsedPositionException extends Exception{
    public AlreadyUsedPositionException(){
        super();
    }
    @Override
    public String getMessage(){
        return "A card is already placed in this position";
    }
}
