package model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

public class AchievementDiagonal implements Achievement{
    private int basePoint;
    private Color color;
    /**
     * Builds cards which achievement is created a diagonal with cards of the same color.
     * They've all the same base points (2)
     * @param color color of the diagonal
     */
    public AchievementDiagonal(Color color){
        this.basePoint = 2;
        this.color = color;
    }

    /**
     *This method calculates the points made by a player with the diagonal achievement. It exploits an ArrayList to mark
     * the elements already visited. For each card it takes also its predecessor and it successor on the diagonal and checks
     * if they've all the same color. In that case it adds their position in the marked list
     * @param player to calculate the points
     * @return amount of points made with this achievement
     */
    @Override
    //TODO: controlla effettiva utilità di marked
    //TODO: scrivere sorting del set per velocizzare algoritmo
    public void calcPoints(Player player) {
        int point = 0;
        PlayerBoard pBoard = player.getPlayerBoard();
        Set<Integer> keySet = player.getPlayerBoard().getPositionCardKeys();
        int len = 0;
        int[] prev;
        int[] coord = new int[2];
        int[] offset = new int [2]; //distance between two elements of the diagonal
        ArrayList<int[]> marked = new ArrayList<>();
        for (Integer i : keySet)
        {
            coord[0] = i % 1024;
            coord[1] = i / 1024;
            if (pBoard.getCard(coord).getColor().equals(this.color)){
                if (this.color.equals(Color.RED) || this.color.equals(Color.BLUE)) {
                    offset[0] = -1;
                    offset[1] = -1;
                }
                else {
                    offset[0] = -1;
                    offset[1] = 1;
                }
                if (!marked.contains(coord))
                {
                    prev = coord;
                    while (pBoard.getCard(prev) != null && pBoard.getCard(prev).getColor().equals(this.color) && !marked.contains(pBoard.getCard(prev))){
                        prev[0] += offset[0];
                        prev[1] += offset[1];
                    }
                    do{
                        prev[0] -= offset[0];
                        prev[1] -= offset[1];
                        marked.add(prev);
                        len++;
                    } while(pBoard.getCard(prev) != null && pBoard.getCard(prev).getColor().equals(this.color) && !marked.contains(pBoard.getCard(prev)));
                    point += this.basePoint * (Math.floorDiv(len, 3));
                    for (int j = 0; j < len % 3; j++) {
                        marked.removeLast();
                    }
                }
            }
        }
        player.addPoints(point);
    }
}
