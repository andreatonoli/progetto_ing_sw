package Controller;

import static org.junit.jupiter.api.Assertions.*;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
import java.util.LinkedList;

public class DrawTest {
    /**
     * Test checks that a player that can draw a card draws it and adds it to his hand, then the card is removed from the deck
     * @throws IOException
     */
    @Test
    @DisplayName("Draw a Card")
    public void DrawACardTest() throws IOException {
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
        c.drawCard(player, deck);
        assertEquals(player.getCardInHand()[0], drawed);
        assertNotEquals(player.getCardInHand()[0], deck.getFirst());
    }
    /**
     *Checks that a player can't draw a card if the deck is empty, it is not his turn or his hand is full.
     * The test checks that before and after the drawCard method call the player hand doesn't change.
     * @throws IOException
     */
    @Test
    @DisplayName("Cannot Draw - Empty Deck")
    public void EmptyTest() throws IOException{
        Game game = new Game();
        GameBoard board = new GameBoard(game);
        Player player = new Player("pippo", board);
        player.setPlayerState(PlayerState.DRAW_CARD);
        LinkedList<Card> deck = new LinkedList<>();
        Controller c = new Controller();
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        ResourceCard b = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        player.addInHand(a);
        player.addInHand(b);
        c.drawCard(player, deck);
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
    }
    @Test
    @DisplayName("Cannot Draw - Full Hand")
    public void FullHandTest() throws IOException{
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
        //Full player's hand
        player.addInHand(a);
        player.addInHand(b);
        player.addInHand(d);
        //Populate the deck
        deck.add(e);
        //Trying to draw a card but player's hand is full
        c.drawCard(player, deck);
        //Checking that Card e isn't in the player's hand
        for (Card card : player.getCardInHand()){
            assertNotEquals(card, e);
        }
        //Player's hand hasn't changed after drawing
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        assertEquals(player.getCardInHand()[2], d);
    }
    @Test
    @DisplayName("Cannot Draw - Not in Turn/Place Card")
    public void NotInTurnTest() throws IOException{
        Game game = new Game();
        GameBoard board = new GameBoard(game);
        Player player = new Player("pippo", board);
        player.setPlayerState(PlayerState.NOT_IN_TURN);
        LinkedList<Card> deck = new LinkedList<>();
        Controller c = new Controller();
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        ResourceCard b = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        deck.add(a);
        player.addInHand(b);
        c.drawCard(player, deck);
        assertEquals(deck.getFirst(), a);
        assertNotEquals(player.getCardInHand()[1], a);
        player.setPlayerState(PlayerState.PLAY_CARD);
        c.drawCard(player, deck);
        assertEquals(deck.getFirst(), a);
        assertNotEquals(player.getCardInHand()[1], a);
    }
}
