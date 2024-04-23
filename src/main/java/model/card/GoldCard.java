package model.card;

import model.enums.Color;
import model.enums.Condition;
import model.enums.CornerEnum;
import model.enums.Symbols;
import model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GoldCard extends Card {
    int basePoint;
    Condition condition;
    Symbols requiredItem;
    int[] cost; //number of symbols to place the card

    /**
     * Builds the front of the gold cards with corners, points and the card's cost
     * @param corners Array of corners
     * @param basePoint point number written on the card
     * @param condition condition (if present) to calc points. For example 1 point for every visible quill
     * @param card_number unique number to distinguish through various cards
     * @param cost represents the cost, in terms of symbols, necessary to place the card
     */
    public GoldCard(Corner[] corners, int basePoint, Condition condition, int card_number, int[] cost, Symbols requiredItem){
        this.currentSide = this;
        this.requiredItem = requiredItem;
        this.front = this;
        this.corners = new Corner[4];
        this.cost = new int[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.type = "gold";
        this.basePoint = basePoint;
        this.condition = condition;
        this.card_number = card_number;
        this.back = false;
        System.arraycopy(cost, 0, this.cost, 0, 4);
        if (card_number <= 10){ /**Gold fungi retro*/
            this.color = Color.RED;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.RED);
        }
        else if (card_number <= 20) /**gold plant retro*/
        {
            this.color = Color.GREEN;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.PLANT)), Color.GREEN);
        }
        else if(card_number <= 30) /**gold animal retro*/
        {
            this.color = Color.BLUE;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.ANIMAL)), Color.BLUE);
        }
        else /**gold insect retro*/
        {
            this.color = Color.PURPLE;
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.INSECT)), Color.PURPLE);
        }
        this.back = false;
    }

    /**
     * @return if the player could handle the cost of the gold card
     */
    //TODO: riscrivere meglio questa funzione
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
    public List<Symbols> getSymbols() {
        if (this.back){
            return this.currentSide.getSymbols();
        }
        return null;
    }
    @Override //TODO: riscrivere bene
    public void calcPoints(Player player) {
        if (this.back){
            this.currentSide.calcPoints(player);
            return;
        }
        int point = 0;
        switch (condition){
            case Condition.CORNER:
                int[] checkCoordinates = new int[2];
                int[] coord = player.getPlayerBoard().getCardCoordinates(this);
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
    public int[] getCost(){ return this.cost; }
    @Override
    public int getPoints(){ return this.basePoint; }
    @Override
    public Condition getCondition(){ return this.condition; }
    @Override
    public Symbols getRequiredItem(){ return this.requiredItem; }
}
