package model;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.exceptions.CostNotSatisfiedException;
import it.polimi.ingsw.model.exceptions.NotInTurnException;
import it.polimi.ingsw.model.exceptions.OccupiedCornerException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
public class PlaceTest{
    private Game game;
    @Test
    @DisplayName("Place a card")
    public void PlaceACardTest() {
        game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.PLAY_CARD);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 1);
        player.getPlayerBoard().setStarterCard(s);
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertDoesNotThrow(()-> player.placeCard(a, new int[] {0,0}));
        assertTrue(player.getPlayerBoard().getCardPosition().containsValue(a));
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size()); //true if both the card were successfully added to the player board
        assertEquals(3, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI)); //checks if card symbols were successfully added to the symbol count
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL)); //if true placeCard() has successfully covered the top-right corner
        assertEquals(1, player.getPoints());
    }

    @Test
    @DisplayName("Cannot Place - Not in turn or Already Placed")
    public void NotInTurnTest() {
        game = new Game(4);
        Player player = new Player("pippo", game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        player.setPlayerState(PlayerState.NOT_IN_TURN);
        player.getPlayerBoard().setStarterCard(s);
        int Bfungi = player.getPlayerBoard().getSymbolCount(Symbols.FUNGI);
        int Banimal = player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL);
        assertThrows(NotInTurnException.class, ()-> player.placeCard(a, new int[]{0,0}));
        //true only if nothing had changed (so the card wasn't placed)
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(a));
        assertEquals(1, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPoints());
        player.setPlayerState(PlayerState.DRAW_CARD);
        assertThrows(NotInTurnException.class, ()-> player.placeCard(a, new int[]{0,0}));
        //true only if nothing had changed (so the card wasn't placed)
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(a));
        assertEquals(1, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPoints());
    }

    @Test
    @DisplayName("Cannot Place - Trying to place over a NOCORNER")
    public void PlaceOnNoCornerTest() {
        game = new Game(4);
        Player player = new Player("pippo", game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.NOTHING, 17, new Integer[]{1, 0, 0, 0}, null);
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        assertDoesNotThrow(()-> player.placeCard(a, new int[]{0,0}));
        //Check if the placement was correctly done
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(0, player.getPoints());
        int Bfungi = player.getPlayerBoard().getSymbolCount(Symbols.FUNGI);
        int Banimal = player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL);
        int Bplant = player.getPlayerBoard().getSymbolCount(Symbols.PLANT);
        int Binsect = player.getPlayerBoard().getSymbolCount(Symbols.INSECT);
        int Bpoint = player.getPoints();
        //a's BR corner is hidden, so we cannot place the b card on that spot
        assertThrows(OccupiedCornerException.class, ()-> player.placeCard(b, new int[]{1,1}));
        //Check if the card was not placed => nothing changed
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(b));
        assertNull(player.getPlayerBoard().getCard(new int[]{2,0}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(Bplant, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(Binsect, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(Bpoint, player.getPoints());
    }

    @Test
    @DisplayName("Cannot Place - Covers a NOCORNER")
    public void CoversNoCornerTest() {
        game = new Game(4);
        Player player = new Player("pippo", game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}, 3, Condition.NOTHING, 17, new Integer[]{1, 0, 0, 0}, null);
        ResourceCard d = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER) }, 19, 1);
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        assertDoesNotThrow(()->{
            player.placeCard(a, new int[]{0,0});
            player.placeCard(b, new int[]{0,0});
        });
        //check the correct placement of the 2 cards
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(b, player.getPlayerBoard().getCard(new int[]{1,-1}));
        assertEquals(3, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(3, player.getPoints());
        int Bfungi = player.getPlayerBoard().getSymbolCount(Symbols.FUNGI);
        int Banimal = player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL);
        int Bplant = player.getPlayerBoard().getSymbolCount(Symbols.PLANT);
        int Binsect = player.getPlayerBoard().getSymbolCount(Symbols.INSECT);
        int Bsize = player.getPlayerBoard().getPositionCardKeys().size();
        int Bpoint = player.getPoints();
        //a's BR corner is hidden, so we cannot place the b card on that spot
        assertThrows(OccupiedCornerException.class, ()-> player.placeCard(d, new int[]{1,-1}));
        //Check if the card was not placed => nothing changed
        assertNull(player.getPlayerBoard().getCard(new int[]{2,0}));
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(d));
        assertEquals(Bsize, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(Bplant, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(Binsect, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(Bpoint, player.getPoints());
    }

    @Test
    @DisplayName("Cannot Place - Cost not satisfied")
    public void CostTest() {
        game = new Game(4);
        int plantCost = 3;
        Player player = new Player("pippo", game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.NOTHING, 17, new Integer[]{0, plantCost, 0, 0}, null);
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        assertThrows(CostNotSatisfiedException.class, ()-> player.placeCard(b, new int[]{0,0}));
        //Check if the card was not placed
        assertEquals(1, player.getPlayerBoard().getCardPosition().size());
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(0, player.getPoints());
        assertTrue(player.getPlayerBoard().getSymbolCount(Symbols.PLANT) < plantCost);
    }
}