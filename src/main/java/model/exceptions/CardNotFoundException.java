package model.exceptions;

public class CardNotFoundException extends Exception{
    public CardNotFoundException(){
        super();
    }
    @Override
    public String getMessage(){
        return "Selected card not found";
    }
}
