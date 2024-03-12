package model;

import java.util.ArrayList;
import java.util.List;

public class CardBack extends Card{
    private final List<Symbols> symbols;
    private final String color;
    public final CardBack PLANT = new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT)), "green", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack ANIMAL = new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), "blue", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack FUNGI = new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "red", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack INSECT = new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "purple", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack PLANT_GOLD = new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT)), "green", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack ANIMAL_GOLD = new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), "blue", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack FUNGI_GOLD = new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "red", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack INSECT_GOLD = new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "purple", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack FUNGI_START = new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)});
    public final CardBack INSECT_START = new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT)});
    public final CardBack PLANT_FUNGI_START = new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack ANIMAL_INSECT_START = new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY)});
    public final CardBack ANIMAL_INSECT_PLANT_START = new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null});
    public final CardBack PLANT_ANIMAL_FUNGI_START = new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), null, null});
    /**
     * Builds the back of the cards, even the empty corners
     * @param symbols one or more symbols which appear in the center of the card
     * @param color color of the card
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     */
    private CardBack(List<Symbols> symbols, String color, Corner[] corners){
        this.symbols = new ArrayList<>();
        this.symbols.addAll(symbols);
        this.color = color;
        System.arraycopy(corners, 0, this.corners, 0, 4);
    }

    /**
     * Getter functions for the params
     */
    public List<Symbols> getSymbols() {
        return symbols;
    }

    public String getColor() {
        return color;
    }
}
