package it.polimi.ingsw.model.exceptions;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(){ super(); }
    @Override
    public String getMessage(){
        return "Player does not exists";
    }
}
