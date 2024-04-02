package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.ArrayList;

public class GameTest {

    private final Game game1;
    private final GameBoard board1;
    private Player player1, player2, player3;

    public GameTest() throws IOException {
        this.game1 = new Game();
        this.board1 = new GameBoard(game1);
        //IMPORTANTE: AGGIUNGERE DENTRO A COSTRUTTORE DI GAMEBOARD
        //LA CHIAMATA AL SETTER DELLA GAMEBOARD DEL GAME
        game1.setGameBoard(board1);
    }

    //DA TESTARE ANCHE CON LISTA DEI GIOCATORI VUOTA?

    /**
     * This test check that a game with three players assigns correctly the common cards
     * and the cards of all the players.
     */
    @Test
    @DisplayName("Test game start")
    public void testGameStart() {
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        player3 = new Player("peach", board1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        game1.startGame();

        assertNotNull(board1.getCommonAchievement());
        assertEquals(board1.getCommonAchievement().length, 2);
        for (int i = 0; i < board1.getCommonAchievement().length; i++){
            assertInstanceOf(Achievement.class, board1.getCommonAchievement()[i]);
            assertFalse(board1.getAchievementDeck().contains(board1.getCommonAchievement()[i]));
        }
        assertNotNull(board1.getCommonResource());
        assertEquals(board1.getCommonResource().length, 2);
        for (int i = 0; i < board1.getCommonResource().length; i++){
            assertInstanceOf(ResourceCard.class, board1.getCommonResource()[i]);
            assertFalse(board1.getResourceDeck().contains(board1.getCommonResource()[i]));
        }
        assertNotNull(board1.getCommonGold());
        assertEquals(board1.getCommonGold().length, 2);
        for (int i = 0; i < board1.getCommonGold().length; i++){
            assertInstanceOf(GoldCard.class, board1.getCommonGold()[i]);
            assertFalse(board1.getGoldDeck().contains(board1.getCommonGold()[i]));
        }

        ArrayList<Player> playersWithoutP;
        for (Player p : game1.getPlayers()){
            assertNotNull(p.getPlayerBoard().getStarterCard());
            assertInstanceOf(StarterCard.class, p.getPlayerBoard().getStarterCard());
            assertFalse(board1.getStarterDeck().contains(p.getPlayerBoard().getStarterCard()));
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
                assertFalse(board1.getResourceDeck().contains(p.getCardInHand()[i]));
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
            assertFalse(board1.getGoldDeck().contains(p.getCardInHand()[2]));
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
                assertFalse(board1.getAchievementDeck().contains(p.getPersonalObj()[i]));
            }
            assertNotNull(p.getChosenObj());
            assertInstanceOf(Achievement.class, p.getChosenObj());
            assertFalse(board1.getAchievementDeck().contains(p.getChosenObj()));
            assertTrue(p.getChosenObj().equals(p.getPersonalObj()[0]) || p.getChosenObj().equals(p.getPersonalObj()[1]));
        }

        assertNotNull(game1.getFirstPlayer());
        assertTrue(game1.getPlayers().contains(game1.getFirstPlayer()));
    }
}
