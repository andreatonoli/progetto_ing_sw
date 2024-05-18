package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.exceptions.GameNotStartedException;
import it.polimi.ingsw.network.messages.GenericMessage;
import it.polimi.ingsw.network.messages.WinnerMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends Observable implements Serializable {
    private int lobbySize;
    private GameBoard gameBoard;
    private GameState gameState;
    private final ArrayList<Player> players;
    private int willPlay;
    private boolean gameFull;
    private Player firstPlayer;
    private Player playerInTurn;
    private Chat chatHandler;
    /**
     * number of disconnected players
     */
    private int disconnections;
    private List<Color> availableColors;


    /**
     *
     * @throws IOException
     */

    public Game(int lobbySize) {
        this.lobbySize = lobbySize;
        this.gameState = GameState.WAIT_PLAYERS;
        this.gameFull = false;
        this.players = new ArrayList<>();
        this.firstPlayer = null;
        this.playerInTurn = null;
        this.disconnections = 0;
        this.chatHandler = new Chat(this);
        this.gameBoard = new GameBoard(this);
        this.availableColors = new ArrayList<>(List.of(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW));
    }

    public List<Color> getAvailableColors(){
        return availableColors;
    }

    public void startGame() throws  NotEnoughPlayersException{
        if (!this.gameFull){
            throw new NotEnoughPlayersException();
        }
        this.gameState = GameState.START;
        //Decks are shuffled
        Collections.shuffle(gameBoard.getStarterDeck());
        Collections.shuffle(gameBoard.getAchievementDeck());
        Collections.shuffle(gameBoard.getResourceDeck());
        Collections.shuffle(gameBoard.getGoldDeck());
        //two cards for each type are settled in the common board
        gameBoard.setCommonResource(gameBoard.drawCard(gameBoard.getResourceDeck()), 0);
        gameBoard.setCommonResource(gameBoard.drawCard(gameBoard.getResourceDeck()), 1);
        gameBoard.setCommonGold(gameBoard.drawCard(gameBoard.getGoldDeck()), 0);
        gameBoard.setCommonGold(gameBoard.drawCard(gameBoard.getGoldDeck()), 1);
        //starter card is given to each player
        for (Player p : players) {
            p.getPlayerBoard().giveStarterCard(gameBoard.drawCard(gameBoard.getStarterDeck()));
            //in first place the player chose the side he prefers, in order to settle down the card
            //when the controller sees that the player wants to settle down the card, it places starterCard
        }
        // every player draws two resource cards and a gold card
        for (Player p : players) {
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getResourceDeck()));
            p.addInHand(gameBoard.drawCard(gameBoard.getGoldDeck()));
        }
        //two achievement cards are settled in the common board
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 0);
        gameBoard.setCommonAchievement(gameBoard.drawCard(), 1);
        //at this moment each player chose the achievement card
        for (Player p : players) {
            p.getPersonalObj()[0] = gameBoard.drawCard();
            p.getPersonalObj()[1] = gameBoard.drawCard();
        }
        //the first player is chosen in a random way
        Collections.shuffle(players);
        setFirstPlayer();
        this.gameState = GameState.IN_GAME;
    }

    public void endGame() throws GameNotStartedException {
        try{
            if(!this.gameState.equals(GameState.IN_GAME)){
                throw new GameNotStartedException();
            }
            this.gameState = GameState.END;
            notifyAll(new GenericMessage("game is ended, counting objective points..."));
            int endPoints;
            for (Player p: players){
                endPoints = p.getPoints();
                p.getChosenObj().calcPoints(p);
                if (p.getPoints()>endPoints){
                    p.addObjCompleted();
                    endPoints=p.getPoints();
                }
                for (Achievement a : gameBoard.getCommonAchievement()){
                    a.calcPoints(p);
                    if (p.getPoints()>endPoints){
                        p.addObjCompleted();
                    }
                }
            }
            /** the winner is chosen by the number of points */
            int max = 0;
            ArrayList<Player> winners = new ArrayList<>();
            for (Player p: players){
                if (p.getPoints() == max && p.getObjCompleted()>winners.getFirst().getObjCompleted()) {
                    winners = new ArrayList<>();
                    winners.add(p);
                }
                else if (p.getPoints() == max && p.getObjCompleted()==winners.getFirst().getObjCompleted()){
                    winners.add(p);
                }
                else if (p.getPoints() > max){
                    winners = new ArrayList<>();
                    winners.add(p);
                    max = p.getPoints();
                }
            }
            notifyAll(new WinnerMessage(winners));
        }
        catch(GameNotStartedException e){
            System.err.println("Game not started");
        }
    }

    public void endGameByDisconnection(Player lastManStanding){
        notifyAll(new GenericMessage("game ended due to lack of players"));
        ArrayList<Player> winners = new ArrayList<>();
        winners.add(lastManStanding);
        notifyAll(new WinnerMessage(winners));
    }

    private void setFirstPlayer()
    {
        firstPlayer = players.getFirst();
        firstPlayer.isFirstToPlay(firstPlayer.getUsername());
        firstPlayer.setPlayerState(PlayerState.PLAY_CARD);
        willPlay = 0;
    }

    public Player getFirstPlayer(){
        return this.firstPlayer;
    }

    public void setGameState(GameState nextGameState)
    {
        this.gameState = nextGameState;
    }

    public Player setPlayerInTurn()
    {
        willPlay++;

        if (willPlay == lobbySize){
            willPlay = 0;
        }

        while (players.get(willPlay).isDisconnected()){
            willPlay++;
            if (willPlay == lobbySize){
                willPlay = 0;
            }
        }
        this.playerInTurn = players.get(willPlay);
        playerInTurn.setPlayerState(PlayerState.PLAY_CARD);
        return playerInTurn;
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

    public Player getPlayerByUsername(String username){
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public void addPlayer(Player player){
        synchronized (this.players){
            this.players.add(player);
            if (players.size()==this.lobbySize){
                gameFull = true;
            }
            notifyAll(new GenericMessage("Players in lobby: " + players.size() + "/" + lobbySize));
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

    public int getDisconnections() {
        return disconnections;
    }
    //TODO: fix
    public void setDisconnections(int disconnectedPlayers) {
        //disconnections = disconnectedPlayers;
    }


}
