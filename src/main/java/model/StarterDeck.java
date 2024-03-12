package model;

import java.util.HashSet;
import java.util.Set;

public class StarterDeck implements Deck{
    private Set<StarterCard> deck;
    private HashSet<StarterCard> alreadyDrawed; //forse useless

    /**
     * builds the deck with 6 StarterCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public StarterDeck() {
        deck = Set.of(
                new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1),
                new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2),
                new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI)}, 3),
                new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL)}, 4),
                new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT)}, 5),
                new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT)}, 6)
        );
        alreadyDrawed = new HashSet<StarterCard>();
    }
}
