package model;

import java.util.ArrayList;
import java.util.List;

public class GoldCard extends Card {
    int basePoint;
    String condition;
    int[] cost; //number of symbols to place the card

    /**
     * Builds the front of the gold cards with corners, points and the card's cost
     * @param corners Array of corners
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
        //TODO: trovare soluzione migliore
        if (condition == null){
            condition = "no";
        }
        this.condition = condition;
        this.card_number = card_number;
        System.arraycopy(cost, 0, this.cost, 0, 4);
        if (card_number <= 10){ /**Gold fungi retro*/
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.RED);
        }
        else if (card_number <= 20) /**gold plant retro*/
        {
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.PLANT)), Color.GREEN);
        }
        else if(card_number <= 30) /**gold animal retro*/
        {
            this.retro = new CardBack(new ArrayList<>(List.of(Symbols.ANIMAL)), Color.BLUE);
        }
        else /**gold insect retro*/
        {
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
                if (cost[s.ordinal()] > player.getPlayerBoard().getSymbolCount().get(s))
                {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public List<Symbols> getSymbols() {
        return null;
    }
    //TODO: riscrivere calcPoint (magari fare sottoclassi)
    @Override
    public void calcPoint(Player player) {
        int point = 0;
        switch (condition){
            case "corner":
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
            case "manuscript":
                point = this.basePoint * player.getPlayerBoard().getSymbolCount().get(Symbols.MANUSCRIPT);
                break;
            case "quill":
                point = this.basePoint * player.getPlayerBoard().getSymbolCount().get(Symbols.QUILL);
                break;
            case "inkwell":
                point = this.basePoint * player.getPlayerBoard().getSymbolCount().get(Symbols.INKWELL);
                break;
            default:
                point = this.basePoint;
                break;
        }
        player.addPoints(point);
    }
}
