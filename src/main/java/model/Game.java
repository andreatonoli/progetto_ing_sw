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

        //mescolamento mazzi
        Collections.shuffle(gameBoard.getStarterDeck());
        Collections.shuffle(gameBoard.getAchievementDeck());
        Collections.shuffle(gameBoard.getResourceDeck());
        Collections.shuffle(gameBoard.getGoldDeck());

        //posizionamento 2 carte nella board comune
        gameBoard.setCommonResource(gameBoard.drawCard(gameBoard.getResourceDeck()), 0);
        gameBoard.setCommonResource(gameBoard.drawCard(gameBoard.getResourceDeck()), 1);
        gameBoard.setCommonGold(gameBoard.drawCard(gameBoard.getGoldDeck()), 0);
        gameBoard.setCommonGold(gameBoard.drawCard(gameBoard.getGoldDeck()), 1);

        //a ogni giocatore viene data la carte iniziale
        for (Player p : players){
            p.getPlayerBoard().setStarterCard(gameBoard.drawCard(gameBoard.getStarterDeck()));
            //prima va fatto scegliere il lato al giocatore
            //quando il controller vede che il player vuole piazzare la carta posiziona la starterCard
        }

        //colore segnalino scelto dal player al login

        //ogni giocatore pesca due carte risorsa e due carte oro
        for (Player p : players){
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getGoldDeck()));
        }

        //posizonamento 2 carte obiettivo nella board comune
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 0);
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 1);

        //scelta carta obiettivo
        for (Player p : players){
            p.getPersonalObj()[0] = gameBoard.drawCard();
            p.getPersonalObj()[1] = gameBoard.drawCard();
            //scelta carta da parte del player
            //per il momento il player sceglie la prima carta
            //poi verrà gestita nel controller
            p.setChosenObj(p.getPersonalObj()[0]);
        }

        //scelta random primo giocatore
        Collections.shuffle(players);
        setFirstPlayer();

    }

    public void endGame() {

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

    public Chat getChat(){
        return this.chatHandler;
    }

}
