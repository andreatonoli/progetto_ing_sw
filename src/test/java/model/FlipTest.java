package model;

import model.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import model.card.CardBack;
import model.card.Corner;
import model.card.ResourceCard;
import model.card.StarterCard;
import model.enums.Color;
import model.enums.CornerEnum;
import model.enums.PlayerState;
import model.enums.Symbols;
import model.exceptions.AlreadyUsedPositionException;
import model.exceptions.CostNotSatisfiedException;
import model.exceptions.NotInTurnException;
import model.exceptions.OccupiedCornerException;
import model.player.Player;
import network.server.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;


public class FlipTest {
    @Test
    @DisplayName("Flip a card")
    public void FlipCardTest() throws IOException {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        player.setPlayerState(PlayerState.DRAW_CARD);
        //First card of the deck
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
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
        assertEquals(Symbols.PLANT, player.getCardInHand()[0].getSymbols().getFirst());
        for (CornerEnum co : CornerEnum.values()) {
            assertEquals(Symbols.EMPTY, player.getCardInHand()[0].getCornerSymbol(co));
        }
    }

    @Test
    @DisplayName("Flip and Place a Card")
    public void FlipPlaceTest() throws OccupiedCornerException, NotInTurnException, AlreadyUsedPositionException, CostNotSatisfiedException {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        //a is a green card => has a permanent plant symbol on its back
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        player.addInHand(a);
        a.setCurrentSide();
        player.placeCard(a, new int[]{0,0}, CornerEnum.BL);
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
        //Back points = 0 (always)
        assertEquals(0, player.getPoints());
        assertEquals(2, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
    }

    @Test
    @DisplayName("Flip StarterCard and Not Valid Placement")
    public void FlipStarterNotValidPlacement() throws IOException{
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY)}));
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
        try {
            player.placeCard(a, new int[]{0,0}, CornerEnum.BR);
        } catch (OccupiedCornerException | NotInTurnException | AlreadyUsedPositionException |
                 CostNotSatisfiedException e) {
            System.err.println(e.getMessage());
        }
        //Check if the card was not placed
        assertEquals(1, player.getPlayerBoard().getCardPosition().size());
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
    }
}
