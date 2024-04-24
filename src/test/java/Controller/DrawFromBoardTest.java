//package Controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//import model.*;
//import model.card.Card;
//import model.card.Corner;
//import model.card.GoldCard;
//import model.card.ResourceCard;
//import model.enums.Condition;
//import model.enums.PlayerState;
//import model.enums.Symbols;
//import model.player.Player;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//
//public class DrawFromBoardTest {
//    @Test
//    @DisplayName("Take Resource from Board")
//    public void takeResource(){
//        Controller c = new Controller(4);
//        Game game = c.getGame();
//        Player p = new Player("pippo", game);
//        Card rCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0);
//        Card rCard2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0);
//        p.setPlayerState(PlayerState.DRAW_CARD);
//        //Player's hand is empty, so he can take one card from the board
//        game.getGameBoard().setCommonResource(rCard, 0);
//        game.getGameBoard().setCommonResource(rCard2, 1);
//        c.drawCardFromBoard(p, rCard);
//        assertEquals(rCard, p.getCardInHand()[0]);
//        assertNotEquals(rCard, game.getGameBoard().getCommonResource()[0]);
//    }
//    @Test
//    @DisplayName("Take Gold from Board")
//    public void takeGold(){
//        Controller c = new Controller(4);
//        Game game = c.getGame();
//        Player p = new Player("pippo", game);
//        Card gCard = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, Condition.NOTHING, 1, null, null);
//        Card gCard2 = new GoldCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, Condition.NOTHING, 2, null, null);
//        p.setPlayerState(PlayerState.DRAW_CARD);
//        //player hand is empty, and he is in draw_card state, so he can draw from the board
//    }
//}
//