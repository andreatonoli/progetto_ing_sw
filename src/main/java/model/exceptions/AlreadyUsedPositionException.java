package model.exceptions;

public class AlreadyUsedPositionException extends Exception{
    public AlreadyUsedPositionException(){
        super();
    }
    @Override
    public String getMessage(){
        return "A card is already placed in this position";
    }
}
