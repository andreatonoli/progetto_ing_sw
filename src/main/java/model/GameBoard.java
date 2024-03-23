package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameBoard {
    private Game game;
    private LinkedList<Achievement> achievementDeck;
    private LinkedList<Card> goldDeck;
    private LinkedList<Card> resourceDeck;
    private LinkedList <Card> starterDeck;

    /**
     * Builds game's board, its decks and links one specific board to one specific game
     * @param game game to which the board belongs
     */
    public GameBoard(Game game) throws IOException {
        this.game = game;
        try {
            Corner[] arrayCorner;
            ArrayList<Symbols> items;
            int[] cost;
            int id;
            int basePoint;
            String content;
            String colorOrSymbol;
            JSONObject jo;
            JSONArray objCost;
            JSONArray symbols;
            //creation of achievementDeck
            this.achievementDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/achievementCard.json")));
            jo = new JSONObject(content);
            items = new ArrayList<>();
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
            id=1;
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
                GoldCard gCard = new GoldCard(arrayCorner, basePoint, colorOrSymbol, id, cost);
                goldDeck.add(gCard);
                id++;
            }
            //creation of resourceDeck
            this.resourceDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/resourceCard.json")));
            jo = new JSONObject(content);
            id=1;
            for (String resourceCard : jo.keySet()) {
                symbols = jo.getJSONObject(resourceCard).getJSONArray("corners");
                arrayCorner = new Corner[symbols.length()];
                for (int i=0; i<symbols.length(); i++){
                    arrayCorner[i] = new Corner(Symbols.valueOf(symbols.getString(i)));
                }
                basePoint = jo.getJSONObject(resourceCard).getInt("point");
                ResourceCard rCard = new ResourceCard(arrayCorner, id, basePoint);
                resourceDeck.add(rCard);
                id++;
            }
            //creation of starterDeck
            this.starterDeck = new LinkedList<>();
            content = new String(Files.readAllBytes(Paths.get("src/main/input_file/starterCard.json")));
            jo = new JSONObject(content);
            id=1;
            items = new ArrayList<>();
            for (String starterCard : jo.keySet()) {
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
                StarterCard sCard = new StarterCard(arrayCorner, id, retro);
                starterDeck.add(sCard);
                id++;
            }

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
     * Receives a shuffled achivementDeck and takes the first card also removing it
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
        return starterDeck;
    }
}
