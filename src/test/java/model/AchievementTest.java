package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
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
        p.getPlayerBoard().setCardPosition(c, new int[]{0,0});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1,1});
        p.getPlayerBoard().setCardPosition(c, new int[]{1,-1});
        a.calcPoints(p);
        assertEquals(2, p.getPoints());
    }
}
