package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AchievementL implements Achievement{
    private int basePoint;
    private Color color;
    /**
     * Builds cards which achievement is "create a L with two cards of the same color, and one different".
     * They've all the same base points (3)
     * @param color color of 2/3 of the cards
     */
    public AchievementL(Color color){
        this.basePoint = 3;
        this.color = color;
    }
    /**
     *This method calculates the points made by a player with the L achievement. It exploits an ArrayList to mark
     * the elements already visited. For each card it takes also the card over and under and checks
     * if they correctly reproduce the L form showed on the achievement card.
     * In that case it adds their position in the marked list and the points to the point variable
     * @param player to calculate the points
     * @return amount of points made with this achievement
     */
    @Override
    public int calcPoints(Player player) { //In questo modo il linkage dei colori è fisso
        int point = 0;
        HashMap<int [], Card> board = player.getPlayerBoard().getCardPosition();
        Set<int[]> keySet = board.keySet();
        int[] bottom = new int[2];
        int[] top = new int[2];
        ArrayList<int[]> marked = new ArrayList<>();
        switch (this.color){
            case Color.RED:
                for (int[] i : keySet){
                    if (board.get(i).getColor().equals(this.color)){
                        top[0] = i[0];
                        top[1] = i[1] + 1;
                        bottom[0] = i[0] + 1;
                        bottom[1] = i[1] - 1;
                        if (!marked.contains(i) && !marked.contains(bottom) && !marked.contains(top)){
                            if (board.get(top).getColor().equals(this.color) && board.get(bottom).getColor().equals(Color.GREEN)){
                                point += this.basePoint;
                                marked.add(i);
                                marked.add(bottom);
                                marked.add(top);
                            }
                        }
                    }
                }
                break;
            case Color.BLUE:
                for (int[] i : keySet){
                    if (board.get(i).getColor().equals(this.color)){
                        top[0] = i[0] + 1;
                        top[1] = i[1] + 1;
                        bottom[0] = i[0];
                        bottom[1] = i[1] - 1;
                        if (!marked.contains(i) && !marked.contains(bottom) && !marked.contains(top)){
                            if (board.get(bottom).getColor().equals(this.color) && board.get(top).getColor().equals(Color.RED)){
                                point += this.basePoint;
                                marked.add(i);
                                marked.add(bottom);
                                marked.add(top);
                            }
                        }
                    }
                }
                break;
            case Color.GREEN:
                for (int[] i : keySet){
                    if (board.get(i).getColor().equals(this.color)){
                        top[0] = i[0];
                        top[1] = i[1] + 1;
                        bottom[0] = i[0] - 1;
                        bottom[1] = i[1] - 1;
                        if (!marked.contains(i) && !marked.contains(bottom) && !marked.contains(top)){
                            if (board.get(top).getColor().equals(this.color) && board.get(bottom).getColor().equals(Color.PURPLE)){
                                point += this.basePoint;
                                marked.add(i);
                                marked.add(bottom);
                                marked.add(top);
                            }
                        }
                    }
                }
                break;
            case Color.PURPLE:
                for (int[] i : keySet){
                    if (board.get(i).getColor().equals(this.color)){
                        top[0] = i[0] - 1;
                        top[1] = i[1] + 1;
                        bottom[0] = i[0];
                        bottom[1] = i[1] - 1;
                        if (!marked.contains(i) && !marked.contains(bottom) && !marked.contains(top)){
                            if (board.get(bottom).getColor().equals(this.color) && board.get(top).getColor().equals(Color.BLUE)){
                                point += this.basePoint;
                                marked.add(i);
                                marked.add(bottom);
                                marked.add(top);
                            }
                        }
                    }
                }
                break;
        }
        return point;
    }
}
