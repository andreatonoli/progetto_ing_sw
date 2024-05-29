package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.util.List;

public class CardBack extends Face {
    /**
     * list of one or more symbols (from the symbols enumeration) which appear in the center of the card
     */
    private final List<Symbols> centerSymbols;
    /**
     * Builds the back of the cards, even the empty corners
     *
     * @param centerSymbols one or more symbols which appear in the center of the card
     * @param corners       Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     */
    public CardBack(List<Symbols> centerSymbols, Corner[] corners) {
        this.corners = new Corner[4];
        this.centerSymbols = new ArrayList<>();
        this.centerSymbols.addAll(centerSymbols);
        System.arraycopy(corners, 0, this.corners, 0, 4);
    }
    /**
     * Builds the back of the cards (in case all corner ar empty)
     *
     * @param centerSymbols one or more symbols which appear in the center of the card
     */
    public CardBack(List<Symbols> centerSymbols) {
        this.centerSymbols = new ArrayList<>();
        this.corners = new Corner[4];
        this.centerSymbols.addAll(centerSymbols);
        for (int i = 0; i < 4; i++) {
            this.corners[i] = new Corner(Symbols.EMPTY);
        }
    }

    @Override
    public List<Symbols> getSymbols() {
        return this.centerSymbols;
    }
}
