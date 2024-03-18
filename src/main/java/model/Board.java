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
    private String[] tmpLine = new String[10];
    private Corner[] arrayCorner = new Corner[4];
    int[] cost = new int[4];
    BufferedReader reader;

    /**
     * Builds game's board, its decks and links one specific board to one specific game
     * @param game game to which the board belongs
     */
    public Board(Game game) throws IOException {
        rand = new Random();
        this.game = game;
        this.achievementDeck = new LinkedList<Achievement>();
        achievementDeck.addAll(List.of(
                new AchievementDiagonal("red"),
                new AchievementDiagonal("green"),
                new AchievementDiagonal("blue"),
                new AchievementDiagonal("purple"),
                new AchievementL("red"),
                new AchievementL("green"),
                new AchievementL("blue"),
                new AchievementL("purple"),
                new AchievementResources(Symbols.FUNGI),
                new AchievementResources(Symbols.PLANT),
                new AchievementResources(Symbols.ANIMAL),
                new AchievementResources(Symbols.INSECT),
                new AchievementItem(3, new ArrayList<Symbols>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT))),
                new AchievementItem(2, new ArrayList<Symbols>(List.of(Symbols.MANUSCRIPT))),
                new AchievementItem(2, new ArrayList<Symbols>(List.of(Symbols.INKWELL))),
                new AchievementItem(2, new ArrayList<Symbols>(List.of(Symbols.QUILL)))
        ));


        goldDeck = new LinkedList<>();
        reader = new BufferedReader(new FileReader("src/main/input_file/goldCard.txt"));
        String line = reader.readLine();
        int id = 1;
        int basePoint;
        while (line != null) {
            tmpLine = line.split("\s");
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


        this.resourceDeck = new LinkedList<>();
        resourceDeck.addAll(List.of(
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI) }, 1, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), null }, 2, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI)}, 3, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}, 4, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.QUILL), new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT)}, 5, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), null }, 6, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY), new Corner(Symbols.MANUSCRIPT) }, 7, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), null, new Corner(Symbols.EMPTY) }, 8, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 9, 1),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI) }, 10, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), null, new Corner(Symbols.PLANT) }, 11, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), null }, 12, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.PLANT), new Corner(Symbols.PLANT) }, 13, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY) }, 14, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.INKWELL), null }, 16, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), null, new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, new Corner(Symbols.PLANT) }, 18, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), null }, 19, 1),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 20, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), null, new Corner(Symbols.EMPTY) }, 21, 0),
                new ResourceCard(new Corner[]{null, new Corner(null), new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL) }, 22, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), null, new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL) }, 23, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), null }, 24, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INKWELL) }, 25, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANUSCRIPT), null }, 26, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), null, new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL) }, 27, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL) }, 28, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY) }, 29, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), null, new Corner(Symbols.EMPTY) }, 30, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), null, new Corner(Symbols.EMPTY) }, 31, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT) }, 32, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), null, new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT) }, 33, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), null }, 34, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL) }, 35, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), null }, 36, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), null, new Corner(Symbols.INKWELL) }, 37, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 38, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), null }, 39, 1),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.INSECT), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY) }, 40, 1)
        ));
        starterDeck = new LinkedList<>();
        starterDeck.addAll(List.of(
                new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)})),
                new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)})),
                new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI)}, 3, new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)})),
                new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL)}, 4, new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)})),
                new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT)}, 5, new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null})),
                new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT)}, 6, new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null}))
        ));
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
