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
    public void calcPoints(Player player) {
        int point = 0;
        Set<Integer> keySet = player.getPlayerBoard().getPositionCardKeys();
        int[] prev = new int[2];
        int[] succ = new int[2];
        int[] coord = new int[2];
        ArrayList<int[]> marked = new ArrayList<>();
        for (Integer i : keySet)
        {
            coord[0] = i % 1024;
            coord[1] = i / 1024;
            if (player.getPlayerBoard().getCard(coord).getColor().equals(this.color)){
                if (this.color.equals(Color.RED) || this.color.equals(Color.BLUE)) {
                    prev[0] = (i % 1024) - 1;
                    prev[1] = (i / 1024) - 1;
                    succ[0] = (i % 1024) + 1;
                    succ[1] = (i / 1024) + 1;
                }
                else {
                    prev[0] = (i % 1024) - 1;
                    prev[1] = (i / 1024) + 1;
                    succ[0] = (i % 1024) + 1;
                    succ[1] = (i / 1024) - 1;
                }
                if (!marked.contains(coord) && !marked.contains(succ) && !marked.contains(prev))
                {
                    Card prec = player.getPlayerBoard().getCard(prev);
                    Card aft = player.getPlayerBoard().getCard(succ);
                    if (prec != null && aft != null){
                        if (prec.getColor().equals(this.color) && aft.getColor().equals(this.color)){
                            point += this.basePoint;
                            marked.add(prev);
                            marked.add(coord);
                            marked.add(succ);
                        }
                    }
                }
            }
        }
        player.addPoints(point);
    }
}
