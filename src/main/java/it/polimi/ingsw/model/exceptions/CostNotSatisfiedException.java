package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the user tries to place a gold card without fulfilling its cost.
 */
public class CostNotSatisfiedException extends Exception{
    public CostNotSatisfiedException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Not enough resources to satisfy the card cost";
    }
}
