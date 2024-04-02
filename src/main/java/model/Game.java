package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    public static final int MAX_PLAYERS = 4; /** sets max number of players */
    public static final int MIN_PLAYERS = 2; /** sets min number of players */
    private GameBoard gameBoard;
    private GameState gameState;
    private ArrayList<Player> players;
    private Player firstPlayer;
    private Player playerInTurn;
    private Chat chatHandler;

    /**
     *
     * @throws IOException
     */

    //eccezione si può togliere da game ma prima va tolta da gameboard
    public Game() throws IOException {
        this.gameState = GameState.WAIT_PLAYERS;
        this.players = new ArrayList<Player>();
        //this.players.add(first);
        this.firstPlayer = null;
        this.playerInTurn = null;
        this.chatHandler = new Chat(this);
        this.gameBoard = new GameBoard(this);
    }

    public void startGame() {

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
        for (Player p : players){
            p.getPlayerBoard().setStarterCard(gameBoard.drawCard(gameBoard.getStarterDeck()));
            /** in first place the player chose the side he prefers, in order to settle down the card*/
            /** when the controller sees that the player wants to settle down the card, it places starterCard*/
        }

        //colore segnalino scelto dal player al login

        /** every player draws two resource cards and a gold card*/
        for (Player p : players){
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getGoldDeck()));
        }

        /** two achievement cards are settled in the common board */
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 0);
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 1);

        /** at this moment each player chose the achievement card */
        for (Player p : players){
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

    }

    public void endGame() {
        //per il momento aggiunta punti non dentro a calcPoints
        for (Player p: players){
            p.addPoints(p.getChosenObj().calcPoints(p));
            for (Achievement a : gameBoard.getCommonAchievement()){
                p.addPoints(a.calcPoints(p));
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

    public void setGameBoard(GameBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    public Chat getChat(){
        return this.chatHandler;
    }

}
