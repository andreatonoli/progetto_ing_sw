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
        );
        alreadyDrawed = new HashSet<ResourceCard>();
    }
}
