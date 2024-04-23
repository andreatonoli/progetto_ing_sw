package model.exceptions;

public class CostNotSatisfiedException extends Exception{
    public CostNotSatisfiedException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Not enough resources to satisfy the card cost";
    }
}
