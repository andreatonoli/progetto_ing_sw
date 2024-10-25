package model;

import it.polimi.ingsw.model.Game;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.exceptions.OccupiedCornerException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;


public class FlipTest {

    @Test
    @DisplayName("Flip a card")
    public void FlipCardTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.DRAW_CARD);
        //First card of the deck
        Card a = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        player.addInHand(a);
        //Checks that the card added in the player hand is really a
        assertEquals(a.getColor(), player.getCardInHand()[0].getColor());
        for (CornerEnum co : CornerEnum.values()) {
            assertEquals(a.getCornerSymbol(co), player.getCardInHand()[0].getCornerSymbol(co));
        }
        //flips the card
        player.getCardInHand()[0].setCurrentSide();
        assertEquals(a.getColor(), player.getCardInHand()[0].getColor());
        assertNotNull(player.getCardInHand()[0].getSymbols());
        assertEquals(1, player.getCardInHand()[0].getSymbols().size());
        assertEquals(Symbols.FUNGI, player.getCardInHand()[0].getSymbols().getFirst());
        for (CornerEnum co : CornerEnum.values()) {
            assertEquals(Symbols.EMPTY, player.getCardInHand()[0].getCornerSymbol(co));
        }

        //flip the card again
        player.getCardInHand()[0].setCurrentSide();
        assertEquals(a.getColor(), player.getCardInHand()[0].getColor());
        assertNull(player.getCardInHand()[0].getSymbols());
        for (CornerEnum co : CornerEnum.values()) {
            assertEquals(a.getCornerSymbol(co), player.getCardInHand()[0].getCornerSymbol(co));
        }
    }

    @Test
    @DisplayName("Flip and Place a Card")
    public void FlipPlaceTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        //a is a green card, so it has a permanent plant symbol on its back
        Card a = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 1), new CardBack(List.of(Symbols.PLANT)), "resource", 30, Color.RED);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        player.addInHand(a);
        a.setCurrentSide();
        assertDoesNotThrow(()-> player.placeCard(a, new int[]{-1,-1}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
        //Back points = 0 (always)
        assertEquals(0, player.getPoints());
        assertEquals(2, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
    }

    @Test
    @DisplayName("Flip StarterCard")
    public void FlipStarterCard(){
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.FUNGI), new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY)}), "starter", 1, Color.WHITE);
        //Flip a starter card with only one back symbol
        s.setCurrentSide();
        assertEquals(Symbols.ANIMAL,s.getCornerSymbol(CornerEnum.TL));
        assertEquals(Symbols.EMPTY,s.getCornerSymbol(CornerEnum.TR));
        assertEquals(Symbols.NOCORNER,s.getCornerSymbol(CornerEnum.BR));
        assertEquals(Symbols.EMPTY,s.getCornerSymbol(CornerEnum.BL));
        assertNotNull(s.getSymbols());
        assertEquals(1, s.getSymbols().size());
        assertEquals(Symbols.FUNGI, s.getSymbols().getFirst());
        //Flip a starterCard with more back symbols
        Card a = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}), "starter", 1, Color.WHITE);
        a.setCurrentSide();
        for (CornerEnum c : CornerEnum.values()){
            assertEquals(Symbols.EMPTY, a.getCornerSymbol(c));
        }
        assertNotNull(s.getSymbols());
        assertEquals(2, a.getSymbols().size());
        assertEquals(Symbols.ANIMAL, a.getSymbols().getFirst());
        assertEquals(Symbols.INSECT, a.getSymbols().get(1));
    }

    @Test
    @DisplayName("Flip StarterCard and Not Valid Placement")
    public void FlipStarterNotValidPlacement() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        Card a = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.FUNGI), new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY)}), "starter", 1, Color.WHITE);
        player.setPlayerState(PlayerState.PLAY_CARD);
        s.setCurrentSide();
        player.getPlayerBoard().setStarterCard(s);
        //Check if starterCard was correctly flipped
        assertEquals(Symbols.ANIMAL, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.TL));
        assertEquals(Symbols.EMPTY, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.TR));
        assertEquals(Symbols.NOCORNER, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.BR));
        assertEquals(Symbols.EMPTY, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.BL));
        assertNotNull(player.getPlayerBoard().getCard(new int[]{0,0}).getSymbols());
        assertEquals(Symbols.FUNGI, player.getPlayerBoard().getCard(new int[]{0,0}).getSymbols().getFirst());
        //BR corner of s retro isn't available
        assertThrows(OccupiedCornerException.class, ()-> player.placeCard(a, new int[]{1,-1}));
        //Check if the card was not placed
        assertEquals(1, player.getPlayerBoard().getCardPosition().size());
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
    }
}