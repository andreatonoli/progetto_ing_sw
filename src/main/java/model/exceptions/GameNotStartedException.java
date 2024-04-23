package model.exceptions;

public class GameNotStartedException extends Exception{
    public GameNotStartedException(){ super(); }
    @Override
    public String getMessage(){
        return "Game not started";
    }
}
