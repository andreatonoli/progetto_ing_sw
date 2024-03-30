package Controller;

import model.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
public class FlipTest {
    @Test
    @DisplayName("Flip a card")
    public void FlipCardTest() throws IOException{
        Game game = new Game();
        Player player = new Player("pippo", game.getGameBoard(), game);
        player.setPlayerState(PlayerState.DRAW_CARD);
        Controller c = new Controller(game);
        //First card of the deck
        ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER)}, 19, 1);
        c.drawCard(player, game.getGameBoard().getResourceDeck());
        assertEquals(a.getColor(), player.getCardInHand()[0].getColor());
        for (CornerEnum co : CornerEnum.values()){
            assertEquals(a.getCornerSymbol(co), player.getCardInHand()[0].getCornerSymbol(co));
        }

    }
}
