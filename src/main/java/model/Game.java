package model;

public class Game {
    public static final int MAX_PLAYERS = 4; /** sets max number of players */

    private int minPlayer;
    private GameState gamestate;
    private List<Player> players;

    public void startGame()
    {

    }

    public void endGame()
    {

    }

    public void setFirstPlayer()
    {

    }

    public void setGameState()
    {

    }

    public void setPlayerInTurn()
    {

    }

    public GameState getNameState()
    {
        /** returns the state in which the game is played */
        return gamestate;
    }

    public List<Player> getPlayerInTurn()
    {
        /**
         returns player that is playing the game at that moment
         */
        return players;
    }

    public int getNumOfPlayers()
    {
        return players.size();
    }


}
