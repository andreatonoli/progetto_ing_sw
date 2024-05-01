package model;

import model.card.*;
import model.enums.Color;
import model.enums.Condition;
import model.enums.Symbols;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameBoard implements Serializable {
    private Game game;
    private LinkedList<Achievement> achievementDeck;
    private LinkedList<Card> goldDeck;
    private LinkedList<Card> resourceDeck;
    private LinkedList <Card> starterDeck;
    private Card[] commonResource;
    private Card[] commonGold;
    private Achievement[] commonAchievement;

    /**
     * Builds game's board, its decks and links one specific board to one specific game
     * @param game game to which the board belongs
     */
    public GameBoard(Game game) {
        this.game = game;
        try {
            Corner[] arrayCorner;
            ArrayList<Symbols> items;
            int[] cost;
            int basePoint;
            int cardNumber;
            Symbols conditionItem;
            String content;
            String colorOrSymbol;
            JSONObject jo;
            JSONArray objCost;
            JSONArray symbols;
            //creation of achievementDeck
            this.achievementDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/achievementCard.json")));
            jo = new JSONObject(content);
            for (String achievementCard : jo.keySet()) {
                if (achievementCard.startsWith("DiagonalAndL")){
                    colorOrSymbol = jo.getJSONObject(achievementCard).getString("color");
                    AchievementDiagonal adCard = new AchievementDiagonal(Color.valueOf(colorOrSymbol));
                    AchievementL alCard = new AchievementL(Color.valueOf(colorOrSymbol));
                    achievementDeck.add(adCard);
                    achievementDeck.add(alCard);
                } else if (achievementCard.startsWith("Resources")) {
                    colorOrSymbol = jo.getJSONObject(achievementCard).getString("symbol");
                    AchievementResources arCard = new AchievementResources(Symbols.valueOf(colorOrSymbol));
                    achievementDeck.add(arCard);
                }
                else{
                    basePoint = jo.getJSONObject(achievementCard).getInt("basePoint");
                    symbols = jo.getJSONObject(achievementCard).getJSONArray("symbols");
                    items = new ArrayList<>();
                    for (int i=0; i<symbols.length(); i++){
                        items.add(Symbols.valueOf(symbols.getString(i)));
                    }
                    AchievementItem aiCard = new AchievementItem(basePoint, items);
                    achievementDeck.add(aiCard);
                }
            }
            //creation of goldDeck
            this.goldDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/goldCard.json")));
            jo = new JSONObject(content);
            for (String goldCard : jo.keySet()) {
                symbols = jo.getJSONObject(goldCard).getJSONArray("corners");
                arrayCorner = new Corner[symbols.length()];
                for (int i=0; i<symbols.length(); i++){
                    arrayCorner[i] = new Corner(Symbols.valueOf(symbols.getString(i)));
                }
                basePoint = jo.getJSONObject(goldCard).getInt("basePoint");
                colorOrSymbol = jo.getJSONObject(goldCard).getString("condition");
                objCost = jo.getJSONObject(goldCard).getJSONArray("cost");
                cost = new int[objCost.length()];
                for (int i=0; i<objCost.length(); i++){
                    cost[i] = objCost.getInt(i);
                }
                cardNumber = jo.getJSONObject(goldCard).getInt("id");
                GoldCard gCard;
                if (colorOrSymbol.equals("ITEM")){
                    conditionItem = Symbols.valueOf(jo.getJSONObject(goldCard).getString("symbol"));
                    gCard = new GoldCard(arrayCorner, basePoint, Condition.valueOf(colorOrSymbol), cardNumber, cost, conditionItem);
                }
                else{
                    gCard = new GoldCard(arrayCorner, basePoint, Condition.valueOf(colorOrSymbol), cardNumber, cost, null);
                }
                goldDeck.add(gCard);
            }
            //creation of resourceDeck
            this.resourceDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/resourceCard.json")));
            jo = new JSONObject(content);
            for (String resourceCard : jo.keySet()) {
                symbols = jo.getJSONObject(resourceCard).getJSONArray("corners");
                arrayCorner = new Corner[symbols.length()];
                for (int i=0; i<symbols.length(); i++){
                    arrayCorner[i] = new Corner(Symbols.valueOf(symbols.getString(i)));
                }
                basePoint = jo.getJSONObject(resourceCard).getInt("point");
                cardNumber = jo.getJSONObject(resourceCard).getInt("id");
                ResourceCard rCard = new ResourceCard(arrayCorner, cardNumber, basePoint);
                resourceDeck.add(rCard);
            }
            //creation of starterDeck
            this.starterDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/starterCard.json")));
            jo = new JSONObject(content);
            for (String starterCard : jo.keySet()) {
                items = new ArrayList<>();
                symbols = jo.getJSONObject(starterCard).getJSONObject("retro").getJSONArray("symbols");
                for (int i=0; i<symbols.length(); i++){
                    items.add(Symbols.valueOf(symbols.getString(i)));
                }
                symbols = jo.getJSONObject(starterCard).getJSONObject("retro").getJSONArray("corners");
                arrayCorner = new Corner[symbols.length()];
                for (int i=0; i<symbols.length(); i++){
                    arrayCorner[i] = new Corner(Symbols.valueOf(symbols.getString(i)));
                }
                CardBack retro = new CardBack(items, Color.WHITE, arrayCorner);
                symbols = jo.getJSONObject(starterCard).getJSONArray("corners");
                arrayCorner = new Corner[symbols.length()];
                for (int i=0; i<symbols.length(); i++){
                    arrayCorner[i] = new Corner(Symbols.valueOf(symbols.getString(i)));
                }
                cardNumber = jo.getJSONObject(starterCard).getInt("id");
                StarterCard sCard = new StarterCard(arrayCorner, cardNumber, retro);
                starterDeck.add(sCard);
            }
            commonResource = new Card[2];
            commonGold = new Card[2];
            commonAchievement = new Achievement[2];
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Receives a shuffled deck and takes the first card also removing it
     * @param deck to draw from
     * @return Card drew from the deck
     */
    protected Card drawCard(LinkedList<Card> deck){
        Card drawedCard = deck.getFirst();
        deck.removeFirst();
        return drawedCard;
    }

    /**
     * Receives a shuffled achievementDeck and takes the first card also removing it
     * @return Achievement drew from the deck
     */
    protected Achievement drawCard(){
        Achievement drawedCard = achievementDeck.getFirst();
        achievementDeck.removeFirst();
        return drawedCard;
    }
    public LinkedList<Achievement> getAchievementDeck() {
        return achievementDeck;
    }
    public LinkedList<Card> getGoldDeck() {
        return goldDeck;
    }
    public LinkedList<Card> getResourceDeck() {
        return resourceDeck;
    }
    public LinkedList<Card> getStarterDeck() {
        return this.starterDeck;
    }

    public Card[] getCommonResource(){
        return this.commonResource;
    }

    public void setCommonResource(Card commonResource, int i){
        this.commonResource[i] = commonResource;
    }

    public Card[] getCommonGold(){
        return this.commonGold;
    }

    public void setCommonGold(Card commonGold, int i){
        this.commonGold[i] = commonGold;
    }

    public Achievement[] getCommonAchievement(){
        return this.commonAchievement;
    }

    public void setCommonAchievement(Achievement commonAchievement, int i){
        this.commonAchievement[i] = commonAchievement;
    }

    /**
     * Replaces the common resource card at indexToReplace with a new resource card drew from the deck
     * @param indexToReplace index of the CommonResource array to be replaced
     */
    public void replaceResourceCard(int indexToReplace){
        this.commonResource[indexToReplace] = this.drawCard(this.resourceDeck);
    }

    /**
     * Replaces the common gold card at indexToReplace with a new gold card drew from the deck
     * @param indexToReplace index of the CommonGold array to be replaced
     */
    public void replaceGoldCard(int indexToReplace){
        this.commonGold[indexToReplace] = this.drawCard(this.goldDeck);
    }
}
