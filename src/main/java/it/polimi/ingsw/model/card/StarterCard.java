package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.List;

public class StarterCard extends Card{
    /**
     * Starter Card constructor.
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     * @param card_number unique number to distinguish through various cards
     * @param retro back of the card
     */
    public StarterCard(Corner[] corners, int card_number, CardBack retro){
        this.currentSide = this;
        this.front = this;
        this.corners = new Corner[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.cardNumber = card_number;
        this.type = "starter";
        this.retro = retro;
        this.back = false;
        this.color = Color.WHITE;
    }

    @Override
    public List<Symbols> getSymbols() {
        if (this.back){
            return this.currentSide.getSymbols();
        }
        return null;
    }
    @Override
    public boolean checkCost(Player player) {
        return true;
    }
    @Override
    public void calcPoints(Player player) { }
    @Override
    public Integer[] getCost(){ return null; }
    @Override
    public int getPoints(){ return 0; }
    @Override
    public Condition getCondition(){ return Condition.NOTHING; }
    @Override
    public Symbols getRequiredItem(){ return null; }
}
