package model;

import java.util.*;
import java.util.Random;

public class Board {
    private Game game;
    private Random rand;
    private LinkedList<Achievement> achievementDeck;
    private LinkedList<GoldCard> goldDeck;
    private LinkedList<ResourceCard> resourceDeck;
    private LinkedList <StarterCard> starterDeck;

    /**
     * Builds game's board, its decks and links one specific board to one specific game
     * @param game game to which the board belongs
     */
    public Board(Game game)
    {
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
        this.goldDeck = new LinkedList<>();
        goldDeck.addAll(List.of(
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY)}, 1, "quill", 1, new int[]{2, 0, 1, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), null}, 1, "inkwell", 2, new int[]{2, 1, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY)}, 1, "manuscript", 3, new int[]{2, 0, 0, 1}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null}, 2, "corner", 4, new int[]{3, 0, 1, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY)}, 2, "corner", 5, new int[]{3, 1, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 2, "corner", 6, new int[]{3, 0, 0, 1}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, null, new Corner(Symbols.INKWELL)}, 3, null, 7, new int[]{3, 0, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY), null, null}, 3, null, 8, new int[]{3, 0, 0, 0}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY), null}, 3, null, 9, new int[]{3, 0, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, null, new Corner(Symbols.EMPTY)}, 5, null, 10, new int[]{5, 0, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY)}, 1, "quill", 11, new int[]{0, 2, 0, 1}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY), null}, 1, "manuscript", 12, new int[]{1, 2, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.INKWELL)}, 1, "inkwell", 13, new int[]{0, 2, 1, 0}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 2, "corner", 14, new int[]{0, 3, 0, 1}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY)}, 2, "corner", 15, new int[]{0, 3, 1, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 2, "corner", 16, new int[]{1, 3, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, null, new Corner(Symbols.QUILL)}, 3, null, 17, new int[]{0, 3, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY), null, null}, 3, null, 18, new int[]{0, 3, 0, 0}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), null}, 3, null, 19, new int[]{0, 3, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null}, 5, null, 20, new int[]{0, 5, 0, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY)}, 1, "inkwell", 21, new int[]{0, 0, 2, 1}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.EMPTY)}, 1, "manuscript", 22, new int[]{0, 1, 2, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL)}, 1, "quill", 23, new int[]{1, 0, 2, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null}, 2, "corner", 24, new int[]{0, 0, 3, 1}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 2, "corner", 25, new int[]{1, 0, 3, 0}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 2, "corner", 26, new int[]{0, 1, 3, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, null, new Corner(Symbols.MANUSCRIPT)}, 3, null, 27, new int[]{0, 0, 3, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.INKWELL), null, null}, 3, null, 28, new int[]{0, 0, 3, 0}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), null}, 3, null, 29, new int[]{0, 0, 3, 0}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null}, 3, null, 30, new int[]{0, 0, 5, 0}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL), new Corner(Symbols.EMPTY), null}, 1, "quill", 31, new int[]{0, 1, 0, 2}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.MANUSCRIPT)}, 1, "manuscript", 32, new int[]{0, 0, 1, 2}),
                new GoldCard(new Corner[]{null, new Corner(Symbols.EMPTY), new Corner(Symbols.INKWELL), new Corner(Symbols.EMPTY)}, 1, "inkwell", 33, new int[]{1, 0, 0, 2}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null}, 2, "corner", 34, new int[]{0, 0, 1, 3}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY)}, 2, "corner", 35, new int[]{0, 1, 0, 3}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), null, new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)}, 2, "corner", 36, new int[]{1, 0, 0, 3}),
                new GoldCard(new Corner[]{new Corner(Symbols.INKWELL), null, null, new Corner(Symbols.EMPTY)}, 3, null, 37, new int[]{0, 0, 0, 3}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.MANUSCRIPT), null, null}, 3, null, 38, new int[]{0, 0, 0, 3}),
                new GoldCard(new Corner[]{null, null, new Corner(Symbols.EMPTY), new Corner(Symbols.QUILL)}, 3, null, 39, new int[]{0, 0, 0, 3}),
                new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null}, 5, null, 40, new int[]{0, 0, 0, 5})
        ));
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
