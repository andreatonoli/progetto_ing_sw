package Controller;

import model.*;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
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
        Player player = new Player("pippo", game.getGameBoard());
        Controller c = new Controller(game);
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        c.placeCard(player, a, new int[]{0,0,}, CornerEnum.TR);
        assertEquals(player.getPlayerBoard().getCardPosition().get(new int[]{1,1}), a);
        assertEquals(player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI), 2);
        assertNull(player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
    }
}
