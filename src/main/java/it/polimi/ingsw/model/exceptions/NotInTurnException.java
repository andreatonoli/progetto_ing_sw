package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when a player tries to perform an action, such as place a card or draw one, without being in turn
 */
public class NotInTurnException extends Exception{
    public NotInTurnException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Not player's turn";
    }
}
