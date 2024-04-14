package Controller;

import static org.junit.jupiter.api.Assertions.*;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
import java.util.LinkedList;
public class DrawFromBoardTest {
    @Test
    @DisplayName("Take Resource from Board")
    public void takeResource(){
        Game game = new Game(4);
        Controller c = new Controller(game);
        Player p = new Player("pippo", game);
        Card rCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
        Card rCard2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
        p.setPlayerState(PlayerState.DRAW_CARD);
        //Player's hand is empty, so he can take one card from the board
        game.getGameBoard().setCommonResource(rCard, 0);
        game.getGameBoard().setCommonResource(rCard2, 1);
        c.drawCardFromBoard(p, rCard);
        assertEquals(rCard, p.getCardInHand()[0]);
        assertNotEquals(rCard, game.getGameBoard().getCommonResource()[0]);
    }
}
