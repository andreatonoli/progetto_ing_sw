package model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.CardNotFoundException;
import it.polimi.ingsw.model.exceptions.FullHandException;
import it.polimi.ingsw.model.exceptions.NotInTurnException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class DrawFromBoardTest {

    private Game game;

    private Player p;
    @BeforeEach
    public void setup() {
        game = new Game(4);
        p = new Player("pippo", game);
    }
    //TODO: che succede quando pesco dalle carte a terra ma i mazzi sono vuoti?
    @Test
    @DisplayName("Take Resource from Board")
    public void takeResource(){
        Card rCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        Card rCard2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        p.setPlayerState(PlayerState.DRAW_CARD);
        //Player's hand is empty, so he can take one card from the board
        game.getGameBoard().setCommonResource(rCard, 0);
        game.getGameBoard().setCommonResource(rCard2, 1);
        assertDoesNotThrow(() -> p.drawCardFromBoard(rCard));
        assertEquals(rCard, p.getCardInHand()[0]);
        assertNotEquals(rCard, game.getGameBoard().getCommonResource()[0]);
        //Check if the card was replaced with another resource card
        assertEquals("resource", game.getGameBoard().getCommonResource()[0].getType());
        //Drawing also the other card
        assertDoesNotThrow(() -> p.drawCardFromBoard(rCard2));
        assertEquals(rCard2, p.getCardInHand()[1]);
        assertNotEquals(rCard2, game.getGameBoard().getCommonResource()[1]);
        //Check if the card was replaced with another resource card
        assertEquals("resource", game.getGameBoard().getCommonResource()[1].getType());
    }
    @Test
    @DisplayName("Take Gold from Board")
    public void takeGold(){
        Card gCard = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, Condition.NOTHING, 1, new Integer[]{0,0,0,0}, null);
        Card gCard2 = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER)}, 2, Condition.NOTHING, 2, new Integer[]{0,0,0,0}, null);
        p.setPlayerState(PlayerState.DRAW_CARD);
        //player hand is empty, and he is in draw_card state, so he can draw from the board
        game.getGameBoard().setCommonGold(gCard, 0);
        game.getGameBoard().setCommonGold(gCard2, 1);
        //Check if the player can draw the first common gold card
        assertDoesNotThrow(() -> p.drawCardFromBoard(gCard));
        assertEquals(gCard, p.getCardInHand()[0]);
        assertNotEquals(gCard, game.getGameBoard().getCommonGold()[0]);
        //Check if the card was replaced with another gold card
        assertEquals("gold", game.getGameBoard().getCommonGold()[0].getType());
        //Drawing also the other gold
        assertDoesNotThrow(() -> p.drawCardFromBoard(gCard2));
        assertEquals(gCard2, p.getCardInHand()[1]);
        assertNotEquals(gCard2, game.getGameBoard().getCommonGold()[1]);
        //Check if the card was replaced with another gold card
        assertEquals("gold", game.getGameBoard().getCommonGold()[1].getType());
    }

    @Test
    @DisplayName("Cannot Draw From Bord - Not in Turn")
    public void notInTurnTest() {
        Card rCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        Card rCard2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        Card gCard = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, Condition.NOTHING, 1, new Integer[]{0,0,0,0}, null);
        Card gCard2 = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER)}, 2, Condition.NOTHING, 2, new Integer[]{0,0,0,0}, null);
        p.setPlayerState(PlayerState.NOT_IN_TURN);
        //Placing the cards on the game board
        game.getGameBoard().setCommonResource(rCard, 0);
        game.getGameBoard().setCommonResource(rCard2, 1);
        game.getGameBoard().setCommonGold(gCard, 0);
        game.getGameBoard().setCommonGold(gCard2, 1);
        //Trying to draw the first resource card
        assertThrows(NotInTurnException.class, () -> p.drawCardFromBoard(rCard));
        assertEquals(rCard, game.getGameBoard().getCommonResource()[0]);
        assertEquals(rCard2, game.getGameBoard().getCommonResource()[1]);
        //Trying to draw the first gold card
        assertThrows(NotInTurnException.class, () -> p.drawCardFromBoard(gCard));
        assertEquals(gCard, game.getGameBoard().getCommonGold()[0]);
        assertEquals(gCard2, game.getGameBoard().getCommonGold()[1]);
        //Now it's player's turn, but he has not placed a card yet
        p.setPlayerState(PlayerState.PLAY_CARD);
        //Trying to draw the first resource card
        assertThrows(NotInTurnException.class, () -> p.drawCardFromBoard(rCard));
        assertEquals(rCard, game.getGameBoard().getCommonResource()[0]);
        assertEquals(rCard2, game.getGameBoard().getCommonResource()[1]);
        //Trying to draw the first gold card
        assertThrows(NotInTurnException.class, () -> p.drawCardFromBoard(gCard));
        assertEquals(gCard, game.getGameBoard().getCommonGold()[0]);
        assertEquals(gCard2, game.getGameBoard().getCommonGold()[1]);
    }

    @Test
    @DisplayName("Cannot Draw From Board - Card not found")
    public void cardNotFound() {
        //Card on the board
        Card rCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        Card rCard2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        Card gCard = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, Condition.NOTHING, 1, new Integer[]{0,0,0,0}, null);
        Card gCard2 = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER)}, 2, Condition.NOTHING, 2, new Integer[]{0,0,0,0}, null);
        //External cards
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.NOCORNER), null, new Corner(Symbols.FUNGI) }, 5, 0);
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.NOTHING, 17, new Integer[]{0, 0, 0, 0}, null);
        //Player can draw
        p.setPlayerState(PlayerState.DRAW_CARD);
        //Placing the cards on the game board
        game.getGameBoard().setCommonResource(rCard, 0);
        game.getGameBoard().setCommonResource(rCard2, 1);
        game.getGameBoard().setCommonGold(gCard, 0);
        game.getGameBoard().setCommonGold(gCard2, 1);
        //Trying to draw a which is not on the board
        assertThrows(CardNotFoundException.class, () -> p.drawCardFromBoard(a));
        assertEquals(rCard, game.getGameBoard().getCommonResource()[0]);
        assertEquals(rCard2, game.getGameBoard().getCommonResource()[1]);
        //Trying to draw b which is not among the common gold cards
        assertThrows(CardNotFoundException.class, () -> p.drawCardFromBoard(b));
        assertEquals(gCard, game.getGameBoard().getCommonGold()[0]);
        assertEquals(gCard2, game.getGameBoard().getCommonGold()[1]);
    }

    @Test
    @DisplayName("Cannot Draw From Board - Full hand")
    public void fullHandTest() {
        //Card on the board
        Card rCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        Card rCard2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        Card gCard = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, Condition.NOTHING, 1, new Integer[]{0,0,0,0}, null);
        Card gCard2 = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER)}, 2, Condition.NOTHING, 2, new Integer[]{0,0,0,0}, null);
        //Player's hand
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.NOCORNER), null, new Corner(Symbols.FUNGI) }, 5, 0);
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.NOTHING, 17, new Integer[]{0, 0, 0, 0}, null);
        ResourceCard c = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER) }, 19, 1);
        //Player can draw
        p.setPlayerState(PlayerState.DRAW_CARD);
        //Placing the cards on the game board
        game.getGameBoard().setCommonResource(rCard, 0);
        game.getGameBoard().setCommonResource(rCard2, 1);
        game.getGameBoard().setCommonGold(gCard, 0);
        game.getGameBoard().setCommonGold(gCard2, 1);
        //Filling player's hand
        p.addInHand(a);
        p.addInHand(b);
        p.addInHand(c);
        //Trying to draw the first resource card
        assertThrows(FullHandException.class, () -> p.drawCardFromBoard(rCard));
        assertEquals(rCard, game.getGameBoard().getCommonResource()[0]);
        assertEquals(rCard2, game.getGameBoard().getCommonResource()[1]);
        //Trying to draw the first gold card
        assertThrows(FullHandException.class, () -> p.drawCardFromBoard(gCard));
        assertEquals(gCard, game.getGameBoard().getCommonGold()[0]);
        assertEquals(gCard2, game.getGameBoard().getCommonGold()[1]);
    }
}
