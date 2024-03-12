package model;

import java.util.*;

public class StarterDeck extends Deck{
    private LinkedList<StarterCard> deck;
    /**
     * builds the deck with 6 StarterCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public StarterDeck() {
        deck = new LinkedList<>();
        deck.addAll(List.of(
                new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT)}, 1, new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)})),
                new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)})),
                new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT), new Corner(Symbols.FUNGI)}, 3, new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)})),
                new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL)}, 4, new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)})),
                new StarterCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT)}, 5, new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null})),
                new StarterCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT)}, 6, new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null}))
        ));
    }
    public StarterCard drawCard() {
        StarterCard drew = null;
        int drew_index = rand.nextInt(deck.size());
        drew = deck.get(drew_index);
        deck.remove(drew_index);
        return drew;
    }

    public StarterCard drawCard(Player player) {
        StarterCard drew = null;
        int drew_index = rand.nextInt(deck.size() + 1);
        drew = deck.get(drew_index);
        deck.remove(drew_index);
        return drew;
    }
}
