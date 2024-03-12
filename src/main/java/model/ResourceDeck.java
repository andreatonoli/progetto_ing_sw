package model;

import java.util.*;

public class ResourceDeck implements Deck{
    private LinkedList<ResourceCard> deck;
    private Random rand = new Random();
    /**
     * builds the deck with 40 ResourceCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public ResourceDeck(){
        deck = new LinkedList<>();
        deck.addAll(List.of(
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
    }

    @Override
    public Card drawCard() throws NullPointerException{
        Card drew = null;
        try {
            int drew_index = rand.nextInt(deck.size());
            drew = deck.get(drew_index);
            deck.remove(drew_index);
        }
        catch (NullPointerException e){
            System.out.println("PC");
        }
        return drew;
    }

    @Override
    public Card drawCard(Player player) {
        int drew_index = rand.nextInt(deck.size() + 1);
        Card drew = deck.get(drew_index);
        deck.remove(drew_index);
        return drew;
    }
}
