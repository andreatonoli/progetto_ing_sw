package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when a player tries to place a card on a non-valid spot.
 */
public class InvalidCoordinatesException extends Exception{
    public InvalidCoordinatesException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Invalid Coordinates";
    }
}
