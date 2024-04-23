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
        Game game = new Game(4);
        Player player = new Player("Ugo", game);
        //game.addPlayer(player);
        //game.startGame(4);
        StarterCard a = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        player.getPlayerBoard().setStarterCard(a);
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(a.card_number, player.getPlayerBoard().getCard(new int[]{0,0}).card_number);
    }
    @Test
    @DisplayName("Assigning a Flipped Starter Card")
    public void PlayerFlippedStarterCardTest() throws IOException{
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        Controller c = new Controller(game);
        StarterCard a = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        c.flipCard(a);
        player.getPlayerBoard().setStarterCard(a);
        assertEquals(2, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertNull(player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertNull(player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(a.card_number, player.getPlayerBoard().getCard(new int[]{0,0}).card_number);
    }
}
