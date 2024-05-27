package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class AchievementTest {
    @Test
    @DisplayName("Diagonal Achievement")
    public void diagonalTest() {
        Game game = new Game(4);
        Achievement a = new AchievementDiagonal(Color.PURPLE, 4);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER), new Corner(Symbols.INKWELL) }, 37, 0);
        Player p = new Player("pippo", game);
        //Creating a purple diagonal
        p.getPlayerBoard().setCardPosition(c, new int[]{-40,-40});
        p.getPlayerBoard().setCardPosition(c, new int[]{-39,-41});
        p.getPlayerBoard().setCardPosition(c, new int[]{-41,-39});
        //2 points
        a.calcPoints(p);
        assertEquals(2, p.getPoints());
    }

    @Test
    @DisplayName("Diagonal Achievement - No Pattern found")
    public void noDiagonalTest() {
        Game game = new Game(4);
        Achievement a = new AchievementDiagonal(Color.RED, 1);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER), new Corner(Symbols.INKWELL) }, 37, 0);
        Player p = new Player("pippo", game);
        int offset = 5; //Added some points to the player to see if calcPoints adds 0 points
        //Added some cards to the player board. None of them is red
        p.getPlayerBoard().setCardPosition(c, new int[]{0,0});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1,1});
        p.getPlayerBoard().setCardPosition(c, new int[]{1,-1});
        p.addPoints(offset);
        a.calcPoints(p); //should not add any points to p
        assertEquals(offset, p.getPoints());
    }

    @Test
    @DisplayName("Diagonal Achievement - No Card Reuse")
    public void noReuseTest() {
        Game game = new Game(4);
        Achievement a = new AchievementDiagonal(Color.PURPLE, 4);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER), new Corner(Symbols.INKWELL) }, 37, 0);
        Player p = new Player("pippo", game);
        //Creating a six-card purple diagonal
        p.getPlayerBoard().setCardPosition(c, new int[]{0,0});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1,1});
        p.getPlayerBoard().setCardPosition(c, new int[]{1,-1});
        p.getPlayerBoard().setCardPosition(c, new int[]{2,-2});
        p.getPlayerBoard().setCardPosition(c, new int[]{3,-3});
        p.getPlayerBoard().setCardPosition(c, new int[]{4,-4});
        //two groups of 3 purple cards = 4 points
        a.calcPoints(p);
        assertEquals(4, p.getPoints());
    }
    @Test
    @DisplayName("Item Achievement - 3 Items")
    public void itemTest() {
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)), 15);
        Card b = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.INKWELL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY) }, 14, 0);
        Card c = new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        Card d = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INKWELL), new Corner(Symbols.NOCORNER) }, 16, 0);
        Card e = new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.QUILL), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0);
        Card f = new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 18, 1);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        //Place some cards
        p.getPlayerBoard().setStarterCard(s);
        try {
            p.placeCard(b, new int[]{-1,1});
            p.placeCard(c, new int[]{1,1});
            p.placeCard(d, new int[]{-1,-1});
            p.placeCard(e, new int[]{1,-1});
            p.placeCard(f, new int[]{-2,0});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
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
    public void noItemTest() {
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementItem(3, new ArrayList<>(List.of(Symbols.INKWELL, Symbols.QUILL, Symbols.MANUSCRIPT)), 15);
        Achievement b = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL)), 14);
        Achievement c = new AchievementItem(2, new ArrayList<>(List.of(Symbols.QUILL)), 13);
        Achievement d = new AchievementItem(2, new ArrayList<>(List.of(Symbols.MANUSCRIPT)), 12);
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
    public void resourcesTest() {
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementResources(Symbols.FUNGI, 8);
        Achievement b = new AchievementResources(Symbols.PLANT, 9);
        Achievement c = new AchievementResources(Symbols.ANIMAL, 10);
        Achievement d = new AchievementResources(Symbols.INSECT, 11);
        //Define a bunch of cards to populate the board
        Card e = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY) }, 14, 0);
        Card f = new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0);
        Card g = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER) }, 16, 0);
        Card h = new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0);
        Card i = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 18, 0);
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.getPlayerBoard().setStarterCard(s);
        //p can place card
        p.setPlayerState(PlayerState.PLAY_CARD);
        //place the cards
        try {
            p.placeCard(e, new int[]{-1,1});
            p.placeCard(f, new int[]{1,1});
            p.placeCard(g, new int[]{-1,-1});
            p.placeCard(h, new int[]{1,-1});
            p.placeCard(i, new int[]{0,2});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
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
    public void lTest() {
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementL(Color.RED, 1);
        Achievement b = new AchievementL(Color.BLUE, 2);
        Achievement c = new AchievementL(Color.GREEN, 3);
        Achievement d = new AchievementL(Color.PURPLE, 4);
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
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        p.getPlayerBoard().setStarterCard(s);
        try {
            p.placeCard(r1, new int[]{1, 1});
            p.placeCard(r2, new int[]{1, -1});
            p.placeCard(g1, new int[]{2, -2});
            p.placeCard(b1, new int[]{0, -2});
            p.placeCard(place1, new int[]{3, -3});
            p.placeCard(place2, new int[]{-1, -3});
            p.placeCard(b2, new int[]{0, -4});
            p.placeCard(g2, new int[]{2, -4});
            p.placeCard(p1, new int[]{1, -5});
            p.placeCard(place3, new int[]{0, -6});
            p.placeCard(p2, new int[]{1, -7});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
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
    public void mixedAchievementTest() {
        Game game = new Game(4);
        Player p = new Player("pippo", game);
        Achievement a = new AchievementL(Color.RED, 1);
        Achievement b = new AchievementDiagonal(Color.GREEN, 5);
        Achievement c = new AchievementItem(2, new ArrayList<>(List.of(Symbols.QUILL)), 9);
        Achievement d = new AchievementResources(Symbols.INSECT, 6);
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
        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        p.setPlayerState(PlayerState.PLAY_CARD);
        p.getPlayerBoard().setStarterCard(s);
        //Placing the cards
        try {
            p.placeCard(r1, new int[]{1, 1});
            p.placeCard(r2, new int[]{1, -1});
            p.placeCard(g1, new int[]{2, -2});
            p.placeCard(g2, new int[]{3, -3}); //
            p.placeCard(g3, new int[]{4, -4});
            p.placeCard(b1, new int[]{0, -2});
            p.placeCard(p3, new int[]{-1, -3});
            p.placeCard(b2, new int[]{-1, 1});
            p.placeCard(p1, new int[]{-1, -1});
            p.placeCard(b3, new int[]{-2, 2});
            p.placeCard(p2, new int[]{2, 2});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
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