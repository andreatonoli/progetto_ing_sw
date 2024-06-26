package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.Symbols;

public class GoldCard extends Face {

    /**
     * Base points given by the card.
     */
    private final int basePoint;

    /**
     * Condition to fulfill to increase the points.
     */
    private final Condition condition;

    /**
     * Item required for the ITEM condition.
     */
    private final Symbols requiredItem;

    /**
     * Cost necessary to place the card.
     */
    private final Integer[] cost;

    /**
     * Builds the front of the gold cards with corners, points and the card's cost.
     *
     * @param corners      corners of the card.
     * @param basePoint    points written on the card.
     * @param condition    condition (if present) to fulfill to calculate points. For example 1 point for every visible quill.
     * @param cost         represents the cost, in terms of symbols, necessary to place the card.
     * @param requiredItem the item needed for the ITEM condition.
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

    @Override
    public void calcPoints(Player player, Card card) {
        int point = 0;

        switch (condition){
            case Condition.CORNER:
                int[] checkCoordinates = new int[2];
                int[] coord = player.getPlayerBoard().getCardCoordinates(card);

                //Checks if there is another card under that specific corner
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
