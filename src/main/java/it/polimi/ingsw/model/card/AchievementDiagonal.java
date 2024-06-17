package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.*;

public class AchievementDiagonal implements Achievement{

    /**
     * Unique number to identify the card.
     */
    private final int id;
    /**
     * points given upon completion of the achievement.
     */
    private final int basePoint;
    /**
     * the needed color (from the color enumeration) of the cards in a diagonal-shaped pattern have to be.
     */
    private final Color color;
    /**
     * Builds cards which achievement is creating a diagonal with cards of the same color.
     * They've all the same base points (2).
     * @param color color of the diagonal.
     */
    public AchievementDiagonal(Color color, int id){
        this.basePoint = 2;
        this.color = color;
        this.id = id;
    }

    /**
     * This method calculates the points made by a player. One diagonal is formed by three card of the same colour placed
     * diagonally.
     * For each card it takes also its predecessor and it successor on the diagonal and checks if they've all the same
     * color, then they are marked in one arrayList to avoid them to be counted twice.
     * @param player to calculate the points.
     */
    @Override
    public void calcPoints(Player player) {
        int point = 0;
        PlayerBoard pBoard = player.getPlayerBoard();
        ArrayList<Integer> sortedKeySet = new ArrayList<>(pBoard.getPositionCardKeys());
        //Direction followed on the board:
        //x: left -> right
        //y: bottom -> top
        Collections.sort(sortedKeySet);
        int[] prev;
        int[] coord = new int[2];
        int[] offset = new int [2]; //distance between two elements of the diagonal
        ArrayList<int[]> marked = new ArrayList<>();
        int len = 0;
        for (Integer i : sortedKeySet)
        {
            coord[0] = (i / 1024) - PlayerBoard.OFFSET;
            coord[1] = (i % 1024) - PlayerBoard.OFFSET;
            if (pBoard.getCard(coord).getColor().equals(this.color)){
                if (this.color.equals(Color.RED) || this.color.equals(Color.BLUE)) {
                    offset[0] = 1;
                    offset[1] = 1;
                }
                else {
                    offset[0] = 1;
                    offset[1] = -1;
                }
                if (!marked.contains(coord))
                {
                    prev = coord;
                    do{
                        prev[0] += offset[0];
                        prev[1] += offset[1];
                        marked.add(prev);
                        len++;
                    } while(pBoard.getCard(prev) != null && pBoard.getCard(prev).getColor().equals(this.color));
                    point += this.basePoint * (Math.floorDiv(len, 3));
                    for (int j = 0; j < len % 3; j++) {
                        marked.removeLast();
                    }
                }
            }
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
    public Color getColor(){ return this.color; }
    @Override
    public ArrayList<Symbols> getSymbols(){ return null; }
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
        if (!this.color.equals(achievementToCompare.getColor())){
            return false;
        }
        return true;
    }
}
