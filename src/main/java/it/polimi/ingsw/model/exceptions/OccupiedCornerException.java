package it.polimi.ingsw.model.exceptions;

public class OccupiedCornerException extends Exception{
    public OccupiedCornerException(){
        super();
    }
    @Override
    public String getMessage(){
        return "This corner is already occupied";
    }
}
