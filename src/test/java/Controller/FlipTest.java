package Controller;

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
import model.player.Player;
import network.server.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
//TODO: rifare da 0 con mockito
//spy(), mock(), verify()
//public class FlipTest {
//    @Test
//    @DisplayName("Flip a card")
//    public void FlipCardTest() throws IOException {
//        Server server = mock(new Server());
//        Controller co = spy(new Controller(4));
//        verify(server, )
//        Controller c = new Controller(4);
//        Game game = c.getGame();
//        Player player = new Player("pippo", game);
//        player.setPlayerState(PlayerState.DRAW_CARD);
//        //First card of the deck
//        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
//        c.drawCard(player, game.getGameBoard().getResourceDeck());
//        assertEquals(a.getColor(), player.getCardInHand()[0].getColor());
//        for (CornerEnum co : CornerEnum.values()) {
//            assertEquals(a.getCornerSymbol(co), player.getCardInHand()[0].getCornerSymbol(co));
//        }
//        c.flipCard(null, player.getCardInHand()[0]); //Setto current side a retro
//        assertEquals(a.getColor(), player.getCardInHand()[0].getColor());
//        assertNotNull(player.getCardInHand()[0].getSymbols());
//        assertEquals(Symbols.PLANT, player.getCardInHand()[0].getSymbols().getFirst());
//        for (CornerEnum co : CornerEnum.values()) {
//            assertEquals(Symbols.EMPTY, player.getCardInHand()[0].getCornerSymbol(co));
//        }
//    }
//
//    @Test
//    @DisplayName("Flip and Place a Card")
//    public void FlipPlaceTest() throws IOException{
//        Controller c = new Controller(4);
//        Game game = c.getGame();
//        Player player = new Player("pippo", game);
//        //a is a green card => has a permanent plant symbol on its back
//        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
//        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
//        player.setPlayerState(PlayerState.PLAY_CARD);
//        player.getPlayerBoard().setStarterCard(s);
//        player.addInHand(a);
//        c.flipCard(null, a);
//        c.placeCard(player, a, new int[]{0,0}, CornerEnum.BL);
//        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
//        //Back points = 0 (always)
//        assertEquals(0, player.getPoints());
//        assertEquals(2, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
//        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
//    }
//
//    @Test
//    @DisplayName("Flip StarterCard and Not Valid Placement")
//    public void FlipStarterNotValidPlacement() throws IOException{
//        Game game = new Game(4);
//        Player player = new Player("pippo", game);
//        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
//        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY)}));
//        player.setPlayerState(PlayerState.PLAY_CARD);
//        c.flipCard(null, s);
//        player.getPlayerBoard().setStarterCard(s);
//        //Check if starterCard was correctly flipped
//        assertEquals(Symbols.ANIMAL, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.TL));
//        assertEquals(Symbols.EMPTY, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.TR));
//        assertEquals(Symbols.NOCORNER, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.BR));
//        assertEquals(Symbols.EMPTY, player.getPlayerBoard().getCard(new int[]{0,0}).getCornerSymbol(CornerEnum.BL));
//        assertNotNull(player.getPlayerBoard().getCard(new int[]{0,0}).getSymbols());
//        assertEquals(Symbols.FUNGI, player.getPlayerBoard().getCard(new int[]{0,0}).getSymbols().getFirst());
//        //BR corner of s retro isn't available
//        c.placeCard(player, a, new int[]{0,0}, CornerEnum.BR);
//        //Check if the card was not placed
//        assertEquals(1, player.getPlayerBoard().getCardPosition().size());
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
//        assertNull(player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
//        assertNull(player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
//    }
//}
//