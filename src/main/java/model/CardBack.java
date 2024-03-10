package model;

import java.util.ArrayList;
import java.util.List;

public enum CardBack {
    PLANT (new ArrayList<Symbols>(List.of(Symbols.PLANT)), "green"),
    ANIMAL(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), "blue"),
    FUNGI(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "red"),
    INSECT(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "purple"),
    PLANT_GOLD(new ArrayList<Symbols>(List.of(Symbols.PLANT)), "green"),
    ANIMAL_GOLD(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), "blue"),
    FUNGI_GOLD(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), "red"),
    INSECT_GOLD(new ArrayList<Symbols>(List.of(Symbols.INSECT)), "purple"),
    FUNGI_START(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), null),
    INSECT_START(new ArrayList<Symbols>(List.of(Symbols.INSECT)), null),
    PLANT_FUNGI_START(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.FUNGI)), null),
    ANIMAL_INSECT_START(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT)), null),
    ANIMAL_INSECT_PLANT_START(new ArrayList<Symbols>(List.of(Symbols.ANIMAL, Symbols.INSECT, Symbols.PLANT)), null),
    PLANT_ANIMAL_FUNGI_START(new ArrayList<Symbols>(List.of(Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI)), null);
    private final List<Symbols> symbols;
    private final String color;

    /**
     * Builds the back of the cards
     * @param symbols one or more symbols which appear in the center of the card
     * @param color color of the card
     */
    private CardBack(List<Symbols> symbols, String color){
        this.symbols = new ArrayList<>();
        this.symbols.addAll(symbols);
        this.color = color;
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
