package model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
import java.util.List;

public class PlayerBoardTest {
    @Test
    @DisplayName("Assigning Starter Card")
    public void PlayerStarterCardTest() {
        Game game = new Game(4);
        Player player = new Player("Ugo", game);
        Card a = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
        player.getPlayerBoard().setStarterCard(a);
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(a.getCardNumber(), player.getPlayerBoard().getCard(new int[]{0,0}).getCardNumber());
    }
    @Test
    @DisplayName("Assigning a Flipped Starter Card")
    public void PlayerFlippedStarterCardTest() {
        Game game = new Game(4);
        Player player = new Player("pippo", game);
        Card a = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.FUNGI), new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}), "starter", 1, Color.WHITE);
        a.setCurrentSide();
        player.getPlayerBoard().setStarterCard(a);
        assertEquals(2, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
        assertEquals(a.getCardNumber(), player.getPlayerBoard().getCard(new int[]{0,0}).getCardNumber());
    }
}