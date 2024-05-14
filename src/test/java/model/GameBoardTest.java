//package model;
//
//import it.polimi.ingsw.model.*;
//import it.polimi.ingsw.model.card.*;
//import it.polimi.ingsw.model.enums.Color;
//import it.polimi.ingsw.model.enums.Condition;
//import it.polimi.ingsw.model.enums.CornerEnum;
//import it.polimi.ingsw.model.enums.Symbols;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameBoardTest {
//
//    @Test
//    @DisplayName("checking resourceCard deck")
//    public void resourceCardTest(){
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        ResourceCard firstCard = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI)}, 1, 0);
//        ResourceCard midCard = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 20, 1);
//        ResourceCard lastCard = new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 40, 1);
//        LinkedList<Card> resourceDeck = board.getResourceDeck();
//
//        //firstCard
//        assertEquals(resourceDeck.getFirst().getCardNumber(), firstCard.getCardNumber());
//        assertEquals(resourceDeck.getFirst().getPoints(), firstCard.getPoints());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(resourceDeck.getFirst().getCornerSymbol(c), firstCard.getCornerSymbol(c));
//        }
//        //midCard
//        assertEquals(resourceDeck.get(19).getCardNumber(), midCard.getCardNumber());
//        assertEquals(resourceDeck.get(19).getPoints(), midCard.getPoints());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(resourceDeck.get(19).getCornerSymbol(c), midCard.getCornerSymbol(c));
//        }
//        //lastCard
//        assertEquals(resourceDeck.get(39).getCardNumber(), lastCard.getCardNumber());
//        assertEquals(resourceDeck.get(39).getPoints(), lastCard.getPoints());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(resourceDeck.get(39).getCornerSymbol(c), lastCard.getCornerSymbol(c));
//        }
//
//        assertEquals(40,resourceDeck.size());
//    }
//
//    @Test
//    @DisplayName("checking goldCard deck")
//    public void goldCardTest(){
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        GoldCard firstCard = new GoldCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY)}, 1, Condition.ITEM, 1,new Integer[]{2,0,1,0},Symbols.QUILL);
//        GoldCard midCard = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}, 5, Condition.NOTHING, 20,new Integer[]{0,5,0,0}, null);
//        GoldCard lastCard = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}, 5, Condition.NOTHING, 40,new Integer[]{0,0,0,5},null);
//        LinkedList<Card> goldDeck = board.getGoldDeck();
//
//        //firstCard
//        assertEquals(goldDeck.getFirst().getCardNumber(), firstCard.getCardNumber());
//        assertEquals(goldDeck.getFirst().getPoints(), firstCard.getPoints());
//        assertEquals(goldDeck.getFirst().getCondition(), firstCard.getCondition());
//        assertEquals(goldDeck.getFirst().getRequiredItem(), firstCard.getRequiredItem());
//        //controlla l'array costo
//        for (int i=0; i<4; i++){
//            assertEquals(goldDeck.getFirst().getCost()[i], firstCard.getCost()[i]);
//        }
//        //controlla gli angoli
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(goldDeck.getFirst().getCornerSymbol(c), firstCard.getCornerSymbol(c));
//        }
//
//        //midCard
//        assertEquals(goldDeck.get(19).getCardNumber(), midCard.getCardNumber());
//        assertEquals(goldDeck.get(19).getPoints(), midCard.getPoints());
//        assertEquals(goldDeck.get(19).getCondition(), midCard.getCondition());
//        assertEquals(goldDeck.get(19).getRequiredItem(), midCard.getRequiredItem());
//        //controlla l'array costo
//        for (int i=0; i<4; i++){
//            assertEquals(goldDeck.get(19).getCost()[i], midCard.getCost()[i]);
//        }
//        //controlla gli angoli
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(goldDeck.get(19).getCornerSymbol(c), midCard.getCornerSymbol(c));
//        }
//
//        //lastCard
//        assertEquals(goldDeck.get(39).getCardNumber(), lastCard.getCardNumber());
//        assertEquals(goldDeck.get(39).getPoints(), lastCard.getPoints());
//        assertEquals(goldDeck.get(39).getCondition(), lastCard.getCondition());
//        assertEquals(goldDeck.get(39).getRequiredItem(), lastCard.getRequiredItem());
//        //controlla l'array costo
//        for (int i=0; i<4; i++){
//            assertEquals(goldDeck.get(39).getCost()[i], lastCard.getCost()[i]);
//        }
//        //controlla gli angoli
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(goldDeck.get(39).getCornerSymbol(c), lastCard.getCornerSymbol(c));
//        }
//
//        assertEquals(40,goldDeck.size());
//    }
//
//    @Test
//    @DisplayName("checking starterCard deck")
//    public void starterCardTest(){
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        StarterCard firstCard = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)}));
//        StarterCard midCard = new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI)}, 3, new CardBack(new ArrayList<>(List.of(Symbols.PLANT,Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}));
//        StarterCard lastCard = new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT)}, 6, new CardBack(new ArrayList<>(List.of(Symbols.PLANT,Symbols.ANIMAL,Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER)}));
//        LinkedList<Card> starterDeck = board.getStarterDeck();
//
//        //firstCard front
//        assertEquals(starterDeck.getFirst().getCardNumber(), firstCard.getCardNumber());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(starterDeck.getFirst().getCornerSymbol(c), firstCard.getCornerSymbol(c));
//        }
//        //firstCard back
//        firstCard.setCurrentSide();
//        starterDeck.getFirst().setCurrentSide();
//        for (int i=0; i<starterDeck.getFirst().getSymbols().size(); i++) {
//            assertEquals(starterDeck.getFirst().getSymbols().get(i), firstCard.getSymbols().get(i));
//        }
//        assertEquals(starterDeck.getFirst().getColor(), firstCard.getColor());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(starterDeck.getFirst().getCornerSymbol(c), firstCard.getCornerSymbol(c));
//        }
//
//        //midCard front
//        assertEquals(starterDeck.get(2).getCardNumber(), midCard.getCardNumber());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(starterDeck.get(2).getCornerSymbol(c), midCard.getCornerSymbol(c));
//        }
//        //midCard back
//        midCard.setCurrentSide();
//        starterDeck.get(2).setCurrentSide();
//        for (int i=0; i<starterDeck.get(2).getSymbols().size(); i++) {
//            assertEquals(midCard.getSymbols().get(i), starterDeck.get(2).getSymbols().get(i));
//        }
//        assertEquals(starterDeck.get(2).getColor(), midCard.getColor());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(starterDeck.get(2).getCornerSymbol(c), midCard.getCornerSymbol(c));
//        }
//
//        //lastCard front
//        assertEquals(lastCard.getCardNumber(),starterDeck.get(5).getCardNumber());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(starterDeck.get(5).getCornerSymbol(c), lastCard.getCornerSymbol(c));
//        }
//        //lastCard back
//        lastCard.setCurrentSide();
//        starterDeck.get(5).setCurrentSide();
//        for (int i=0; i<starterDeck.get(5).getSymbols().size(); i++) {
//            assertEquals(starterDeck.get(5).getSymbols().get(i), lastCard.getSymbols().get(i));
//        }
//        assertEquals(starterDeck.get(5).getColor(), lastCard.getColor());
//        for (CornerEnum c : CornerEnum.values()){
//            assertEquals(starterDeck.get(5).getCornerSymbol(c), lastCard.getCornerSymbol(c));
//        }
//
//        assertEquals(6,starterDeck.size());
//    }
//
//    @Test
//    @DisplayName("checking achievementCard deck")
//    public void achievementCardTest(){
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        Achievement firstCard_L = new AchievementL(Color.RED);
//        Achievement firstCard_D = new AchievementL(Color.RED);
//        Achievement midCard = new AchievementResources(Symbols.ANIMAL);
//        Achievement midCard_I = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)));
//        Achievement lastCard =  new AchievementItem(2, new ArrayList<>(List.of(Symbols.QUILL)));
//        LinkedList<Achievement> achievementDeck = board.getAchievementDeck();
//
//        //achievements L and D
//        assertEquals(achievementDeck.getFirst().getColor(), firstCard_L.getColor());
//        assertEquals(achievementDeck.get(1).getColor(), firstCard_D.getColor());
//        //achievement resources
//        assertEquals(achievementDeck.get(10).getSymbol(), midCard.getSymbol());
//        //achievements items
//        assertEquals(achievementDeck.get(12).getPoints(), midCard_I.getPoints());
//        for(int i=0; i<achievementDeck.get(12).getSymbols().size(); i++) {
//            assertEquals(achievementDeck.get(12).getSymbols().get(i), midCard_I.getSymbols().get(i));
//        }
//        assertEquals(achievementDeck.get(15).getPoints(), lastCard.getPoints());
//        assertEquals(achievementDeck.get(15).getSymbols().getFirst(), lastCard.getSymbols().getFirst());
//
//        assertEquals(16,achievementDeck.size());
//    }
//
//    @Test
//    @DisplayName("drawing from resource deck")
//    public void drawResourceCardTest() {
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        LinkedList<Card> resourceDeck = board.getResourceDeck();
//        Card firstCard = resourceDeck.getFirst();
//        Card drawedCard = board.drawCard(resourceDeck);
//        assertEquals(firstCard, drawedCard);
//        assertEquals(39, resourceDeck.size());
//    }
//
//    @Test
//    @DisplayName("drawing from gold deck")
//    public void drawGoldCardTest() {
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        LinkedList<Card> goldDeck = board.getGoldDeck();
//        Card firstCard = goldDeck.getFirst();
//        Card drawedCard = board.drawCard(goldDeck);
//        assertEquals(firstCard, drawedCard);
//        assertEquals(39, goldDeck.size());
//    }
//
//    @Test
//    @DisplayName("drawing from achievement deck")
//    public void drawAchievementCardTest() {
//        Game game = new Game(2);
//        GameBoard board = game.getGameBoard();
//        LinkedList<Achievement> achievementDeck = board.getAchievementDeck();
//        Achievement firstCard = achievementDeck.getFirst();
//        Achievement drawnCard = board.drawCard();
//        assertEquals(firstCard, drawnCard);
//        assertEquals(15, achievementDeck.size());
//    }
//
//
//}
