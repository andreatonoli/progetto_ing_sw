package model.exceptions;

public class EmptyException extends Exception{
    public EmptyException(){ super(); }
    @Override
    public String getMessage(){
        return "The deck is empty";
    }
}
