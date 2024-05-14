//package model;
//
//import static org.junit.jupiter.api.Assertions.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.model.card.CardBack;
//import it.polimi.ingsw.model.card.Corner;
//import it.polimi.ingsw.model.card.StarterCard;
//import it.polimi.ingsw.model.enums.Color;
//import it.polimi.ingsw.model.enums.Symbols;
//import it.polimi.ingsw.model.player.Player;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import java.io.IOException;
//
//public class PlayerBoardTest {
//    @Test
//    @DisplayName("Assigning Starter Card")
//    public void PlayerStarterCardTest() throws IOException {
//        Game game = new Game(4);
//        Player player = new Player("Ugo", game);
//        StarterCard a = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
//        player.getPlayerBoard().setStarterCard(a);
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.PLANT));
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.INSECT));
//        assertEquals(a.getCardNumber(), player.getPlayerBoard().getCard(new int[]{0,0}).getCardNumber());
//    }
//    @Test
//    @DisplayName("Assigning a Flipped Starter Card")
//    public void PlayerFlippedStarterCardTest() throws IOException{
//        Game game = new Game(4);
//        Player player = new Player("pippo", game);
//        StarterCard a = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
//        a.setCurrentSide();
//        player.getPlayerBoard().setStarterCard(a);
//        assertEquals(2, player.getPlayerBoard().getSymbolCount(Symbols.FUNGI));
//        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.PLANT)); //Perchè non sono null?
//        assertEquals(1, player.getPlayerBoard().getSymbolCount(Symbols.ANIMAL));
//        assertEquals(0, player.getPlayerBoard().getSymbolCount(Symbols.INSECT)); //Perchè non sono null?
//        assertEquals(a.getCardNumber(), player.getPlayerBoard().getCard(new int[]{0,0}).getCardNumber());
//    }
//}
