package it.polimi.ingsw.model.exceptions;

public class FullHandException extends Exception{
    public FullHandException(){
        super ();
    }
    @Override
    public String getMessage(){
        return "Player's hand is full";
    }
}
