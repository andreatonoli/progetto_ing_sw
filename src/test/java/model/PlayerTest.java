package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class PlayerTest {
    private final Game game = new Game(4);

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
        ResourceCard g15 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        ResourceCard b26 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER) }, 26, 0);
        GoldCard p36 = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 2, Condition.CORNER ,36 , new Integer[]{1, 0, 0, 3}, null);
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
        ResourceCard r1 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        ResourceCard r2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER) }, 2, 0);
        ResourceCard r3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI) }, 3, 0);
        ResourceCard g13 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT) }, 13, 0);
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
        ResourceCard r4 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 4, 0);
        ResourceCard r5 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL), new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT) }, 5, 0);
        ResourceCard r6 = new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER) }, 6, 0);
        ResourceCard r1 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
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
        ResourceCard r4 = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 4, 0);
        player.addInHand(r4);
        assertTrue(Arrays.stream(player.getCardInHand()).toList().contains(r4));
    }
    @Test
    @DisplayName("Add Points test")
    public void pointsTest(){
        Player p = new Player("arianna", game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 1);
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