package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when a user tries to join a lobby which is already full of players.
 */
public class FullLobbyExeption extends Exception{
    public FullLobbyExeption(){
        super();
    }
    public String getMessage(){
        return "the game is already full";
    }
}
