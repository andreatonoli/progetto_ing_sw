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
        Player p = new Player("pippo", game);

    }
}
