package model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class Board {
    private Game game;
    private Random rand;
    private LinkedList<Achievement> achievementDeck;
    private LinkedList<GoldCard> goldDeck;
    private LinkedList<ResourceCard> resourceDeck;
    private LinkedList <StarterCard> starterDeck;
    private String[] tmpLine;

    /**
     * Builds game's board, its decks and links one specific board to one specific game
     * @param game game to which the board belongs
     */
    public Board(Game game) throws IOException {
        rand = new Random();
        this.game = game;

        try {
            Corner[] arrayCorner;
            int[] cost;
            int id;
            int basePoint;
            BufferedReader reader;
            String line;

            //creation of achievementDeck
            this.achievementDeck = new LinkedList<>();
            tmpLine = new String[5];
            reader = new BufferedReader(new FileReader("src/main/input_file/achievementCard.txt"));
            line = reader.readLine();
            boolean first = true;
            while (line != null) {
                tmpLine = line.split(" ");
                if (tmpLine[0].equals("DiagonalAndL")) {
                    AchievementDiagonal adCard = new AchievementDiagonal(tmpLine[1]);
                    AchievementL alCard = new AchievementL(tmpLine[1]);
                    achievementDeck.add(adCard);
                    achievementDeck.add(alCard);
                } else if (tmpLine[0].equals("Resources")) {
                    AchievementResources arCard = new AchievementResources(Symbols.valueOf(tmpLine[1]));
                    achievementDeck.add(arCard);
                } else {
                    ArrayList<Symbols> items = new ArrayList<>();
                    if (first) {
                        for (int i = 2; i < tmpLine.length; i++) {
                            items.add(Symbols.valueOf(tmpLine[i]));
                        }
                        first = false;
                    } else {
                        items.add(Symbols.valueOf(tmpLine[2]));
                    }
                    AchievementItem aiCard = new AchievementItem(Integer.parseInt(tmpLine[1]), items);
                    achievementDeck.add(aiCard);
                }
                line = reader.readLine();
            }
            reader.close();

            //creation of goldDeck
            this.goldDeck = new LinkedList<>();
            cost = new int[4];
            arrayCorner = new Corner[4];
            tmpLine = new String[10];
            reader = new BufferedReader(new FileReader("src/main/input_file/goldCard.txt"));
            line = reader.readLine();
            id = 1;
            while (line != null) {
                tmpLine = line.split(" ");
                for (int i = 0; i < 4; i++) {
                    if (tmpLine[i].equals("null")) {
                        arrayCorner[i] = new Corner(Symbols.NOCORNER);
                    } else {
                        Symbols symbol = Symbols.valueOf(tmpLine[i]);
                        arrayCorner[i] = new Corner(symbol);
                    }
                }
                basePoint = Integer.parseInt(tmpLine[4]);
                for (int i = 6; i < 10; i++) {
                    cost[i - 6] = Integer.parseInt(tmpLine[i]);
                }

                GoldCard gCard = new GoldCard(arrayCorner, basePoint, tmpLine[5], id, cost);
                goldDeck.add(gCard);
                id++;
                line = reader.readLine();
            }
            reader.close();

            //creation of resourceDeck
            this.resourceDeck = new LinkedList<>();
            arrayCorner = new Corner[4];
            tmpLine = new String[5];
            reader = new BufferedReader(new FileReader("src/main/input_file/resourceCard.txt"));
            line = reader.readLine();
            id = 1;
            while (line != null) {
                tmpLine = line.split(" ");
                for (int i = 0; i < 4; i++) {
                    if (tmpLine[i].equals("null")) {
                        arrayCorner[i] = new Corner(Symbols.NOCORNER);
                    } else {
                        Symbols symbol = Symbols.valueOf(tmpLine[i]);
                        arrayCorner[i] = new Corner(symbol);
                    }
                }
                basePoint = Integer.parseInt(tmpLine[4]);
                ResourceCard rCard = new ResourceCard(arrayCorner, id, basePoint);
                resourceDeck.add(rCard);
                id++;
                line = reader.readLine();
            }
            reader.close();

            //creation of starterDeck
            this.starterDeck = new LinkedList<>();
            tmpLine = new String[13];
            reader = new BufferedReader(new FileReader("src/main/input_file/starterCard.txt"));
            line = reader.readLine();
            id = 1;
            while (line != null) {
                tmpLine = line.split(" ");
                int j;
                List<Symbols> symbols = new ArrayList<>();
                for (j = 5; !tmpLine[j].equals("|"); j++) {
                    symbols.add(Symbols.valueOf(tmpLine[j]));
                }
                j++;
                for (int i = j; i < j + 4; i++) {
                    Symbols symbol = Symbols.valueOf(tmpLine[i]);
                    arrayCorner[i] = new Corner(symbol);
                }
                CardBack retro = new CardBack(symbols, "white", arrayCorner);
                for (int i = 0; i < 4; i++) {
                    Symbols symbol = Symbols.valueOf(tmpLine[i]);
                    arrayCorner[i] = new Corner(symbol);
                }
                StarterCard sCard = new StarterCard(arrayCorner, id, retro);
                starterDeck.add(sCard);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //da far controllare a cugola tutti i draw
    public Achievement drawCardA(LinkedList<Achievement> achievementDeck) {
        Achievement drew = null;
        int drew_index = rand.nextInt(achievementDeck.size());
        drew = achievementDeck.get(drew_index);
        achievementDeck.remove(drew_index);
        return drew;
    }
    public GoldCard drawCardG(LinkedList<GoldCard> goldDeck) {
        GoldCard drew = null;
        int drew_index = rand.nextInt(goldDeck.size());
        drew = goldDeck.get(drew_index);
        goldDeck.remove(drew_index);
        return drew;
    }
    public ResourceCard drawCardR(LinkedList<ResourceCard> resourceDeck) {
        ResourceCard drew = null;
        int drew_index = rand.nextInt(resourceDeck.size());
        drew = resourceDeck.get(drew_index);
        resourceDeck.remove(drew_index);
        return drew;
    }
    public StarterCard drawCardS(LinkedList <StarterCard> starterDeck) {
        StarterCard drew = null;
        int drew_index = rand.nextInt(starterDeck.size());
        drew = starterDeck.get(drew_index);
        starterDeck.remove(drew_index);
        return drew;
    }


    public LinkedList<Achievement> getAchievementDeck() {
        return achievementDeck;
    }
    public LinkedList<GoldCard> getGoldDeck() {
        return goldDeck;
    }
    public LinkedList<ResourceCard> getResourceDeck() {
        return resourceDeck;
    }
    public LinkedList<StarterCard> getStarterDeck() {
        return starterDeck;
    }


}
