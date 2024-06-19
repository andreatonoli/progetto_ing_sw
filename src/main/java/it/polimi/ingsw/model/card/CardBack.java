package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.util.List;

public class CardBack extends Face {

    /**
     * list of one or more symbols which appear in the middle of the card.
     */
    private final List<Symbols> centerSymbols;

    /**
     * Builds the back of the cards, and sets the corners copying the corners array.
     * @param centerSymbols one or more symbols which appear in the middle of the card.
     * @param corners array of corners.
     */
    public CardBack(List<Symbols> centerSymbols, Corner[] corners) {
        this.corners = new Corner[4];
        this.centerSymbols = new ArrayList<>();
        this.centerSymbols.addAll(centerSymbols);
        System.arraycopy(corners, 0, this.corners, 0, 4);
    }

    /**
     * Builds the back of the cards, and sets all the corners to empty.
     * @param centerSymbols one or more symbols which appear in the middle of the card.
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
