package model;

import java.util.ArrayList;
import java.util.List;

public class CardBack extends Card{
    private final List<Symbols> symbols;
    /**
     * Builds the back of the cards, even the empty corners
     * @param symbols one or more symbols which appear in the center of the card
     * @param color color of the card
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     */
    public CardBack(List<Symbols> symbols, Color color, Corner[] corners) {
        this.corners = new Corner[4];
        this.symbols = new ArrayList<>();
        this.symbols.addAll(symbols);
        this.color = color;
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.back = true;
        this.type = "retro";
    }
    public CardBack(List<Symbols> symbols, Color color)
    {
        this.symbols = new ArrayList<>();
        this.corners = new Corner[4];
        this.symbols.addAll(symbols);
        this.color = color;
        for (int i = 0; i < 4; i++) {
            this.corners[i] = new Corner(Symbols.EMPTY);
        }
        this.back = true;
        this.type = "retro";
    }

    /**
     * Getter functions for the params
     */
    public List<Symbols> getSymbols() {
        return symbols;
    }

}
