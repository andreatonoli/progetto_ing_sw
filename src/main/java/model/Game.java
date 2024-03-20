package model;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    public static final int MAX_PLAYERS = 4; /** sets max number of players */

    public static final int MIN_PLAYERS = 2; /** sets min number of players */

    private GameBoard gameBoard;
    private GameState gameState;
    private ArrayList<Player> players;
    private Player firstPlayer;
    private Player playerInTurn;
    private Chat messagesContainer;

    /**
     * @param first is the first player to enter, first create the game
     */
    public Game() throws IOException {
        this.gameState = GameState.WAIT_PLAYERS;
        this.players = new ArrayList<Player>();
        //this.players.add(first);
        this.firstPlayer = null;
        this.playerInTurn = null;
        this.messagesContainer = new Chat(this);
        this.gameBoard = new GameBoard(this);
    }

    public Chat getChat(){
        return this.messagesContainer;
    }

    public void startGame()
    {

    }

    public void endGame()
    {

    }

    public void setFirstPlayer()
    {
        firstPlayer = players.get(0);
        firstPlayer.isFirstToPlay(firstPlayer.getUsername());
        firstPlayer.setPlayerState(PlayerState.PLAY_CARD);
    }

    public void setGameState(GameState nextGameState)
    {
        this.gameState = nextGameState;
    }

    public void setPlayerInTurn(Player playerPlaying)
    {
        this.playerInTurn = playerPlaying;
    }

    public GameState getGameState()
    {
        /** returns the state in which the game is played */
        return gameState;
    }

    public Player getPlayerInTurn()
    {
        /**
         returns player that is playing the game at that moment
         */
        return playerInTurn;
    }

    public int getNumOfPlayers()
    {
        return players.size();
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    //temporaneo
    public void addPlayer(Player player){
        this.players.add(player);
    }

    public GameBoard getGameBoard(){
        return this.gameBoard;
    }

}
