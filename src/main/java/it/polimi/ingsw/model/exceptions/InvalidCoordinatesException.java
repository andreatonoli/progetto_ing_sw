package it.polimi.ingsw.model.exceptions;

public class InvalidCoordinatesException extends Exception{
    public InvalidCoordinatesException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Invalid Coordinates";
    }
}
