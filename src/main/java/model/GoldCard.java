package model;

import java.util.ArrayList;
import java.util.List;

public class GoldCard extends Card {
    int basePoint;
    String condition;
    int[] cost; /** number of symbols to place the card, cost[0] = FUNGI, cost[1] = PLANT, cost[2] = ANIMAL, cost[3] = INSECT

    /**
     * Builds the front of the gold cards with corners, points and the card's cost
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     * @param basePoint point number written on the card
     * @param condition condition (if present) to calc points. For example 1 point for every visible quill
     * @param card_number unique number to distinguish through various cards
     * @param cost represents the cost, in terms of symbols, necessary to place the card
     */
    public GoldCard(Corner[] corners, int basePoint, String condition, int card_number, int[] cost){
        this.currentSide = this;
        this.front = this;
        this.corners = new Corner[4];
        this.cost = new int[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.type = "gold";
        this.basePoint = basePoint;
        this.condition = condition;
        this.card_number = card_number;
        System.arraycopy(cost, 0, this.cost, 0, 4);
        if (card_number <= 10){ /**Gold fungi retro*/
            this.retro = new CardBack(new ArrayList<Symbols>(List.of(Symbols.FUNGI)), Color.RED);
        }
        else if (card_number <= 20) /**gold plant retro*/
        {
            this.retro = new CardBack(new ArrayList<Symbols>(List.of(Symbols.PLANT)), Color.GREEN);
        }
        else if(card_number <= 30) /**gold animal retro*/
        {
            this.retro = new CardBack(new ArrayList<Symbols>(List.of(Symbols.ANIMAL)), Color.BLUE);
        }
        else /**gold insect retro*/
        {
            this.retro = new CardBack(new ArrayList<Symbols>(List.of(Symbols.INSECT)), Color.PURPLE);
        }
        this.back = false;
    }
}
