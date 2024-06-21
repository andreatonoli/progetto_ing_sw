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
    @DisplayName("Checking card info")
    public void getterTest(){
        Achievement a = new AchievementDiagonal(Color.RED, 1);
        Achievement b = new AchievementL(Color.BLUE, 2);
        Achievement c = new AchievementResources(Symbols.PLANT, 3);
        Achievement d = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL)), 4);

        //Checking a's infos
        assertEquals(1, a.getId());
        assertEquals(Color.RED, a.getColor());
        assertEquals(2, a.getPoints());
        assertNull(a.getSymbols());
        assertNull(a.getSymbol());

        //Checking b's infos
        assertEquals(2, b.getId());
        assertEquals(Color.BLUE, b.getColor());
        assertEquals(3, b.getPoints());
        assertNull(b.getSymbols());
        assertNull(b.getSymbol());

        //Checking c's infos
        assertEquals(3, c.getId());
        assertNull(c.getColor());
        assertEquals(2, c.getPoints());
        assertNull(c.getSymbols());
        assertEquals(Symbols.PLANT, c.getSymbol());

        //Checking d's infos
        assertEquals(4, d.getId());
        assertNull(d.getColor());
        assertEquals(2, d.getPoints());
        assertEquals(1, d.getSymbols().size());
        assertEquals(Symbols.INKWELL, d.getSymbols().getFirst());
        assertNull(d.getSymbol());

    }

    @Test
    @DisplayName("Equals")
    public void equalsTest(){
        Achievement a1 = new AchievementDiagonal(Color.RED, 1);
        Achievement a2 = new AchievementDiagonal(Color.BLUE, 1);
        Achievement a3 = new AchievementDiagonal(Color.RED, 10);
        Achievement a4 = new AchievementDiagonal(Color.RED, 1);

        Achievement b1 = new AchievementL(Color.BLUE, 2);
        Achievement b2 = new AchievementL(Color.GREEN, 2);
        Achievement b3 = new AchievementL(Color.BLUE, 20);
        Achievement b4 = new AchievementL(Color.BLUE, 2);

        Achievement c1 = new AchievementResources(Symbols.PLANT, 3);
        Achievement c2 = new AchievementResources(Symbols.FUNGI, 3);
        Achievement c3 = new AchievementResources(Symbols.PLANT, 30);
        Achievement c4 = new AchievementResources(Symbols.PLANT, 3);

        Achievement d1 = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL)), 4);
        Achievement d2 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.INKWELL)), 4);
        Achievement d3 = new AchievementItem(2, new ArrayList<>(List.of(Symbols.QUILL)), 4);
        Achievement d4 = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL, Symbols.MANUSCRIPT)), 4);
        Achievement d5 = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL)), 40);
        Achievement d6 = new AchievementItem(2, new ArrayList<>(List.of(Symbols.INKWELL)), 4);

        //Every type of achievement is different
        assertFalse(a1.equals(b1));
        assertFalse(a1.equals(c1));
        assertFalse(a1.equals(d1));
        assertFalse(b1.equals(a1));
        assertFalse(b1.equals(c1));
        assertFalse(b1.equals(d1));
        assertFalse(c1.equals(a1));
        assertFalse(c1.equals(b1));
        assertFalse(c1.equals(d1));
        assertFalse(d1.equals(a1));
        assertFalse(d1.equals(b1));
        assertFalse(d1.equals(c1));

        //Different cards are different
        assertFalse(a1.equals(a2));
        assertFalse(a1.equals(a3));
        assertFalse(b1.equals(b2));
        assertFalse(b1.equals(b3));
        assertFalse(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(d1.equals(d2));
        assertFalse(d1.equals(d3));
        assertFalse(d1.equals(d4));
        assertFalse(d1.equals(d5));

        //same card are equals
        assertTrue(a1.equals(a4));
        assertTrue(b1.equals(b4));
        assertTrue(c1.equals(c4));
        assertTrue(d1.equals(d6));

    }
    @Test
    @DisplayName("Diagonal Achievement")
    public void diagonalTest() {
        Game game = new Game(4);
        Achievement a = new AchievementDiagonal(Color.PURPLE, 4);
        Card c = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Player p = new Player("pippo", game);
        //Creating a purple diagonal
        p.getPlayerBoard().setCardPosition(c, new int[]{0, 0});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1, 1});
        p.getPlayerBoard().setCardPosition(c, new int[]{1, -1});

        //Creating a purple diagonal but in the opposite sense
        p.getPlayerBoard().setCardPosition(c, new int[]{1, 1});
        p.getPlayerBoard().setCardPosition(c, new int[]{-1, -1});

        //2 points
        a.calcPoints(p);
        assertEquals(2, p.getPoints());
    }

    @Test
    @DisplayName("Diagonal Achievement - No Pattern found")
    public void noDiagonalTest() {
        Game game = new Game(4);
        Achievement a = new AchievementDiagonal(Color.RED, 1);
        Card c = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
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
        Card c = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
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
        Card b = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.INKWELL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card c = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INKWELL), new Corner(Symbols.MANUSCRIPT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card d = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card e = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.QUILL), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card f = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 1), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
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
        Card e = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card f = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card g = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card h = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card i = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
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
        Card r1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card r2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        //Define 2 green cards
        Card g1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.GREEN);
        Card g2 = new Card(  new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.GREEN);
        //Define 2 blue cards
        Card b1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.BLUE);
        Card b2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.BLUE);
        //Define 2 purple cards
        Card p1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card p2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        //Define 2 cards to link the structure
        Card place1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.BLUE);
        Card place2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.GREEN);
        Card place3 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
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
        Card r1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card r2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.RED);
        Card g1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INKWELL), new Corner(Symbols.INSECT), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.GREEN);
        Card g2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.GREEN);
        Card g3 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.GREEN);
        Card b1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.FUNGI), new Corner(Symbols.INSECT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.BLUE);
        Card b2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.BLUE);
        Card p1 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card p2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.QUILL), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card b3 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.QUILL), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.BLUE);
        Card p3 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 30, Color.PURPLE);
        Card s = new Card(new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}), new CardBack(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT), new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}), "starter", 1, Color.WHITE);
        p.setPlayerState(PlayerState.PLAY_CARD);
        p.getPlayerBoard().setStarterCard(s);
        //Placing the cards
        try {
            p.placeCard(r1, new int[]{1, 1});
            p.placeCard(r2, new int[]{1, -1});
            p.placeCard(g1, new int[]{2, -2});
            p.placeCard(g2, new int[]{3, -3});
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