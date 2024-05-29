package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class PlayerTest {
    private Game game;

    @BeforeEach
    public void setup(){
        game = new Game(4);
    }

    /**
     * This test check that a card that has been removed from player's hand is
     * the right card and that the other card has not been modified
     */

    @Test
    @DisplayName("Remove card from player's hand")
    public void testRemoveCardFromHand()  {

        // create the player
        Player player = new Player("chiara", game);
        // create three cards for the player, two resource cards and one gold card
        Card g15 = new Card(new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.PLANT)), "resource", 15, Color.GREEN);
        Card b26 = new Card(new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.ANIMAL)), "resource", 26, Color.BLUE);
        Card p36 = new Card(new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 2, Condition.CORNER , new Integer[]{1, 0, 0, 3}, null), new CardBack(List.of(Symbols.INSECT)), "gold", 36, Color.PURPLE);
        // adds cards to player's hand
        player.addInHand(g15);
        player.addInHand(b26);
        player.addInHand(p36);
        // remove one card from player's hand
        player.removeFromHand(b26);

        //verify that b26 isn't in player's hand
        for (int i=0; i< player.getCardInHand().length; i++) {
            assertNotEquals(player.getCardInHand()[i],b26);
        }
        //verify that the other two cards are still in their place
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(g15));
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(p36));
    }

    /**
     * This test check that if I remove a card which is not in player's hand
     * this has no effect for cards in player's hand (number and type)
     */
    @Test
    @DisplayName("Remove a card that is not in player's hand")
    public void testRemoveCard()
    {
        Player player = new Player("camilla", game);
        Card r1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 1, Color.RED);
        Card r2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 2, Color.RED);
        Card r3 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 3, Color.RED);
        Card g13 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 13, Color.GREEN);
        player.addInHand(r1);
        player.addInHand(r2);
        player.addInHand(r3);
        player.removeFromHand(g13);
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r1));
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r2));
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r3));
    }

    /**
     * This test check that a card which has been added in a full hand
     * is not contained in player's hand after the call of the method
     */
    @Test
    @DisplayName("Add a card in a full hand")
    public void testAddCardFullHand() {
        Player player = new Player("arianna", game);
        Card r4 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 4, Color.RED);
        Card r5 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL), new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 5, Color.RED);
        Card r6 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 6, Color.RED);
        Card r1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 1, Color.RED);
        player.addInHand(r4);
        player.addInHand(r5);
        player.addInHand(r6);
        player.addInHand(r1);
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r4));
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r5));
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r6));
        assertFalse(Arrays.stream(player.getCardInHand()).toList().contains(r1));
    }

    /**
     * This test check that a card is actually added to player's hand after the call of addInHand
     */
    @Test
    @DisplayName("Add a card in player's hand")
    public void testAddCard() {
        Player player = new Player("alba", game);
        Card r4 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 4, Color.RED);
        player.addInHand(r4);
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r4));
    }
    @Test
    @DisplayName("Add Points test")
    public void pointsTest(){
        Player p = new Player("arianna", game);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
        Card a = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 4, Color.RED);
        //Add card in p's hand and placing its starter card
        p.addInHand(a);
        p.getPlayerBoard().setStarterCard(s);
        //Adding some points
        p.addPoints(19);
        assertEquals(19, p.getPoints());
        //Placing the card and reaching 20 points
        p.setPlayerState(PlayerState.PLAY_CARD);
        assertDoesNotThrow(() -> p.placeCard(a, new int[]{1,1}));
        //p reaches 20 points, so he's the first to end
        assertEquals(20, p.getPoints());
        assertTrue(p.isFirstToEnd());
        //Adding 20 points, the max points a player can do is 29
        p.addPoints(20);
        assertEquals(29, p.getPoints());
    }

}