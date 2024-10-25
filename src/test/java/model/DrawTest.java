package model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardBack;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.exceptions.EmptyException;
import it.polimi.ingsw.model.exceptions.FullHandException;
import it.polimi.ingsw.model.exceptions.NotInTurnException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.LinkedList;
import java.util.List;

public class DrawTest {
    /**
     * Test checks that a player that can draw a card draws it and adds it to his hand, then the card is removed from the deck
     */
    @Test
    @DisplayName("Draw a Card")
    public void DrawACardTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.DRAW_CARD);
        LinkedList<Card> deck = new LinkedList<>();
        Card drew = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        deck.add(drew);
        deck.add(new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED));
        deck.add(new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED));
        assertDoesNotThrow(()->player.drawCard(deck));
        assertEquals(player.getCardInHand()[0], drew);
        assertNotEquals(player.getCardInHand()[0], deck.getFirst());
    }
    /**
     *Checks that a player can't draw a card if the deck is empty.
     * The test also checks that before and after the drawCard method call the player hand doesn't change.
     */
    @Test
    @DisplayName("Cannot Draw - Empty Deck")
    public void EmptyTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.DRAW_CARD);
        LinkedList<Card> deck = new LinkedList<>();
        Card a = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card b = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        player.addInHand(a);
        player.addInHand(b);
        assertThrows(EmptyException.class, () -> player.drawCard(deck));
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        assertNull(player.getCardInHand()[2]);
    }

    /**
     * Checks that the player cannot draw if he has 3 cards in hand
     */
    @Test
    @DisplayName("Cannot Draw - Full Hand")
    public void FullHandTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.DRAW_CARD);
        LinkedList<Card> deck = new LinkedList<>();
        Card a = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card b = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card d = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), null, new Corner(Symbols.EMPTY) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card e = new Card(  new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        //Full player's hand
        player.addInHand(a);
        player.addInHand(b);
        player.addInHand(d);
        //Populate the deck
        deck.add(e);
        //Trying to draw a card but player's hand is full
        assertThrows(FullHandException.class, () -> player.drawCard(deck));
        //Checking that Card e isn't in the player's hand
        for (Card card : player.getCardInHand()){
            assertNotEquals(card, e);
        }
        //Player's hand hasn't changed after drawing
        assertEquals(player.getCardInHand()[0], a);
        assertEquals(player.getCardInHand()[1], b);
        assertEquals(player.getCardInHand()[2], d);
    }

    /**
     * Checks the player cannot draw if he is not in turn, or he hasn't already played a card
     */
    @Test
    @DisplayName("Cannot Draw - Not in Turn/Place Card")
    public void NotInTurnTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.NOT_IN_TURN);
        LinkedList<Card> deck = new LinkedList<>();
        Card a = new Card(  new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card b = new Card(  new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        deck.add(a);
        player.addInHand(b);
        assertThrows(NotInTurnException.class, () -> player.drawCard(deck));
        assertEquals(deck.getFirst(), a);
        assertNotEquals(player.getCardInHand()[1], a);
        player.setPlayerState(PlayerState.PLAY_CARD);
        assertThrows(NotInTurnException.class, () -> player.drawCard(deck));
        assertEquals(deck.getFirst(), a);
        assertNotEquals(player.getCardInHand()[1], a);
    }
}