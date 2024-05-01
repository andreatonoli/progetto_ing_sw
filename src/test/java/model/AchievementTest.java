package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import Controller.Controller;
import model.card.*;
import model.enums.Color;
import model.enums.CornerEnum;
import model.enums.PlayerState;
import model.enums.Symbols;
import model.exceptions.AlreadyUsedPositionException;
import model.exceptions.CostNotSatisfiedException;
import model.exceptions.NotInTurnException;
import model.exceptions.OccupiedCornerException;
import model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
public class AchievementTest {
    @Test
    @DisplayName("Diagonal Achievement")
    public void diagonalTest() throws IOException{
        Game game = new Game(4);
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
        Game game = new Game(4);
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
        Game game = new Game(4);
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
    @Test
    @DisplayName("Item Achievement - 3 Items")
    public void itemTest() throws IOException{
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)));
        Card b = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.INKWELL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY) }, 14, 0);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        Card d = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INKWELL), new Corner(Symbols.NOCORNER) }, 16, 0);
        Card e = new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.QUILL), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0);
        Card f = new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 18, 1);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        //Place some cards
        p.getPlayerBoard().setStarterCard(s);
        try {
            p.placeCard(b, new int[]{0,0}, CornerEnum.TL);
            p.placeCard(c, new int[]{0,0}, CornerEnum.TR);
            p.placeCard(d, new int[]{0,0}, CornerEnum.BL);
            p.placeCard(e, new int[]{0,0}, CornerEnum.BR);
            p.placeCard(f, new int[]{-1,1}, CornerEnum.BL);
        } catch (OccupiedCornerException | NotInTurnException | AlreadyUsedPositionException |
                 CostNotSatisfiedException ex) {
            System.out.println(ex.getMessage());
        }
        //Number of items:
        //  Manuscript: 3
        //  Inkwell: 4
        //  Quill: 4
        a.calcPoints(p);
        //9 points from achievement and 1 point from f card
        assertEquals(10, p.getPoints());
    }
    @Test
    @DisplayName("Item Achievement - No Enough Items")
    public void noItemTest() throws IOException{
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementItem(3, new ArrayList<>(List.of(Symbols.INKWELL, Symbols.QUILL, Symbols.MANUSCRIPT)));
        Achievement b = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL)));
        Achievement c = new AchievementItem(2, new ArrayList<>(List.of(Symbols.QUILL)));
        Achievement d = new AchievementItem(2, new ArrayList<>(List.of(Symbols.MANUSCRIPT)));
        int offset = 5; //Adding some points to player
        p.addPoints(offset);
        //Not enough items to add points to player => his points will be equal to offset
        p.getPlayerBoard().increaseSymbolCount(Symbols.INKWELL);
        a.calcPoints(p);
        b.calcPoints(p);
        c.calcPoints(p);
        d.calcPoints(p);
        assertEquals(offset, p.getPoints());
    }

    @Test
    @DisplayName("Resources Achievement")
    public void resourcesTest() throws IOException{
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementResources(Symbols.FUNGI);
        Achievement b = new AchievementResources(Symbols.PLANT);
        Achievement c = new AchievementResources(Symbols.ANIMAL);
        Achievement d = new AchievementResources(Symbols.INSECT);
        //Define a bunch of cards to populate the board
        Card e = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY) }, 14, 0);
        Card f = new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        Card g = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER) }, 16, 0);
        Card h = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0);
        Card i = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 18, 0);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.getPlayerBoard().setStarterCard(s);
        //p can place card
        p.setPlayerState(PlayerState.PLAY_CARD);
        //place the cards
        try {
            p.placeCard(e, new int[]{0,0}, CornerEnum.TL);
            p.placeCard(f, new int[]{0,0}, CornerEnum.TR);
            p.placeCard(g, new int[]{0,0}, CornerEnum.BL);
            p.placeCard(h, new int[]{0,0}, CornerEnum.BR);
            p.placeCard(i, new int[]{1,1}, CornerEnum.TL);
        } catch (OccupiedCornerException | NotInTurnException | AlreadyUsedPositionException |
                 CostNotSatisfiedException ex) {
            System.out.println(ex.getMessage());
        }
        //Number of not covered symbols:
        //Fungi: 4 => 2 point
        a.calcPoints(p);
        assertEquals(2, p.getPoints());
        //Plant: 5 => 2 points
        b.calcPoints(p);
        assertEquals(4, p.getPoints());
        //Animal: 2 => 0 point
        c.calcPoints(p);
        assertEquals(4, p.getPoints());
        //Insect: 2 => 0 points
        d.calcPoints(p);
        assertEquals(4, p.getPoints());
        //Total : 4 points
        assertEquals(4, p.getPoints());
    }

    @Test
    @DisplayName("L Achievement")
    public void lTest() throws IOException{
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementL(Color.RED);
        Achievement b = new AchievementL(Color.BLUE);
        Achievement c = new AchievementL(Color.GREEN);
        Achievement d = new AchievementL(Color.PURPLE);
        //Define 2 red cards
        Card r1 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 7, 0);
        Card r2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 8, 0);
        //Define 2 green cards
        Card g1 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY) }, 14, 0);
        Card g2 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 15, 0);
        //Define 2 blue cards
        Card b1 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 24, 0);
        Card b2 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 27, 0);
        //Define 2 purple cards
        Card p1 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 32, 0);
        Card p2 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 38, 0);
        //Define 2 cards to link the structure
        Card place1 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 12, 0);
        Card place2 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 26, 0);
        Card place3 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 36, 0);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        p.getPlayerBoard().setStarterCard(s);
        try {
            p.placeCard(r1, new int[]{0, 0}, CornerEnum.TR);
            p.placeCard(r2, new int[]{0, 0}, CornerEnum.BR);
            p.placeCard(g1, new int[]{1, -1}, CornerEnum.BR);
            p.placeCard(b1, new int[]{1, -1}, CornerEnum.BL);
            p.placeCard(place1, new int[]{2, -2}, CornerEnum.BR);
            p.placeCard(place2, new int[]{0, -2}, CornerEnum.BL);
            p.placeCard(b2, new int[]{-1, -3}, CornerEnum.BR);
            p.placeCard(g2, new int[]{3, -3}, CornerEnum.BL);
            p.placeCard(p1, new int[]{0, -4}, CornerEnum.BR);
            p.placeCard(place3, new int[]{1, -5}, CornerEnum.BL);
            p.placeCard(p2, new int[]{0, -6}, CornerEnum.BR);
        } catch (OccupiedCornerException | NotInTurnException | AlreadyUsedPositionException |
                 CostNotSatisfiedException ex) {
            System.out.println(ex.getMessage());
        }
        a.calcPoints(p);
        b.calcPoints(p);
        c.calcPoints(p);
        d.calcPoints(p);
        //12 points done
        assertEquals(12, p.getPoints());
    }

    @Test
    @DisplayName("Mixed Achievement")
    public void mixedAchievementTest() throws IOException{
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementL(Color.RED);
        Achievement b = new AchievementDiagonal(Color.GREEN);
        Achievement c = new AchievementItem(2, new ArrayList<>(List.of(Symbols.QUILL)));
        Achievement d = new AchievementResources(Symbols.INSECT);
        //Define a bunch of cards to fill the board
        Card r1 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 7, 0);
        Card r2 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.PLANT) }, 8, 0);
        Card g1 = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.QUILL) }, 14, 0);
        Card g2 = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 15, 0);
        Card g3 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 12, 0);
        Card b1 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.INSECT) }, 24, 0);
        Card b2 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.QUILL) }, 27, 0);
        Card p1 = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 32, 0);
        Card p2 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.QUILL), new Corner(Symbols.QUILL) }, 38, 0);
        Card b3 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.QUILL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 26, 0);
        Card p3 = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY) }, 36, 0);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        p.getPlayerBoard().setStarterCard(s);
        //Placing the cards
        try {
            p.placeCard(r1, new int[]{0, 0}, CornerEnum.TR);
            p.placeCard(r2, new int[]{0, 0}, CornerEnum.BR);
            p.placeCard(g1, new int[]{1, -1}, CornerEnum.BR);
            p.placeCard(g2, new int[]{2, -2}, CornerEnum.BR);
            p.placeCard(g3, new int[]{3, -3}, CornerEnum.BR);
            p.placeCard(b1, new int[]{1, -1}, CornerEnum.BL);
            p.placeCard(p3, new int[]{2, -2}, CornerEnum.BL);
            p.placeCard(b2, new int[]{3, -3}, CornerEnum.BL);
            p.placeCard(p1, new int[]{0, 0}, CornerEnum.TL);
            p.placeCard(b3, new int[]{4, -4}, CornerEnum.TR);
            p.placeCard(p2, new int[]{2, -2}, CornerEnum.TR);
        } catch (OccupiedCornerException | NotInTurnException | AlreadyUsedPositionException |
                 CostNotSatisfiedException ex) {
            System.out.println(ex.getMessage());
        }
        //point = 3 => only one red L shape
        a.calcPoints(p);
        // point = 2 => only one green diagonal
        b.calcPoints(p);
        // Uncovered Quill: 7
        c.calcPoints(p);
        // Uncovered Insect:5 => point = 1
        d.calcPoints(p);
        assertEquals(13, p.getPoints());
    }
}
