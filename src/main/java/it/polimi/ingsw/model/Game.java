package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.exceptions.GameNotStartedException;
import it.polimi.ingsw.network.messages.GenericMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends Observable implements Serializable {

    /**
     * LobbySize is the number of players that the game can host.
     */
    private final int lobbySize;

    /**
     * GameBoard is the board where the game is played.
     */
    private final GameBoard gameBoard;

    /**
     * GameState is the state the game is currently in.
     */
    private GameState gameState;

    /**
     * Players is the list of players that are currently in the game.
     */
    private final ArrayList<Player> players;

    /**
     * willPlay is the index of the player that will play next.
     */
    private int willPlay;

    /**
     * gameFull is a boolean that is true if the game has reached the maximum number of players.
     */
    private boolean gameFull;

    /**
     * firstPlayer is the player that will play first.
     */
    private Player firstPlayer;

    /**
     * playerInTurn is the player that is currently playing.
     */
    private Player playerInTurn;

    /**
     * chatHandler is the chat of the game.
     */
    private final Chat chatHandler;

    /**
     * Number of disconnected players.
     */
    private int disconnections;

    /**
     * List of available colors for the players.
     */
    private final List<Color> availableColors;

    /**
     * Constructor for the Game class. It initializes the game with the given lobby size. The game is in the WAIT_PLAYERS.
     * @param lobbySize the number of players that the game can host.
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
        this.gameBoard = new GameBoard();
        this.availableColors = new ArrayList<>(List.of(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW));
    }

    /**
     * Method that returns the available colors for the players.
     * @return the list of available colors.
     */
    public List<Color> getAvailableColors(){
        return availableColors;
    }

    /**
     * Method that starts the game. It shuffles the decks and gives the cards to the players.
     * @throws NotEnoughPlayersException if the game has not reached the maximum number of players.
     */
    public void startGame() throws NotEnoughPlayersException{
        if (!this.gameFull){
            throw new NotEnoughPlayersException();
        }
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
    }

    /**
     * Method that ends the game. It calculates the points of each player and one or more winners are chosen.
     * The winner is the player with the highest number of points. In case of a tie in points, the winner is the player
     * with the highest number of objectives completed. If there is still a tie, the game is a draw.
     * @return the list of winners.
     * @throws GameNotStartedException if the game has not started yet.
     */
    public ArrayList<Player> endGame() throws GameNotStartedException {
        if(!this.gameState.equals(GameState.IN_GAME)){
            throw new GameNotStartedException();
        }
        this.gameState = GameState.END;
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
                    endPoints = p.getPoints();
                }
            }
        }
        //the winner is chosen by the number of points and the number of objectives completed in case of a tie in points between two or more players
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
        return winners;
    }

    /**
     * Method that ends the game due to disconnection of one or more players.
     */
    public void endGameByDisconnection(){
        notifyAll(new GenericMessage("\ngame ended due to lack of players"));
        this.gameState = GameState.END;
    }

    /**
     * Method that sets the first player to play.
     */
    private void setFirstPlayer()
    {
        firstPlayer = players.getFirst();
        firstPlayer.setFirstToPlay();
        firstPlayer.setPlayerState(PlayerState.PLAY_CARD);
        willPlay = 4;
        this.setPlayerInTurn();
    }

    /**
     * Method that returns the first player to play.
     * @return the first player.
     */
    public Player getFirstPlayer(){
        return this.firstPlayer;
    }

    /**
     * Method that sets the state in which the game is currently in.
     * @param nextGameState the current state of the game.
     */
    public void setGameState(GameState nextGameState)
    {
        this.gameState = nextGameState;
    }

    /**
     * Method that sets the player that is currently playing.
     */
    public void setPlayerInTurn()
    {
        willPlay++;
        if (willPlay >= lobbySize){
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
    }

    /**
     * Method that returns the player that is currently playing.
     * @return the player that is currently playing.
     */
    public Player getPlayerInTurn(){
        return this.playerInTurn;
    }

    /**
     * Gets the state in which the game is played.
     * @return returns the state in which the game is played.
     */
    public GameState getGameState()
    {
        return gameState;
    }

    /**
     * Method that returns the list of players that are currently in the game.
     * @return the list of players.
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * Method that returns the player with the given username.
     * @param username the username of the player.
     * @return the player with the given username.
     */
    public Player getPlayerByUsername(String username){
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Method that adds a player to the game.
     * @param player the player to be added.
     */
    public void addPlayer(Player player){
        synchronized (this.players){
            this.players.add(player);
            if (players.size()==this.lobbySize){
                gameFull = true;
            }
        }
    }

    /**
     * Method that checks if the game is full.
     * @return true if the game is full, false otherwise.
     */
    public boolean isFull(){
        return gameFull;
    }

    /**
     * Method that returns the game board.
     * @return the game board.
     */
    public GameBoard getGameBoard(){
        return this.gameBoard;
    }

    /**
     * Method that returns the chat of the game.
     * @return the chat of the game.
     */
    public Chat getChat(){
        return this.chatHandler;
    }

    /**
     * Method that returns the number of players that the game can host.
     * @return the number of players that the game can host.
     */
    public int getLobbySize(){
        return lobbySize;
    }

    /**
     * Method that returns the number of disconnected players.
     * @return the number of disconnected players.
     */
    public int getDisconnections() {
        return disconnections;
    }

    /**
     * Method that adds a disconnection to the game.
     * @param disconnectedPlayers the number of disconnected players.
     */
    public void addDisconnections(int disconnectedPlayers) {
        disconnections += disconnectedPlayers;
    }

    /**
     * Method that removes a player from the game.
     * @param username the username of the player to be removed.
     */
    public void removePlayer(String username){
        players.remove(getPlayerByUsername(username));
    }
}
