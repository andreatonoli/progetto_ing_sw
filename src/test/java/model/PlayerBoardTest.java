package model;

import Controller.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;

public class PlayerBoardTest {
    @Test
    @DisplayName("Assigning Starter Card")
    public void PlayerStarterCardTest() throws IOException {
        Game game = new Game();
        Player player = new Player("Ugo", game.getGameBoard(), game);
        //game.addPlayer(player);
        //game.startGame();
        StarterCard a = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        player.getPlayerBoard().setStarterCard(a);
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.INSECT));
        assertEquals(a.card_number, player.getPlayerBoard().getCard(new int[]{0,0}).card_number);
    }
    @Test
    @DisplayName("Assigning a Flipped Starter Card")
    public void PlayerFlippedStarterCardTest() throws IOException{
        Game game = new Game();
        Player player = new Player("pippo", game.getGameBoard(), game);
        Controller c = new Controller(game);
        StarterCard a = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        c.flipCard(a);
        player.getPlayerBoard().setStarterCard(a);
        assertEquals(2, player.getPlayerBoard().getSymbolCount().get(Symbols.FUNGI));
        assertNull(player.getPlayerBoard().getSymbolCount().get(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount().get(Symbols.ANIMAL));
        assertNull(player.getPlayerBoard().getSymbolCount().get(Symbols.INSECT));
        assertEquals(a.card_number, player.getPlayerBoard().getCard(new int[]{0,0}).card_number);
    }
}
