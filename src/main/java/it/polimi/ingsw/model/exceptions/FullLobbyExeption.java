package it.polimi.ingsw.model.exceptions;

public class FullLobbyExeption extends Exception{
    public FullLobbyExeption(){
        super();
    }
    public String getMessage(){
        return "the game is already full";
    }
}
