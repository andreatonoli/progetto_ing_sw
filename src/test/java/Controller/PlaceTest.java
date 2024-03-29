package Controller;

import model.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;

public class PlaceTest{
    private Game game;
    @Test
    @DisplayName("Place a card")
    public void PlaceACardTest() throws IOException{
        game = new Game();
        Player player = new Player("pippo", game.getGameBoard(), game);
        player.setPlayerState(PlayerState.PLAY_CARD);
        Controller c = new Controller(game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        player.getPlayerBoard().setStarterCard(s);
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        c.placeCard(player, a, new int[] {0,0}, CornerEnum.TR);
        assertTrue(player.getPlayerBoard().getCardPosition().containsValue(a));
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size()); //true if both the card were successfully added to the player board
        assertEquals(3, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI)); //checks if card symbols were successfully added to the symbol count
        assertEquals(0, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL)); //if true placeCard() has successfully covered the top-right corner
    }

    @Test
    @DisplayName("Cannot Place - Not in turn or Already Placed")
    public void NotInTurnTest() throws IOException{
        game = new Game();
        Controller c = new Controller(game);
        Player player = new Player("pippo", game.getGameBoard(), game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        player.setPlayerState(PlayerState.NOT_IN_TURN);
        player.getPlayerBoard().setStarterCard(s);
        int Bfungi = player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI);
        int Banimal = player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL);
        c.placeCard(player, a, new int[]{0,0}, CornerEnum.TL);
        //true only if nothing had changed (so the card wasn't placed)
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(a));
        assertEquals(1, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
        player.setPlayerState(PlayerState.DRAW_CARD);
        c.placeCard(player, a, new int[]{0,0}, CornerEnum.TL);
        //true only if nothing had changed (so the card wasn't placed)
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(a));
        assertEquals(1, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
    }

    @Test
    @DisplayName("Cannot Place - Trying to place over a NOCORNER")
    public void PlaceOnNoCornerTest() throws IOException{
        game = new Game();
        Controller c = new Controller(game);
        Player player = new Player("pippo", game.getGameBoard(), game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, null, 17, new int[]{0, 3, 0, 0});
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        c.placeCard(player, a, new int[]{0,0}, CornerEnum.TR);
        //Check if the placement was correctly done
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
        int Bfungi = player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI);
        int Banimal = player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL);
        int Bplant = player.getPlayerBoard().getSymbolCount().get(Symbols.PLANT);
        int Binsect = player.getPlayerBoard().getSymbolCount().get(Symbols.EMPTY);
        //a's BR corner is hidden, so we cannot place the b card on that spot
        c.placeCard(player, b, new int[]{1,1}, CornerEnum.BR);
        //Check if the card was not placed => nothing changed
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(b));
        assertNull(player.getPlayerBoard().getCard(new int[]{2,0}));
        assertEquals(2, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
        assertEquals(Bplant, player.getPlayerBoard().getSymbolCount().get(Symbols.PLANT));
        assertEquals(Binsect, player.getPlayerBoard().getSymbolCount().get(Symbols.INSECT));
    }

    @Test
    @DisplayName("Cannot Place - Covers a NOCORNER")
    public void CoversNoCornerTest() throws IOException{
        game = new Game();
        Controller c = new Controller(game);
        Player player = new Player("pippo", game.getGameBoard(), game);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 0);
        GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}, 3, null, 17, new int[]{0, 3, 0, 0});
        ResourceCard d = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER) }, 19, 1);
        player.setPlayerState(PlayerState.PLAY_CARD);
        player.getPlayerBoard().setStarterCard(s);
        c.placeCard(player, a, new int[]{0,0}, CornerEnum.TR);
        c.placeCard(player, b, new int[]{0,0}, CornerEnum.BR);
        //check the correct placement of the 2 cards
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(b, player.getPlayerBoard().getCard(new int[]{1,-1}));
        assertEquals(3, player.getPlayerBoard().getPositionCardKeys().size());
        int Bfungi = player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI);
        int Banimal = player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL);
        int Bplant = player.getPlayerBoard().getSymbolCount().get(Symbols.PLANT);
        int Binsect = player.getPlayerBoard().getSymbolCount().get(Symbols.INSECT);
        int Bsize = player.getPlayerBoard().getPositionCardKeys().size();
        //a's BR corner is hidden, so we cannot place the b card on that spot
        c.placeCard(player, d, new int[]{1,-1}, CornerEnum.TR);
        //Check if the card was not placed => nothing changed
        assertNull(player.getPlayerBoard().getCard(new int[]{2,0}));
        assertFalse(player.getPlayerBoard().getCardPosition().containsValue(d));
        assertEquals(Bsize, player.getPlayerBoard().getPositionCardKeys().size());
        assertEquals(Bfungi, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        assertEquals(Banimal, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
        assertEquals(Bplant, player.getPlayerBoard().getSymbolCount().get(Symbols.PLANT));
        assertEquals(Binsect, player.getPlayerBoard().getSymbolCount().get(Symbols.INSECT));
    }
}
