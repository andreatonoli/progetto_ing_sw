package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.util.List;

public class CardBack extends Card{
    /**
     * list of one or more symbols (from the symbols enumeration) which appear in the center of the card
     */
    private List<Symbols> centerSymbols;
    /**
     * Builds the back of the cards, even the empty corners
     * @param centerSymbols one or more symbols which appear in the center of the card
     * @param color color of the card
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     */
    public CardBack(List<Symbols> centerSymbols, Color color, Corner[] corners) {
        this.corners = new Corner[4];
        this.centerSymbols = new ArrayList<>();
        this.centerSymbols.addAll(centerSymbols);
        this.color = color;
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.back = true;
        this.type = "retro";
    }
    /**
     * Builds the back of the cards (in case all corner ar empty)
     * @param centerSymbols one or more symbols which appear in the center of the card
     * @param color color of the card
     */
    public CardBack(List<Symbols> centerSymbols, Color color) {
        this.centerSymbols = new ArrayList<>();
        this.corners = new Corner[4];
        this.centerSymbols.addAll(centerSymbols);
        this.color = color;
        for (int i = 0; i < 4; i++) {
            this.corners[i] = new Corner(Symbols.EMPTY);
        }
        this.back = true;
        this.type = "retro";
    }

    @Override
    public List<Symbols> getSymbols() {
        return this.centerSymbols;
    }
    @Override
    public boolean checkCost(Player player) {
        return true;
    }
    @Override
    public void calcPoints(Player player) { }
    @Override
    public int[] getCost(){ return new int[]{0,0,0,0}; }
    @Override
    public int getPoints(){ return 0; }
    @Override
    public Condition getCondition(){ return null; }
    @Override
    public Symbols getRequiredItem(){ return null; }
}
