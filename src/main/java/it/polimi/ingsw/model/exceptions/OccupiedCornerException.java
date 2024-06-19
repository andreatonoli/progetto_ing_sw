package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when a player tries to place a card on a corner which does not exist or that is already
 * covered by another card.
 */
public class OccupiedCornerException extends Exception{
    public OccupiedCornerException(){
        super();
    }
    @Override
    public String getMessage(){
        return "This corner is already occupied";
    }
}
