package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;

public class AchievementItem implements Achievement{

    /**
     * Unique number to identify the card.
     */
    private final int id;

    /**
     * Points given upon completion of the achievement.
     */
    private final int basePoint;

    /**
     * Indicates the symbols (from the symbols enumeration) to collect.
     */
    private final ArrayList<Symbols> symbol;

    /**
     * Builds cards which achievement is collecting a certain number of symbols.
     * They've all the same base points (2 or 3).
     * @param basePoint points given upon completion of the achievement.
     * @param symbols symbols to collect.
     * @param id unique number to identify the card.
     */
    public AchievementItem(int basePoint, ArrayList<Symbols> symbols, int id){
        this.basePoint = basePoint;
        this.symbol = symbols;
        this.id = id;
    }

    /**
     * This method calculates the points made by a player. The points are calculated by multiplying the number of symbols
     * collected by the base points of the achievement.
     * @param player to calculate the points.
     */
    @Override
    public void calcPoints(Player player) {
        int point = 0;
        int min_val;
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
    public int getId(){
        return this.id;
    }
    @Override
    public int getPoints(){ return this.basePoint; }
    @Override
    public Color getColor(){ return null; }
    @Override
    public ArrayList<Symbols> getSymbols(){ return this.symbol; }
    @Override
    public Symbols getSymbol(){ return null; }

    @Override
    public boolean equals(Achievement achievementToCompare) {
        if (!this.getClass().getName().equals(achievementToCompare.getClass().getName())){
            return false;
        }
        if(achievementToCompare.getId() != this.id){
            return false;
        }
        if (this.basePoint != achievementToCompare.getPoints()){
            return false;
        }
        if (this.symbol.size() != achievementToCompare.getSymbols().size()){
            return false;
        }
        if (!this.symbol.containsAll(achievementToCompare.getSymbols())){
            return false;
        }
        return true;
    }
}
