package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import Controller.Controller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
public class AchievementTest {
    @Test
    @DisplayName("Diagonal Achievement")
    public void diagonalTest() throws IOException{
        Game game = new Game();
        Achievement a = new AchievementDiagonal(Color.PURPLE);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER), new Corner(Symbols.INKWELL) }, 37, 0);
        Player p = new Player("pippo", game);
        p.getPlayerBoard().setCardPosition(c, new int[]{-40,-40});
        p.getPlayerBoard().setCardPosition(c, new int[]{-39,-41});
        p.getPlayerBoard().setCardPosition(c, new int[]{-41,-39});
        a.calcPoints(p);
        assertEquals(2, p.getPoints());
    }

    @Test
    @DisplayName("Diagonal Achievement - No Pattern found")
    public void noDiagonalTest() throws IOException{
        Game game = new Game();
        Achievement a = new AchievementDiagonal(Color.RED);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER), new Corner(Symbols.INKWELL) }, 37, 0);
        Player p = new Player("pippo", game);
        int offset = 5; //Added some points to the player to see if calcPoints adds 0 points
        //Added some cards to the playerboard. None of them is red
        p.getPlayerBoard().setCardPosition(c, new int[]{0,0});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1,1});
        p.getPlayerBoard().setCardPosition(c, new int[]{1,-1});
        p.addPoints(offset);
        a.calcPoints(p); //should not add any points to p
        assertEquals(offset, p.getPoints());
    }

    @Test
    @DisplayName("Diagonal Achievement - No Card Reuse")
    public void noReuseTest() throws IOException{
        Game game = new Game();
        Achievement a = new AchievementDiagonal(Color.PURPLE);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER), new Corner(Symbols.INKWELL) }, 37, 0);
        Player p = new Player("pippo", game);
        p.getPlayerBoard().setCardPosition(c, new int[]{0,0});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1,1});
        p.getPlayerBoard().setCardPosition(c, new int[]{1,-1});
        p.getPlayerBoard().setCardPosition(c, new int[]{2,-2});
        p.getPlayerBoard().setCardPosition(c, new int[]{3,-3});
        p.getPlayerBoard().setCardPosition(c, new int[]{4,-4});
        a.calcPoints(p);
        assertEquals(4, p.getPoints());
    }
    //TODO: Imparare a calcolare un minimo
    @Test
    @DisplayName("Item Achievement")
    public void itemTest() throws IOException{
        Game game = new Game();
        Player p = new Player("pippo", game);
        Controller co = new Controller(game);
        Achievement a = new AchievementItem(3, new ArrayList<>(List.of(Symbols.INKWELL, Symbols.QUILL, Symbols.MANUSCRIPT)));
        Card b = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.INKWELL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY) }, 14, 0);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        Card d = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INKWELL), new Corner(Symbols.NOCORNER) }, 16, 0);
        Card e = new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.QUILL), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0);
        Card f = new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 18, 1);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        //Place some cards
        p.getPlayerBoard().setStarterCard(s);
        co.placeCard(p, b, new int[]{0,0}, CornerEnum.TL);
        co.placeCard(p, c, new int[]{0,0}, CornerEnum.TR);
        co.placeCard(p, d, new int[]{0,0}, CornerEnum.BL);
        co.placeCard(p, e, new int[]{0,0}, CornerEnum.BR);
        co.placeCard(p, b, new int[]{-1,1}, CornerEnum.BL);
        //Number of items:
        //  Manuscript: 3
        //  Inkwell: 4
        //  Quill: 4
        a.calcPoints(p);
        assertEquals(9, p.getPoints());
    }
}
