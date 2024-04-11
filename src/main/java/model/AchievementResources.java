package model;

import java.util.HashMap;

public class AchievementResources implements Achievement{
    private int basePoint;
    private Symbols symbol;
    /**
     * Builds card which achievement is "Collect 3 symbols of the same type"
     * @param symbol symbol to collect
     */
    public AchievementResources(Symbols symbol){
        this.basePoint = 2;
        this.symbol = symbol;
    }

    /**
     * points = basePoint * (symbol_count / 3)
     * @param player on which calculate the score
     * @return amount of points done with this achievement
     */
    @Override
    public void calcPoints(Player player) {
        int point;
        point = this.basePoint * (Math.floorDiv(player.getPlayerBoard().getSymbolCount(this.symbol), 3));
        player.addPoints(point);
    }
}
