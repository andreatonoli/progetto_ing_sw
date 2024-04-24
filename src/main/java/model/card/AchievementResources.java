package model.card;

import model.enums.Color;
import model.player.Player;
import model.enums.Symbols;

import java.util.ArrayList;

public class AchievementResources implements Achievement{
    /**
     * points given upon completion of the achievement
     */
    private int basePoint;
    /**
     * symbol to collect
     */
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
     */
    @Override
    public void calcPoints(Player player) {
        int point;
        point = this.basePoint * (Math.floorDiv(player.getPlayerBoard().getSymbolCount(this.symbol), 3));
        player.addPoints(point);
    }

    @Override
    public int getPoints(){ return this.basePoint; }
    @Override
    public Color getColor(){ return null; }
    @Override
    public ArrayList<Symbols> getSymbols(){ return null; }
    @Override
    public Symbols getSymbol(){ return this.symbol; }
}
