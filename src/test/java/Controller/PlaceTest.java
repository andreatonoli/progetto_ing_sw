package Controller;

import model.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
import java.util.LinkedList;

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
        player.getPlayerBoard().setCardPosition(s, new int[]{0,0});
        c.placeCard(player, a, new int[] {0,0}, CornerEnum.TR);
        assertEquals(a, player.getPlayerBoard().getCard(new int[]{1,1}));
        assertEquals(2, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        //assertNull(player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
    }
}
