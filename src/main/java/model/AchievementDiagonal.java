package model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

public class AchievementDiagonal implements Achievement{
    private int basePoint;
    private String color;
    /**
     * Builds cards which achievement is created a diagonal with cards of the same color.
     * They've all the same base points (2)
     * @param color color of the diagonal
     */
    public AchievementDiagonal(String color){
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
    public int calcPoints(Player player) {
        int point = 0;
        HashMap<int [], Card> board = player.getPlayerBoard().getCardPosition();
        Set<int[]> keySet = board.keySet();
        int[] prev = new int[2];
        int[] succ = new int[2];
        ArrayList<int[]> marked = new ArrayList<>();
        if(this.color.equals("red") || this.color.equals("blue")){
            for (int[] i : keySet)
            {
                if (board.get(i).getColor().equals(this.color)){
                    prev[0] = i[0] - 1;
                    prev[1] = i[1] - 1;
                    succ[0] = i[0] + 1;
                    succ[1] = i[1] + 1;
                    if (!marked.contains(i) && !marked.contains(succ) && !marked.contains(prev))
                    {
                        if (board.get(prev).getColor().equals(this.color) && board.get(succ).getColor().equals(this.color)){
                            point += this.basePoint;
                            marked.add(prev);
                            marked.add(i);
                            marked.add(succ);
                        }
                    }
                }
            }
        } else if (this.color.equals("green") || this.color.equals("purple")) {
            for (int[] i : keySet)
            {
                if (board.get(i).getColor().equals(this.color)){
                    prev[0] = i[0] - 1;
                    prev[1] = i[1] + 1;
                    succ[0] = i[0] + 1;
                    succ[1] = i[1] - 1;
                    if (!marked.contains(i) && !marked.contains(succ) && !marked.contains(prev))
                    {
                        if (board.get(prev).getColor().equals(this.color) && board.get(succ).getColor().equals(this.color)){
                            point += this.basePoint;
                            marked.add(prev);
                            marked.add(i);
                            marked.add(succ);
                        }
                    }
                }
            }
        }
        return point;
    }
}
