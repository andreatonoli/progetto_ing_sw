package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.util.List;

public class GoldCard extends Face {
    /**
     * point given by the card
     */
    int basePoint;
    /**
     * condition to calc points (from the condition enumeration)
     */
    Condition condition;
    /**
     * is the item (from the color enumeration) needed for the condition.ITEM
     */
    Symbols requiredItem;
    /**
     * represents the cost, in terms of symbols, necessary to place the card
     */
    Integer[] cost;

    /**
     * Builds the front of the gold cards with corners, points and the card's cost
     *
     * @param corners      Array of corners
     * @param basePoint    point number written on the card
     * @param condition    condition (if present) to calc points. For example 1 point for every visible quill
     * @param cost         represents the cost, in terms of symbols, necessary to place the card
     * @param requiredItem is the item needed for the condition.ITEM
     */
    public GoldCard(Corner[] corners, int basePoint, Condition condition, Integer[] cost, Symbols requiredItem){
        this.requiredItem = requiredItem;
        this.corners = new Corner[4];
        this.cost = new Integer[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.basePoint = basePoint;
        this.condition = condition;
        System.arraycopy(cost, 0, this.cost, 0, 4);
    }

    @Override
    public boolean checkCost(Player player) {
        for (Symbols s : Symbols.values()){
            if (s.ordinal() < 4){
                if (cost[s.ordinal()] > player.getPlayerBoard().getSymbolCount(s))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override //TODO: riscrivere bene
    public void calcPoints(Player player, Card card) {
        int point = 0;
        switch (condition){
            case Condition.CORNER:
                int[] checkCoordinates = new int[2];
                int[] coord = player.getPlayerBoard().getCardCoordinates(card);
                for (CornerEnum c: CornerEnum.values()){
                    checkCoordinates[0] = coord[0] + c.getX();
                    checkCoordinates[1] = coord[1] + c.getY();
                    if (player.getPlayerBoard().getCard(checkCoordinates) != null){
                        point += this.basePoint;
                    }
                }
                break;
            case Condition.ITEM:
                point = this.basePoint * player.getPlayerBoard().getSymbolCount(requiredItem);
                break;
            default:
                point = this.basePoint;
                break;
        }
        player.addPoints(point);
    }
    @Override
    public Integer[] getCost(){ return this.cost; }
    @Override
    public int getPoints(){ return this.basePoint; }
    @Override
    public Condition getCondition(){ return this.condition; }
    @Override
    public Symbols getRequiredItem(){ return this.requiredItem; }
}
