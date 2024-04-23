package model;

import Controller.*;

import static org.junit.jupiter.api.Assertions.*;

import model.card.*;
import model.enums.Color;
import model.enums.CornerEnum;
import model.enums.PlayerState;
import model.enums.Symbols;
import model.exceptions.GameNotStartedException;
import model.exceptions.NotEnoughPlayersException;
import model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameTest {

    private final Game game1;
    private Player player1, player2, player3;

    public GameTest() throws IOException {
        this.game1 = new Game(4);
    }
    //TODO: testare costruttore e addPlayers

    /**
     * This test check that a game with three players assigns correctly the common cards
     * and the cards of all the players.
     */
    @Test
    @DisplayName("Test game start")
    public void testGameStart() throws NotEnoughPlayersException {
        player1 = new Player("mario", game1);
        player2 = new Player("luigi", game1);
        player3 = new Player("peach", game1);
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

    /**
     * This test check that a game with zero players don't assign the common cards
     */
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

    /**
     * This test check that a game with one player don't assign the common cards
     * and don't assign the cards to the player
     */
    @Test
    @DisplayName("Test game start with one players")
    public void testGameStartWithOnePlayers() throws NotEnoughPlayersException {
        player1 = new Player("mario", game1);
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

    /**
     * this test checks that a game with three players that placed some cards
     * to make all types of achievements is ended with the correct number
     * of points for each player
     * @throws NotEnoughPlayersException
     * @throws GameNotStartedException
     */
    @Test
    @DisplayName("Test game end")
    public void testGameEnd() throws NotEnoughPlayersException, GameNotStartedException {
        Controller c = new Controller(game1);
        player1 = new Player("mario", game1);
        player2 = new Player("luigi", game1);
        player3 = new Player("peach", game1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        game1.startGame();
        //3 points
        AchievementL a1 = new AchievementL(Color.RED);
        //3 points
        AchievementItem a2 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)));
        //2 points
        AchievementDiagonal a3 = new AchievementDiagonal(Color.PURPLE);
        //2 points
        AchievementResources a4 = new AchievementResources(Symbols.ANIMAL);
        //3 points
        AchievementL a5 = new AchievementL(Color.GREEN);
        game1.getGameBoard().setCommonAchievement(a1, 0);
        game1.getGameBoard().setCommonAchievement(a2, 1);
        player1.setChosenObj(a3);
        player2.setChosenObj(a4);
        player3.setChosenObj(a5);
        for (Player p : game1.getPlayers()){
            p.getPlayerBoard().setStarterCard(p.getPlayerBoard().getStarterCard());
        }
        player1.setPlayerState(PlayerState.PLAY_CARD);
        ResourceCard r2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER) }, 2, 0);
        ResourceCard r3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI) }, 3, 0);
        ResourceCard g11 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 11, 0);
        c.placeCard(player1, r2, new int[]{0,0} , CornerEnum.TR);
        c.placeCard(player1, r3, new int[]{0,0} , CornerEnum.BR);
        c.placeCard(player1, g11, new int[]{1,-1} , CornerEnum.BR);
        ResourceCard p31 = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 31, 0);
        ResourceCard p32 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT) }, 32, 0);
        ResourceCard p33 = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT) }, 33, 0);
        c.placeCard(player1, p31, new int[]{0,0} , CornerEnum.TL);
        c.placeCard(player1, p33, new int[]{-1,1} , CornerEnum.TL);
        c.placeCard(player1, p32, new int[]{-2,2} , CornerEnum.TL);
        //player1 made a1 and a3 achievements (total 5 points)
        player1.setPlayerState(PlayerState.NOT_IN_TURN);
        player2.setPlayerState(PlayerState.PLAY_CARD);
        ResourceCard r6 = new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER) }, 6, 0);
        ResourceCard g15 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        ResourceCard b26 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER) }, 26, 0);
        c.placeCard(player2, r6, new int[]{0,0} , CornerEnum.TR);
        c.placeCard(player2, g15, new int[]{1,1} , CornerEnum.BR);
        c.placeCard(player2, b26, new int[]{2,0} , CornerEnum.BR);
        ResourceCard b21 = new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 21, 0);
        c.placeCard(player2, b21, new int[]{0,0} , CornerEnum.TL);
        //player2 made a2 and a4 achievements (total 5 points)
        player2.setPlayerState(PlayerState.NOT_IN_TURN);
        player3.setPlayerState(PlayerState.PLAY_CARD);
        ResourceCard g12 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT) }, 12, 0);
        ResourceCard g13 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY) }, 13, 0);
        ResourceCard p34 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER) }, 34, 0);
        c.placeCard(player3, g12, new int[]{0,0} , CornerEnum.TR);
        c.placeCard(player3, g13, new int[]{0,0} , CornerEnum.BR);
        c.placeCard(player3, p34, new int[]{1,-1} , CornerEnum.BL);
        ResourceCard b25 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INKWELL) }, 25, 0);
        ResourceCard g17 = new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL) }, 17, 0);
        ResourceCard p35 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL) }, 35, 0);
        c.placeCard(player3, g17, new int[]{0,0}, CornerEnum.TL);
        c.placeCard(player3, b25, new int[]{-1,1}, CornerEnum.BL);
        c.placeCard(player3, p35, new int[]{-2,0},CornerEnum.BR);
        //player3 made a2 and a5 achievements (total 6 points)
        game1.endGame();

        assertEquals(player1.getPoints(), 5);
        assertEquals(player2.getPoints(), 5);
        assertEquals(player3.getPoints(), 6);
    }

    /**
     * this test checks that a game with three players that placed some cards
     * to make all types of achievements but without calling startGame(4) method
     * is ended with the GameNotStartedException and with no points added
     * to each player
     * @throws GameNotStartedException
     */
    @Test
    @DisplayName("Test game end without starting before")
    public void testGameEndWithoutStarting() throws GameNotStartedException {
        Controller c = new Controller(game1);
        player1 = new Player("mario", game1);
        player2 = new Player("luigi", game1);
        player3 = new Player("peach", game1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //starting the game without actually calling game1.startGame(4)
        AchievementL a1 = new AchievementL(Color.RED);
        AchievementItem a2 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)));
        AchievementDiagonal a3 = new AchievementDiagonal(Color.PURPLE);
        AchievementResources a4 = new AchievementResources(Symbols.ANIMAL);
        AchievementL a5 = new AchievementL(Color.GREEN);
        game1.getGameBoard().setCommonAchievement(a1, 0);
        game1.getGameBoard().setCommonAchievement(a2, 1);
        player1.setChosenObj(a3);
        player2.setChosenObj(a4);
        player3.setChosenObj(a5);
        StarterCard s1 = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        StarterCard s2 = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        StarterCard s3 = new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI)}, 3, new CardBack(new ArrayList<>(List.of(Symbols.PLANT, Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}));
        player1.getPlayerBoard().setStarterCard(s1);
        player2.getPlayerBoard().setStarterCard(s2);
        player3.getPlayerBoard().setStarterCard(s3);
        player1.setPlayerState(PlayerState.PLAY_CARD);
        ResourceCard r2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER) }, 2, 0);
        ResourceCard r3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI) }, 3, 0);
        ResourceCard g11 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 11, 0);
        c.placeCard(player1, r2, new int[]{0,0} , CornerEnum.TR);
        c.placeCard(player1, r3, new int[]{0,0} , CornerEnum.BR);
        c.placeCard(player1, g11, new int[]{1,-1} , CornerEnum.BR);
        ResourceCard p31 = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 31, 0);
        ResourceCard p32 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT) }, 32, 0);
        ResourceCard p33 = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT) }, 33, 0);
        c.placeCard(player1, p31, new int[]{0,0} , CornerEnum.TL);
        c.placeCard(player1, p33, new int[]{-1,1} , CornerEnum.TL);
        c.placeCard(player1, p32, new int[]{-2,2} , CornerEnum.TL);
        player1.setPlayerState(PlayerState.NOT_IN_TURN);
        player2.setPlayerState(PlayerState.PLAY_CARD);
        ResourceCard r6 = new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER) }, 6, 0);
        ResourceCard g15 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        ResourceCard b26 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER) }, 26, 0);
        c.placeCard(player2, r6, new int[]{0,0} , CornerEnum.TR);
        c.placeCard(player2, g15, new int[]{1,1} , CornerEnum.BR);
        c.placeCard(player2, b26, new int[]{2,0} , CornerEnum.BR);
        ResourceCard b21 = new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 21, 0);
        c.placeCard(player1, b21, new int[]{0,0} , CornerEnum.TL);
        player2.setPlayerState(PlayerState.NOT_IN_TURN);
        player3.setPlayerState(PlayerState.PLAY_CARD);
        ResourceCard g12 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT) }, 12, 0);
        ResourceCard g13 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY) }, 13, 0);
        ResourceCard p34 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER) }, 34, 0);
        c.placeCard(player3, g12, new int[]{0,0} , CornerEnum.TR);
        c.placeCard(player3, g13, new int[]{0,0} , CornerEnum.BR);
        c.placeCard(player3, p34, new int[]{1,-1} , CornerEnum.BL);
        ResourceCard b25 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INKWELL) }, 25, 0);
        ResourceCard g17 = new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL) }, 17, 0);
        ResourceCard p35 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL) }, 35, 0);
        c.placeCard(player3, g17, new int[]{0,0}, CornerEnum.TL);
        c.placeCard(player3, b25, new int[]{-1,1}, CornerEnum.BL);
        c.placeCard(player3, p35, new int[]{-2,0},CornerEnum.BR);
        game1.endGame();

        assertEquals(player1.getPoints(), 0);
        assertEquals(player2.getPoints(), 0);
        assertEquals(player3.getPoints(), 0);
    }
}
