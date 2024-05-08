package it.polimi.ingsw.model.exceptions;

public class NotEnoughPlayersException extends Exception{
    public NotEnoughPlayersException() { super(); }
    @Override
    public String getMessage(){
        return "Not enough players to start the game";
    }
}
