package model.exceptions;

public class NotInTurnException extends Exception{
    //TODO: scrivere messaggi diversi in base a dove viene lanciata
    public NotInTurnException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Not player's turn";
    }
}
