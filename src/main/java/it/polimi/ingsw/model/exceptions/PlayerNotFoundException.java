package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when a player tries to send a message to another player who is not in the same lobby.
 */
public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(){ super(); }
    @Override
    public String getMessage(){
        return "Player does not exists";
    }
}
