package model.card;

import model.enums.Color;
import model.player.Player;
import model.enums.Symbols;

import java.util.ArrayList;

public class AchievementItem implements Achievement{
    /**
     * points given upon completion of the achievement
     */
    private int basePoint;
    /**
     * symbols to collect
     */
    private ArrayList<Symbols> symbol;

    /**
     * Builds cards which achievement is getting some uncovered symbol
     * @param basePoint are the points that the player will get each time it completes the achievement
     * @param symbols are the needed symbols to get to complete the achievement
     */
    public AchievementItem(int basePoint, ArrayList<Symbols> symbols){
        this.basePoint = basePoint;
        this.symbol = symbols;
    }

    /**
     * if (basePoint == 3) then point = basePoint * min(Item_symbolCount)
     * if (basePoint == 2) then point = basePoint * (item_count / 2)
     * @param player on which calculate the score
     */
    @Override
    public void calcPoints(Player player) {
        int point = 0;
        int min_val = 0;
        if (this.basePoint == 3){
            if (player.getPlayerBoard().getSymbolCount(symbol.getFirst()) <= player.getPlayerBoard().getSymbolCount(symbol.get(1))){
                min_val = player.getPlayerBoard().getSymbolCount(symbol.getFirst());
            }
            else{
                min_val = player.getPlayerBoard().getSymbolCount(symbol.get(1));
            }
            if (min_val > player.getPlayerBoard().getSymbolCount(symbol.get(2))){
                min_val = player.getPlayerBoard().getSymbolCount(symbol.get(2));
            }
            point = this.basePoint * min_val;
        } else if (this.basePoint == 2) {
            point = this.basePoint * (Math.floorDiv(player.getPlayerBoard().getSymbolCount(symbol.getFirst()), 2));
        }
        player.addPoints(point);
    }

    @Override
    public int getPoints(){ return this.basePoint; }
    @Override
    public Color getColor(){ return null; }
    @Override
    public ArrayList<Symbols> getSymbols(){ return this.symbol; }
    @Override
    public Symbols getSymbol(){ return null; }
}
