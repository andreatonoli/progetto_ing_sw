package it.polimi.ingsw.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.exceptions.JsonFileNotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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
    private Color resourceDeckRetro;
    private Color resourceGoldRetro;

    /**
     * Builds game's board, its decks and links one specific board to one specific game
     * @param game game to which the board belongs
     */
    public GameBoard(Game game) {
        this.game = game;
        try {
            Corner[] arrayCorner;
            ArrayList<Symbols> items;
            Integer[] cost;
            int basePoint;
            int cardNumber;
            Symbols conditionItem;
            String content;

            String path;
            JsonObject json;
            InputStream is;

            String colorOrSymbol;
            JsonArray objCost;
            JsonArray symbols;
            //creation of achievementDeck
            this.achievementDeck = new LinkedList<>();
            path = "json/achievementCard.json";
            is = Achievement.class.getClassLoader().getResourceAsStream(path);
            if (is == null){
                throw new JsonFileNotFoundException(path);
            }
            json = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
            JsonArray achievements = json.getAsJsonArray("achievements");
            for (JsonElement achievementCard : achievements) {
                JsonObject jo = achievementCard.getAsJsonObject();
                String type = jo.get("type").getAsString();
                if (type.startsWith("DiagonalAndL")){
                    colorOrSymbol = jo.get("color").getAsString();
                    AchievementDiagonal adCard = new AchievementDiagonal(Color.valueOf(colorOrSymbol));
                    AchievementL alCard = new AchievementL(Color.valueOf(colorOrSymbol));
                    achievementDeck.add(adCard);
                    achievementDeck.add(alCard);
                } else if (type.startsWith("Resources")) {
                    colorOrSymbol = jo.get("symbol").getAsString();
                    AchievementResources arCard = new AchievementResources(Symbols.valueOf(colorOrSymbol));
                    achievementDeck.add(arCard);
                }
                else{
                    basePoint = jo.get("basePoint").getAsInt();
                    symbols = jo.getAsJsonArray("symbols");
                    items = new ArrayList<>();
                    for (JsonElement elem : symbols){
                        items.add(Symbols.valueOf(elem.getAsString()));
                    }
                    AchievementItem aiCard = new AchievementItem(basePoint, items);
                    achievementDeck.add(aiCard);
                }
            }
            //creation of goldDeck
            this.goldDeck = new LinkedList<>();
            path = "json/goldCard.json";
            is = GoldCard.class.getClassLoader().getResourceAsStream(path);
            if (is == null){
                throw new JsonFileNotFoundException(path);
            }
            json = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
            JsonArray goldCards = json.getAsJsonArray("goldcards");
            for (JsonElement goldCard : goldCards) {
                JsonObject jo = goldCard.getAsJsonObject();
                symbols = jo.get("corners").getAsJsonArray();
                ArrayList<Corner> cornerList = new ArrayList<>();
                for (JsonElement elem : symbols){
                    cornerList.add(new Corner(Symbols.valueOf(elem.getAsString())));
                }
                arrayCorner = cornerList.toArray(new Corner[0]);
                basePoint = jo.get("basePoint").getAsInt();
                colorOrSymbol = jo.get("condition").getAsString();
                objCost = jo.get("cost").getAsJsonArray();
                ArrayList<Integer> costList = new ArrayList<>();
                for (JsonElement elem : objCost){
                    costList.add(elem.getAsInt());
                }
                cost = costList.toArray(new Integer[0]);
                cardNumber = jo.get("id").getAsInt();
                GoldCard gCard;
                if (colorOrSymbol.equals("ITEM")){
                    conditionItem = Symbols.valueOf(jo.get("symbol").getAsString());
                    gCard = new GoldCard(arrayCorner, basePoint, Condition.valueOf(colorOrSymbol), cardNumber, cost, conditionItem);
                }
                else{
                    gCard = new GoldCard(arrayCorner, basePoint, Condition.valueOf(colorOrSymbol), cardNumber, cost, null);
                }
                goldDeck.add(gCard);
            }
            //creation of resourceDeck
            this.resourceDeck = new LinkedList<>();
            path = "json/resourceCard.json";
            is = ResourceCard.class.getClassLoader().getResourceAsStream(path);
            if (is == null){
                throw new JsonFileNotFoundException(path);
            }
            json = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
            JsonArray resourceCards = json.getAsJsonArray("resourcecards");
            for (JsonElement resourceCard : resourceCards) {
                JsonObject jo = resourceCard.getAsJsonObject();
                symbols = jo.get("corners").getAsJsonArray();
                ArrayList<Corner> cornerList = new ArrayList<>();
                for (JsonElement elem : symbols){
                    cornerList.add(new Corner(Symbols.valueOf(elem.getAsString())));
                }
                arrayCorner = cornerList.toArray(new Corner[0]);
                basePoint = jo.get("point").getAsInt();
                cardNumber = jo.get("id").getAsInt();
                ResourceCard rCard = new ResourceCard(arrayCorner, cardNumber, basePoint);
                resourceDeck.add(rCard);
            }
            //creation of starterDeck
            this.starterDeck = new LinkedList<>();
            path = "json/starterCard.json";
            is = StarterCard.class.getClassLoader().getResourceAsStream(path);
            if (is == null){
                throw new JsonFileNotFoundException(path);
            }
            json = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
            JsonArray starterCards = json.getAsJsonArray("startercards");
            for (JsonElement starterCard : starterCards) {
                JsonObject jo = starterCard.getAsJsonObject();
                JsonObject retro = jo.get("retro").getAsJsonObject();
                items = new ArrayList<>();
                symbols = retro.getAsJsonArray("symbols");
                for (JsonElement elem : symbols){
                    items.add(Symbols.valueOf(elem.getAsString()));
                }
                symbols = retro.get("corners").getAsJsonArray();
                ArrayList<Corner> retroCornerList = new ArrayList<>();
                for (JsonElement elem : symbols){
                    retroCornerList.add(new Corner(Symbols.valueOf(elem.getAsString())));
                }
                arrayCorner = retroCornerList.toArray(new Corner[0]);
                CardBack back = new CardBack(items, Color.WHITE, arrayCorner);
                symbols = jo.get("corners").getAsJsonArray();
                ArrayList<Corner> cornerList = new ArrayList<>();
                for (JsonElement elem : symbols){
                    cornerList.add(new Corner(Symbols.valueOf(elem.getAsString())));
                }
                arrayCorner = cornerList.toArray(new Corner[0]);
                cardNumber = jo.get("id").getAsInt();
                StarterCard sCard = new StarterCard(arrayCorner, cardNumber, back);
                starterDeck.add(sCard);
            }
            commonResource = new Card[2];
            commonGold = new Card[2];
            commonAchievement = new Achievement[2];
        } catch (JsonFileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Receives a shuffled deck and takes the first card also removing it
     * @param deck to draw from
     * @return Card drew from the deck
     */
    public Card drawCard(LinkedList<Card> deck){
        Card drawedCard = deck.getFirst();
        deck.removeFirst();
        return drawedCard;
    }

    /**
     * Receives a shuffled achievementDeck and takes the first card also removing it
     * @return Achievement drew from the deck
     */
    public Achievement drawCard(){
        Achievement drawedCard = achievementDeck.getFirst();
        achievementDeck.removeFirst();
        return drawedCard;
    }
    public LinkedList<Achievement> getAchievementDeck() {
        return this.achievementDeck;
    }
    public LinkedList<Card> getGoldDeck() {
        return this.goldDeck;
    }
    public LinkedList<Card> getResourceDeck() {
        return this.resourceDeck;
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
    public Color getResourceDeckRetro() {
        return resourceDeckRetro;
    }

    public void setResourceDeckRetro(Color resourceDeckRetro) {
        this.resourceDeckRetro = resourceDeckRetro;
    }

    public Color getGoldDeckRetro() {
        return resourceGoldRetro;
    }

    public void setGoldDeckRetro(Color resourceGoldRetro) {
        this.resourceGoldRetro = resourceGoldRetro;
    }
}
