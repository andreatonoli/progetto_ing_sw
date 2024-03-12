package model;

import java.util.HashSet;
import java.util.Set;

public class GoldDeck implements Deck{
    private Set<GoldCard> deck;
    private HashSet<GoldCard> alreadyDrawed; //forse useless
    //cost[0] = FUNGI, cost[1] = PLANT, cost[2] = ANIMAL, cost[3] = INSECT

    /**
     * builds the deck with 40 GoldCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public GoldDeck() {
        deck = Set.of(
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
        );
        alreadyDrawed = new HashSet<GoldCard>();
    }
}
