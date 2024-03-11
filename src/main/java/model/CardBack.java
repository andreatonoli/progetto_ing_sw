package model;

import java.util.ArrayList;
import java.util.List;

public enum CardBack {
    PLANT (new ArrayList<Symbols>(List.of(Symbols.PLANT)), "green", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    ANIMAL(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), "blue", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    FUNGI(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "red", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    INSECT(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "purple", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    PLANT_GOLD(new ArrayList<Symbols>(List.of(Symbols.PLANT)), "green", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    ANIMAL_GOLD(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), "blue", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    FUNGI_GOLD(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "red", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    INSECT_GOLD(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "purple", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    FUNGI_START(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "white", new Corner[]{new Corner(Symbols.ANIMAL), new Corner(null), new Corner(Symbols.FUNGI), new Corner(null)}),
    INSECT_START(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "white", new Corner[]{new Corner(null), new Corner(Symbols.PLANT), new Corner(null), new Corner(Symbols.INSECT)}),
    PLANT_FUNGI_START(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.FUNGI)), "white", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    ANIMAL_INSECT_START(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT)), "white", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    ANIMAL_INSECT_PLANT_START(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT)), "white", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)}),
    PLANT_ANIMAL_FUNGI_START(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI)), "white", new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)});
    private final List<Symbols> symbols;
    private final String color;
    private Corner[] corners;

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
