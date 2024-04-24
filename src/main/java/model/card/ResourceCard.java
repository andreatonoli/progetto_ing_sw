package model.card;

import model.enums.Color;
import model.enums.Condition;
import model.enums.Symbols;
import model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ResourceCard extends Card {
    /**
     * point given by the card
     */
    private int point;
    /**
     * Resource Card constructor.
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     * @param card_number unique number to distinguish through various cards
     * @param point sometimes resource cards give points when placed
     */
    public ResourceCard(Corner[] corners, int card_number, int point){
        this.currentSide = this;
        this.front = this;
        this.corners = new Corner[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.type = "resource";
        this.cardNumber = card_number;
        this.point = point;
        this.back = false;
        if (card_number <= 10) /**FUNGI retro*/
        {
            this.color = Color.RED;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.RED);
        }
        else if (card_number <= 20) /**Plant retro*/
        {
            this.color = Color.GREEN;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.PLANT)), Color.GREEN);
        }
        else if(card_number <= 30) /**Animal retro*/
        {
            this.color = Color.BLUE;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.ANIMAL)), Color.BLUE);
        }
        else /**Insect retro*/
        {
            this.color = Color.PURPLE;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.PURPLE);
        }
        this.back = false;
        this.currentSide = this;
    }
    @Override
    public List<Symbols> getSymbols(){
        if(this.back) {
            return this.currentSide.getSymbols();
        }
        return null;
    }
    @Override
    public boolean checkCost(Player player) {
        return true;
    }

    @Override
    public void calcPoints(Player player) {
        if (this.back){
            this.currentSide.calcPoints(player);
            return;
        }
        player.addPoints(this.point);
    }

    @Override
    public int[] getCost(){ return null; }
    @Override
    public int getPoints(){ return this.point; }
    @Override
    public Condition getCondition(){ return Condition.NOTHING; }
    @Override
    public Symbols getRequiredItem(){ return null; }
}
