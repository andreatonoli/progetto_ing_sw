package model;

import model.exceptions.GameNotStartedException;
import model.exceptions.NotEnoughPlayersException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Game implements Serializable {
    public static final int MAX_PLAYERS = 4; /** sets max number of players */
    public static final int MIN_PLAYERS = 2; /** sets min number of players */
    private int lobbySize;
    private GameBoard gameBoard;
    private GameState gameState;
    private final ArrayList<Player> players;
    private int willPlay = -1;
    private boolean gameFull;
    private Player firstPlayer;
    private Player playerInTurn;
    private Chat chatHandler;

    private boolean gameStarted = false;

    /**
     *
     * @throws IOException
     */

    public Game(int lobbySize) {
        this.lobbySize = lobbySize;
        this.gameState = GameState.WAIT_PLAYERS;
        this.gameFull = false;
        this.players = new ArrayList<Player>();
        //this.players.add(first);
        this.firstPlayer = null;
        this.playerInTurn = null;
        this.chatHandler = new Chat(this);
        this.gameBoard = new GameBoard(this);
    }

    public void startGame(){
        this.setGameState(GameState.START);
        /** decks are shuffled*/
        Collections.shuffle(gameBoard.getStarterDeck());
        Collections.shuffle(gameBoard.getAchievementDeck());
        Collections.shuffle(gameBoard.getResourceDeck());
        Collections.shuffle(gameBoard.getGoldDeck());
        /** two cards are settled in the common board*/
        gameBoard.setCommonResource(gameBoard.drawCard(gameBoard.getResourceDeck()), 0);
        gameBoard.setCommonResource(gameBoard.drawCard(gameBoard.getResourceDeck()), 1);
        gameBoard.setCommonGold(gameBoard.drawCard(gameBoard.getGoldDeck()), 0);
        gameBoard.setCommonGold(gameBoard.drawCard(gameBoard.getGoldDeck()), 1);
        /** starter card is given to each player*/
        for (Player p : players) {
            p.getPlayerBoard().setStarterCard(gameBoard.drawCard(gameBoard.getStarterDeck()));
            /** in first place the player chose the side he prefers, in order to settle down the card*/
            /** when the controller sees that the player wants to settle down the card, it places starterCard*/
        }
        //colore segnalino scelto dal player al login
        /** every player draws two resource cards and a gold card*/
        for (Player p : players) {
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getGoldDeck()));
        }
        /** two achievement cards are settled in the common board */
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 0);
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 1);
        /** at this moment each player chose the achievement card */
        for (Player p : players) {
            p.getPersonalObj()[0] = gameBoard.drawCard();
            p.getPersonalObj()[1] = gameBoard.drawCard();
            //scelta carta da parte del player
            //per il momento il player sceglie la prima carta
            //poi verrà gestita nel controller
            p.setChosenObj(p.getPersonalObj()[0]);
        }

        /** the first player is chosen in a random way*/
        Collections.shuffle(players);
        setFirstPlayer();
        this.setGameState(GameState.IN_GAME);
        //gameStarted = true;
    }

    public void endGame() throws GameNotStartedException {
        try{
            if(/*!gameStarted*/!this.gameState.equals(GameState.IN_GAME)){ //fare controllare a ste
                throw new GameNotStartedException();
            }
            this.setGameState(GameState.END);
            for (Player p: players){
                p.getChosenObj().calcPoints(p);
                for (Achievement a : gameBoard.getCommonAchievement()){
                    a.calcPoints(p);
                }
            }

            /** the winner is chosen by the number of points */
            int max = 0;
            ArrayList<Player> winners = new ArrayList<>();
            for (Player p: players){
                if (p.getPoints() == max){
                    winners.add(p);
                    max = p.getPoints();
                }
                if (p.getPoints() > max){
                    winners = new ArrayList<>();
                    winners.add(p);
                    max = p.getPoints();
                }
            }
            //gameStarted = false;
        }
        catch(GameNotStartedException e){
            System.out.println("Game not started");
        }
    }

    public void setFirstPlayer()
    {
        firstPlayer = players.getFirst();
        firstPlayer.isFirstToPlay(firstPlayer.getUsername());
        firstPlayer.setPlayerState(PlayerState.PLAY_CARD);
    }

    public Player getFirstPlayer(){
        return this.firstPlayer;
    }

    public void setGameState(GameState nextGameState)
    {
        this.gameState = nextGameState;
    }

    public void setPlayerInTurn()
    {
        if (willPlay == lobbySize){
            willPlay = 0;
        }
        else{
            willPlay++;
        }
        this.playerInTurn = players.get(willPlay);
        playerInTurn.setPlayerState(PlayerState.PLAY_CARD);
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

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public void addPlayer(Player player){
        synchronized (this.players){
            this.players.add(player);
            if (players.size()==this.lobbySize){
                gameFull=true;
            }
        }
    }

    public boolean isFull(){
        return gameFull;
    }

    public GameBoard getGameBoard(){
        return this.gameBoard;
    }

    public Chat getChat(){
        return this.chatHandler;
    }
    public int getLobbySize(){
        return lobbySize;
    }
}
