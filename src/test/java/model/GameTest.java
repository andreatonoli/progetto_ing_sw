package model;

import static org.junit.jupiter.api.Assertions.*;

import model.exceptions.NotEnoughPlayersException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.ArrayList;

public class GameTest {

    private final Game game1;
    private Player player1, player2, player3;

    public GameTest() throws IOException {
        this.game1 = new Game();
    }

    //DA TESTARE ANCHE CON LISTA DEI GIOCATORI VUOTA?

    /**
     * This test check that a game with three players assigns correctly the common cards
     * and the cards of all the players.
     */
    @Test
    @DisplayName("Test game start")
    public void testGameStart() throws NotEnoughPlayersException {
        player1 = new Player("mario", game1.getGameBoard());
        player2 = new Player("luigi", game1.getGameBoard());
        player3 = new Player("peach", game1.getGameBoard());
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        game1.startGame();

        assertNotNull(game1.getGameBoard().getCommonAchievement());
        assertEquals(game1.getGameBoard().getCommonAchievement().length, 2);
        for (int i = 0; i < game1.getGameBoard().getCommonAchievement().length; i++){
            assertInstanceOf(Achievement.class, game1.getGameBoard().getCommonAchievement()[i]);
            assertFalse(game1.getGameBoard().getAchievementDeck().contains(game1.getGameBoard().getCommonAchievement()[i]));
        }
        assertNotNull(game1.getGameBoard().getCommonResource());
        assertEquals(game1.getGameBoard().getCommonResource().length, 2);
        for (int i = 0; i < game1.getGameBoard().getCommonResource().length; i++){
            assertInstanceOf(ResourceCard.class, game1.getGameBoard().getCommonResource()[i]);
            assertFalse(game1.getGameBoard().getResourceDeck().contains(game1.getGameBoard().getCommonResource()[i]));
        }
        assertNotNull(game1.getGameBoard().getCommonGold());
        assertEquals(game1.getGameBoard().getCommonGold().length, 2);
        for (int i = 0; i < game1.getGameBoard().getCommonGold().length; i++){
            assertInstanceOf(GoldCard.class, game1.getGameBoard().getCommonGold()[i]);
            assertFalse(game1.getGameBoard().getGoldDeck().contains(game1.getGameBoard().getCommonGold()[i]));
        }

        ArrayList<Player> playersWithoutP;
        for (Player p : game1.getPlayers()){
            assertNotNull(p.getPlayerBoard().getStarterCard());
            assertInstanceOf(StarterCard.class, p.getPlayerBoard().getStarterCard());
            assertFalse(game1.getGameBoard().getStarterDeck().contains(p.getPlayerBoard().getStarterCard()));
            playersWithoutP = new ArrayList<>();
            playersWithoutP.addAll(game1.getPlayers());
            playersWithoutP.remove(p);
            for (Player pp : playersWithoutP){
                assertNotEquals(p.getPlayerBoard().getStarterCard(), pp.getPlayerBoard().getStarterCard());
            }

            assertNotNull(p.getCardInHand());
            assertEquals(p.getCardInHand().length, 3);
            for (int i = 0; i < p.getCardInHand().length - 1; i++){
                assertInstanceOf(ResourceCard.class, p.getCardInHand()[i]);
                assertFalse(game1.getGameBoard().getResourceDeck().contains(p.getCardInHand()[i]));
                playersWithoutP = new ArrayList<>();
                playersWithoutP.addAll(game1.getPlayers());
                playersWithoutP.remove(p);
                for (Player pp : playersWithoutP){
                    for(Card c : pp.getCardInHand()){
                        assertNotEquals(p.getCardInHand()[i], c);
                    }
                }
            }
            assertInstanceOf(GoldCard.class, p.getCardInHand()[2]);
            assertFalse(game1.getGameBoard().getGoldDeck().contains(p.getCardInHand()[2]));
            playersWithoutP = new ArrayList<>();
            playersWithoutP.addAll(game1.getPlayers());
            playersWithoutP.remove(p);
            for (Player pp : playersWithoutP){
                for(Card c : pp.getCardInHand()){
                    assertNotEquals(p.getCardInHand()[2], c);
                }
            }

            assertNotNull(p.getPersonalObj());
            assertEquals(p.getPersonalObj().length, 2);
            for (int i = 0; i < p.getPersonalObj().length; i++){
                assertInstanceOf(Achievement.class, p.getPersonalObj()[i]);
                assertFalse(game1.getGameBoard().getAchievementDeck().contains(p.getPersonalObj()[i]));
            }
            assertNotNull(p.getChosenObj());
            assertInstanceOf(Achievement.class, p.getChosenObj());
            assertFalse(game1.getGameBoard().getAchievementDeck().contains(p.getChosenObj()));
            assertTrue(p.getChosenObj().equals(p.getPersonalObj()[0]) || p.getChosenObj().equals(p.getPersonalObj()[1]));
        }

        assertNotNull(game1.getFirstPlayer());
        assertTrue(game1.getPlayers().contains(game1.getFirstPlayer()));
    }

    @Test
    @DisplayName("Test game start without players")
    public void testGameStartWithoutPlayers() throws NotEnoughPlayersException {
        game1.startGame();

        for (Achievement a : game1.getGameBoard().getCommonAchievement()){
            assertNull(a);
        }
        for (Card r : game1.getGameBoard().getCommonResource()){
            assertNull(r);
        }
        for (Card g : game1.getGameBoard().getCommonGold()){
            assertNull(g);
        }

    }

    @Test
    @DisplayName("Test game start with one players")
    public void testGameStartWithOnePlayers() throws NotEnoughPlayersException {
        player1 = new Player("mario", game1.getGameBoard());
        game1.addPlayer(player1);
        game1.startGame();

        for (Achievement a : game1.getGameBoard().getCommonAchievement()){
            assertNull(a);
        }
        for (Card r : game1.getGameBoard().getCommonResource()){
            assertNull(r);
        }
        for (Card g : game1.getGameBoard().getCommonGold()){
            assertNull(g);
        }
        for (Card c : player1.getCardInHand()){
            assertNull(c);
        }
    }
}
