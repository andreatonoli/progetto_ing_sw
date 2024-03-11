package model;

import java.util.HashSet;
import java.util.Set;

public class ResourceDeck extends ResourceCard implements Deck{
    //Sfrutto costruttore di ResourceCard per generare 2 set, uno che contiene tutto il mazzo (non estratto), uno che contiene le carte estratte
    //Non so ancora che collection usare
    private Set<ResourceCard> deck;
    private HashSet<ResourceCard> alreadyDrawed; //forse useless

    /**
     * builds the deck with 40 ResourceCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public ResourceDeck(){
        deck = Set.of(
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(null), null, new Corner(Symbols.FUNGI) }, 1, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(null), null }, 2, 0),
                new ResourceCard(new Corner[]{new Corner(null), null, new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI)}, 3, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(null)}, 4, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.QUILL), new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT)}, 5, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), null }, 6, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.INSECT), new Corner(null), new Corner(Symbols.MANOSCRIPT) }, 7, 0),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(Symbols.FUNGI), null, new Corner(null) }, 8, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), null, new Corner(null), new Corner(null) }, 9, 1),
                new ResourceCard(new Corner[]{null, new Corner(null), new Corner(null), new Corner(Symbols.FUNGI) }, 10, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(null), null, new Corner(Symbols.PLANT) }, 11, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(null), null }, 12, 0),
                new ResourceCard(new Corner[]{new Corner(null), null, new Corner(Symbols.PLANT), new Corner(Symbols.PLANT) }, 13, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.PLANT), new Corner(Symbols.PLANT), new Corner(null) }, 14, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 15, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.PLANT), new Corner(Symbols.INKWELL), null }, 16, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.MANOSCRIPT), null, new Corner(Symbols.ANIMAL), new Corner(Symbols.PLANT) }, 17, 0),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(null), null, new Corner(Symbols.PLANT) }, 18, 1),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(null), new Corner(Symbols.PLANT), null }, 19, 1),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.PLANT), new Corner(null), new Corner(null) }, 20, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), null, new Corner(null) }, 21, 0),
                new ResourceCard(new Corner[]{null, new Corner(null), new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL) }, 22, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), null, new Corner(null), new Corner(Symbols.ANIMAL) }, 23, 0),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), null }, 24, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INKWELL) }, 25, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANOSCRIPT), null }, 26, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.QUILL), null, new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL) }, 27, 0),
                new ResourceCard(new Corner[]{null, new Corner(null), new Corner(null), new Corner(Symbols.ANIMAL) }, 28, 1),
                new ResourceCard(new Corner[]{new Corner(null), null, new Corner(Symbols.ANIMAL), new Corner(null) }, 29, 1),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(Symbols.ANIMAL), null, new Corner(null) }, 30, 1),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), null, new Corner(null) }, 31, 0),
                new ResourceCard(new Corner[]{null, new Corner(null), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT) }, 32, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), null, new Corner(null), new Corner(Symbols.INSECT) }, 33, 0),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), null }, 34, 0),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL) }, 35, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.MANOSCRIPT), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI), null }, 36, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), null, new Corner(Symbols.INKWELL) }, 37, 0),
                new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), null, new Corner(null), new Corner(null) }, 38, 1),
                new ResourceCard(new Corner[]{new Corner(null), new Corner(null), new Corner(Symbols.INSECT), null }, 39, 1),
                new ResourceCard(new Corner[]{null, new Corner(Symbols.INSECT), new Corner(null), new Corner(null) }, 40, 1)
        );
        alreadyDrawed = new HashSet<ResourceCard>();
    }
}
