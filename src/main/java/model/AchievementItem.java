package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AchievementItem implements Achievement{
    private int basePoint;
    private ArrayList<Symbols> symbol;
    public AchievementItem(int basePoint, ArrayList<Symbols> symbols){
        this.basePoint = basePoint;
        this.symbol = symbols;
    }

    /**
     * if (basePoint == 3) then point = basePoint * min(Item_symbolCount)
     * if (basePoint == 2) then point = basePoint * (item_count / 2)
     * @param player on which calculate the score
     * @return the amount of points done with this achievement card
     */
    @Override
    public void calcPoints(Player player) {
        int point = 0;
        int min_val = 0;
        if (this.basePoint == 3){
            if (player.getPlayerBoard().getSymbolCount().get(symbol.getFirst()) <= player.getPlayerBoard().getSymbolCount().get(symbol.get(1))){
                min_val = player.getPlayerBoard().getSymbolCount().get(symbol.getFirst());
            }
            else{
                min_val = player.getPlayerBoard().getSymbolCount().get(symbol.get(1));
            }
            if (min_val > player.getPlayerBoard().getSymbolCount().get(symbol.get(2))){
                min_val = player.getPlayerBoard().getSymbolCount().get(symbol.get(2));
            }
            point = this.basePoint * min_val;
        } else if (this.basePoint == 2) {
            point = this.basePoint * (Math.floorDiv(player.getPlayerBoard().getSymbolCount().get(symbol.getFirst()), 2));
        }
        player.addPoints(point);
    }
}
