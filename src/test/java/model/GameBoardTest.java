package model;

import model.card.*;
import model.enums.Color;
import model.enums.Condition;
import model.enums.CornerEnum;
import model.enums.Symbols;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    @DisplayName("checking resourceCard deck")
    public void resourceCardTest(){
        Game game = new Game(2);
        GameBoard board = game.getGameBoard();
        ResourceCard firstCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI)}, 1, 0);
        ResourceCard midCard = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 20, 1);
        ResourceCard lastCard = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 40, 1);
        LinkedList<Card> resourceDeck = board.getResourceDeck();

        //for (int i=0; i< resourceDeck.size(); i++){
        //    System.out.println(resourceDeck.get(i).getCardNumber()+" è "+ i);
        //    System.out.println("numero punti "+resourceDeck.get(i).getPoints()+" simboli "+resourceDeck.get(i).getCorner(CornerEnum.TL).getSymbol().toString()+" "+resourceDeck.get(i).getCorner(CornerEnum.TR).getSymbol().toString()+" "+resourceDeck.get(i).getCorner(CornerEnum.BL).getSymbol().toString()+" "+resourceDeck.get(i).getCorner(CornerEnum.BR).getSymbol().toString())
        //}

        assertEquals(firstCard.getCardNumber(),resourceDeck.get(38).getCardNumber());
        assertEquals(firstCard.getPoints(),resourceDeck.get(38).getPoints());
        assertEquals(firstCard.getCornerSymbol(CornerEnum.TL),resourceDeck.get(38).getCornerSymbol(CornerEnum.TL));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.TR),resourceDeck.get(38).getCornerSymbol(CornerEnum.TR));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.BL),resourceDeck.get(38).getCornerSymbol(CornerEnum.BL));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.BR),resourceDeck.get(38).getCornerSymbol(CornerEnum.BR));

        assertEquals(midCard.getCardNumber(),resourceDeck.get(15).getCardNumber());
        assertEquals(midCard.getPoints(),resourceDeck.get(15).getPoints());
        assertEquals(midCard.getCornerSymbol(CornerEnum.TL),resourceDeck.get(15).getCornerSymbol(CornerEnum.TL));
        assertEquals(midCard.getCornerSymbol(CornerEnum.TR),resourceDeck.get(15).getCornerSymbol(CornerEnum.TR));
        assertEquals(midCard.getCornerSymbol(CornerEnum.BL),resourceDeck.get(15).getCornerSymbol(CornerEnum.BL));
        assertEquals(midCard.getCornerSymbol(CornerEnum.BR),resourceDeck.get(15).getCornerSymbol(CornerEnum.BR));

        assertEquals(lastCard.getCardNumber(),resourceDeck.get(16).getCardNumber());
        assertEquals(lastCard.getPoints(),resourceDeck.get(16).getPoints());
        assertEquals(lastCard.getCornerSymbol(CornerEnum.TL),resourceDeck.get(16).getCornerSymbol(CornerEnum.TL));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.TR),resourceDeck.get(16).getCornerSymbol(CornerEnum.TR));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.BL),resourceDeck.get(16).getCornerSymbol(CornerEnum.BL));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.BR),resourceDeck.get(16).getCornerSymbol(CornerEnum.BR));

        assertEquals(40,resourceDeck.size());
    }

    @Test
    @DisplayName("checking resourceCard deck")
    public void goldCardTest(){
        Game game = new Game(2);
        GameBoard board = game.getGameBoard();
        GoldCard firstCard = new GoldCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY)}, 1, Condition.ITEM, 1,new int[]{2,0,1,0},Symbols.QUILL);
        GoldCard midCard = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}, 5, Condition.NOTHING, 20,new int[]{0,5,0,0}, null);
        GoldCard lastCard = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}, 5, Condition.NOTHING, 40,new int[]{0,0,0,5},null);
        LinkedList<Card> goldDeck = board.getGoldDeck();

        //firstCard
        assertEquals(firstCard.getCardNumber(),goldDeck.get(0).getCardNumber());
        assertEquals(firstCard.getPoints(),goldDeck.get(0).getPoints());
        assertEquals(firstCard.getCondition(),goldDeck.get(0).getCondition());
        assertEquals(firstCard.getRequiredItem(),goldDeck.get(0).getRequiredItem());
        //controlla l'array costo
        for (int i=0; i<4; i++){
            assertEquals(firstCard.getCost()[i],goldDeck.get(0).getCost()[i]);
        }
        //controlla gli angoli
        assertEquals(firstCard.getCornerSymbol(CornerEnum.TL),goldDeck.get(0).getCornerSymbol(CornerEnum.TL));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.TR),goldDeck.get(0).getCornerSymbol(CornerEnum.TR));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.BL),goldDeck.get(0).getCornerSymbol(CornerEnum.BL));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.BR),goldDeck.get(0).getCornerSymbol(CornerEnum.BR));

        //midCard
        assertEquals(midCard.getCardNumber(),goldDeck.get(39).getCardNumber());
        assertEquals(midCard.getPoints(),goldDeck.get(39).getPoints());
        assertEquals(midCard.getCondition(),goldDeck.get(39).getCondition());
        assertEquals(midCard.getRequiredItem(),goldDeck.get(39).getRequiredItem());
        //controlla l'array costo
        for (int i=0; i<4; i++){
            assertEquals(midCard.getCost()[i],goldDeck.get(39).getCost()[i]);
        }
        //controlla gli angoli
        assertEquals(midCard.getCornerSymbol(CornerEnum.TL),goldDeck.get(39).getCornerSymbol(CornerEnum.TL));
        assertEquals(midCard.getCornerSymbol(CornerEnum.TR),goldDeck.get(39).getCornerSymbol(CornerEnum.TR));
        assertEquals(midCard.getCornerSymbol(CornerEnum.BL),goldDeck.get(39).getCornerSymbol(CornerEnum.BL));
        assertEquals(midCard.getCornerSymbol(CornerEnum.BR),goldDeck.get(39).getCornerSymbol(CornerEnum.BR));

        //lastCard
        assertEquals(lastCard.getCardNumber(),goldDeck.get(37).getCardNumber());
        assertEquals(lastCard.getPoints(),goldDeck.get(37).getPoints());
        assertEquals(lastCard.getCondition(),goldDeck.get(37).getCondition());
        assertEquals(lastCard.getRequiredItem(),goldDeck.get(37).getRequiredItem());
        //controlla l'array costo
        for (int i=0; i<4; i++){
            assertEquals(lastCard.getCost()[i],goldDeck.get(37).getCost()[i]);
        }
        //controlla gli angoli
        assertEquals(lastCard.getCornerSymbol(CornerEnum.TL),goldDeck.get(37).getCornerSymbol(CornerEnum.TL));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.TR),goldDeck.get(37).getCornerSymbol(CornerEnum.TR));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.BL),goldDeck.get(37).getCornerSymbol(CornerEnum.BL));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.BR),goldDeck.get(37).getCornerSymbol(CornerEnum.BR));

        assertEquals(40,goldDeck.size());
    }

    @Test
    @DisplayName("checking starterCard deck")
    public void starterCardTest(){
        Game game = new Game(2);
        GameBoard board = game.getGameBoard();
        StarterCard firstCard = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
        StarterCard midCard = new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI)}, 3, new CardBack(new ArrayList<>(List.of(Symbols.PLANT,Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}));
        StarterCard lastCard = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT)}, 6, new CardBack(new ArrayList<>(List.of(Symbols.PLANT,Symbols.ANIMAL,Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}));
        LinkedList<Card> starterDeck = board.getStarterDeck();

        for (int i=0; i< starterDeck.size(); i++){
            System.out.println(starterDeck.get(i).getCardNumber()+" è "+ i);
        }

        //firstCard front
        assertEquals(firstCard.getCardNumber(),starterDeck.get(5).getCardNumber());
        assertEquals(firstCard.getCornerSymbol(CornerEnum.TL),starterDeck.get(5).getCornerSymbol(CornerEnum.TL));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.TR),starterDeck.get(5).getCornerSymbol(CornerEnum.TR));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.BL),starterDeck.get(5).getCornerSymbol(CornerEnum.BL));
        assertEquals(firstCard.getCornerSymbol(CornerEnum.BR),starterDeck.get(5).getCornerSymbol(CornerEnum.BR));
        //firstCard back
        //TODO: cambia getBack girando prima la carta con il metodo setCurrentSide
        for (int i=0; i<starterDeck.get(0).getBack().getSymbols().size(); i++) {
            assertEquals(firstCard.getBack().getSymbols().get(i), starterDeck.get(5).getBack().getSymbols().get(i));
        }
        assertEquals(firstCard.getBack().getColor(), starterDeck.get(5).getBack().getColor());
        assertEquals(firstCard.getBack().getCornerSymbol(CornerEnum.TL),starterDeck.get(5).getBack().getCornerSymbol(CornerEnum.TL));
        assertEquals(firstCard.getBack().getCornerSymbol(CornerEnum.TR),starterDeck.get(5).getBack().getCornerSymbol(CornerEnum.TR));
        assertEquals(firstCard.getBack().getCornerSymbol(CornerEnum.BL),starterDeck.get(5).getBack().getCornerSymbol(CornerEnum.BL));
        assertEquals(firstCard.getBack().getCornerSymbol(CornerEnum.BR),starterDeck.get(5).getBack().getCornerSymbol(CornerEnum.BR));

        //midCard front
        assertEquals(midCard.getCardNumber(),starterDeck.get(3).getCardNumber());
        assertEquals(midCard.getCornerSymbol(CornerEnum.TL),starterDeck.get(3).getCornerSymbol(CornerEnum.TL));
        assertEquals(midCard.getCornerSymbol(CornerEnum.TR),starterDeck.get(3).getCornerSymbol(CornerEnum.TR));
        assertEquals(midCard.getCornerSymbol(CornerEnum.BL),starterDeck.get(3).getCornerSymbol(CornerEnum.BL));
        assertEquals(midCard.getCornerSymbol(CornerEnum.BR),starterDeck.get(3).getCornerSymbol(CornerEnum.BR));
        //midCard back
        for (int i=0; i<starterDeck.get(0).getBack().getSymbols().size(); i++) {
            assertEquals(midCard.getBack().getSymbols().get(i), starterDeck.get(3).getBack().getSymbols().get(i));
        }
        assertEquals(midCard.getBack().getColor(), starterDeck.get(3).getBack().getColor());
        assertEquals(midCard.getBack().getCornerSymbol(CornerEnum.TL),starterDeck.get(3).getBack().getCornerSymbol(CornerEnum.TL));
        assertEquals(midCard.getBack().getCornerSymbol(CornerEnum.TR),starterDeck.get(3).getBack().getCornerSymbol(CornerEnum.TR));
        assertEquals(midCard.getBack().getCornerSymbol(CornerEnum.BL),starterDeck.get(3).getBack().getCornerSymbol(CornerEnum.BL));
        assertEquals(midCard.getBack().getCornerSymbol(CornerEnum.BR),starterDeck.get(3).getBack().getCornerSymbol(CornerEnum.BR));

        //lastCard front
        assertEquals(lastCard.getCardNumber(),starterDeck.get(0).getCardNumber());
        assertEquals(lastCard.getCornerSymbol(CornerEnum.TL),starterDeck.get(0).getCornerSymbol(CornerEnum.TL));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.TR),starterDeck.get(0).getCornerSymbol(CornerEnum.TR));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.BL),starterDeck.get(0).getCornerSymbol(CornerEnum.BL));
        assertEquals(lastCard.getCornerSymbol(CornerEnum.BR),starterDeck.get(0).getCornerSymbol(CornerEnum.BR));
        //lastCard back
        for (int i=0; i<starterDeck.get(0).getBack().getSymbols().size(); i++) {
            assertEquals(lastCard.getBack().getSymbols().get(i), starterDeck.get(0).getBack().getSymbols().get(i));
        }
        assertEquals(lastCard.getBack().getColor(), starterDeck.get(0).getBack().getColor());
        assertEquals(lastCard.getBack().getCornerSymbol(CornerEnum.TL),starterDeck.get(0).getBack().getCornerSymbol(CornerEnum.TL));
        assertEquals(lastCard.getBack().getCornerSymbol(CornerEnum.TR),starterDeck.get(0).getBack().getCornerSymbol(CornerEnum.TR));
        assertEquals(lastCard.getBack().getCornerSymbol(CornerEnum.BL),starterDeck.get(0).getBack().getCornerSymbol(CornerEnum.BL));
        assertEquals(lastCard.getBack().getCornerSymbol(CornerEnum.BR),starterDeck.get(0).getBack().getCornerSymbol(CornerEnum.BR));

        assertEquals(6,starterDeck.size());
    }

    @Test
    @DisplayName("drawing from resource deck")
    public void drawResourceCardTest() {
        Game game = new Game(2);
        GameBoard board = game.getGameBoard();
        LinkedList<Card> resourceDeck = board.getResourceDeck();
        Card firstCard = resourceDeck.getFirst();
        Card drawedCard = board.drawCard(resourceDeck);
        assertEquals(firstCard,drawedCard);
        assertEquals(39,resourceDeck.size());
    }

    @Test
    @DisplayName("drawing from gold deck")
    public void drawGoldCardTest() {
        Game game = new Game(2);
        GameBoard board = game.getGameBoard();
        LinkedList<Card> goldDeck = board.getGoldDeck();
        Card firstCard = goldDeck.getFirst();
        Card drawedCard = board.drawCard(goldDeck);
        assertEquals(firstCard,drawedCard);
        assertEquals(39,goldDeck.size());
    }

    @Test
    @DisplayName("drawing from achievement deck")
    public void drawAchievementCardTest() {
        Game game = new Game(2);
        GameBoard board = game.getGameBoard();
        LinkedList<Achievement> achievementDeck = board.getAchievementDeck();
        Achievement firstCard = achievementDeck.getFirst();
        Achievement drawnCard = board.drawCard();
        assertEquals(firstCard,drawnCard);
        assertEquals(15,achievementDeck.size());
    }


}