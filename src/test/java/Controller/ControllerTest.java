package Controller;

import junit.framework.Assert;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import Controller.Controller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.LinkedList;

public class ControllerTest {
    /**
     * Test checks that a player that can draw a card draws it and adds it to his hand, then the card is removed from the deck
     * @throws IOException
     */
    @Test
    @DisplayName("Draw a Card")
    public void DrawTest() throws IOException {
        Game game = new Game();
        GameBoard board = new GameBoard(game);
        Player player = new Player("pippo", board);
        player.setPlayerState(PlayerState.DRAW_CARD);
        LinkedList<Card> deck = new LinkedList<>();
        ResourceCard drawed = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        deck.add(drawed);
        deck.add(new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0));
        deck.add(new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI)}, 3, 0));
        Controller c = new Controller();
        c.drawResource(player, deck);
        assertEquals(player.getCardInHand()[0], drawed);
        assertNotEquals(player.getCardInHand()[0], deck.getFirst());
    }

    /**
     *Checks that a player can't draw a card if the deck is empty, it is not his turn or his hand si full.
     * The test checks that before and after the drawResource method call the player hand doesn't change.
     * @throws IOException
     */
    @Test
    @DisplayName("Cannot Draw")
    public void EmptyTest() throws IOException{
        Game game = new Game();
        GameBoard board = new GameBoard(game);
        Player player = new Player("pippo", board);
        player.setPlayerState(PlayerState.DRAW_CARD);
        LinkedList<Card> deck = new LinkedList<>();
        Controller c = new Controller();
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        ResourceCard b = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        ResourceCard d = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), null, new Corner(Symbols.EMPTY) }, 8, 1);
        ResourceCard e = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 9, 1);
        player.addInHand(a);
        player.addInHand(b);
        c.drawResource(player, deck);
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        deck.add(d);
        deck.add(e);
        player.setPlayerState(PlayerState.NOT_IN_TURN);
        c.drawResource(player, deck);
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        player.setPlayerState(PlayerState.DRAW_CARD);
        c.drawResource(player, deck);
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        assertEquals(player.getCardInHand()[2], d);
        c.drawResource(player, deck);
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        assertEquals(player.getCardInHand()[2], d);
    }
}
