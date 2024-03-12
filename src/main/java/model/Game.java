package model;

import java.util.List;

public class Game {
    public static final int MAX_PLAYERS = 4; /** sets max number of players */

    public static final int MIN_PLAYERS = 2; /** sets min number of players */

    private GameState gameState;
    private List<Player> players;

    private Player firstPlayer;
    private Player playerInTurn;
    private ResourceDeck rDeck;
    private GoldDeck gDeck;
    private AchievementDeck aDeck;
    private StarterDeck sDeck;

    ResourceDeck rDeck = new ResourceDeck();
    GoldDeck gDeck = new GoldDeck();
    AchievementDeck aDeck = new AchievementDeck();
    StarterDeck sDeck = new StarterDeck();


    public ResourceDeck getResourceDeck()
    {
        return rDeck;
    }

    public GoldDeck getGoldDeck()
    {
        return gDeck;
    }

    public AchievementDeck getAchievementDeck()
    {
        return aDeck;
    }

    public StarterDeck getStarterDeck()
    {
        return sDeck;
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


}
