package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;

public class AchievementResources implements Achievement{

    /**
     * Unique number to identify the card
     */
    private final int id;
    /**
     * points given upon completion of the achievement
     */
    private int basePoint;
    /**
     * indicates the symbols (from the symbols enumeration) to collect
     */
    private Symbols symbol;
    /**
     * Builds card which achievement is "Collect 3 symbols of the same type"
     * @param symbol symbol to collect
     */
    public AchievementResources(Symbols symbol, int id){
        this.basePoint = 2;
        this.symbol = symbol;
        this.id = id;
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
    public int getId(){
        return this.id;
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
