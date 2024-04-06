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
    public void calcPoints(Player player) {
        int point = 0;
        Set<Integer> keySet = player.getPlayerBoard().getPositionCardKeys();
        PlayerBoard pBoard = player.getPlayerBoard();
        int[] diff = new int[2];
        int[] coord = new int[2];
        int[] same = new int[2];
        ArrayList<int[]> marked = new ArrayList<>();
        for (Integer i : keySet){
            coord[0] = i % 1024;
            coord[1] = i / 1024;
            if (pBoard.getCard(coord).getColor().equals(this.color)){
                //Cercare un algoritmo migliore (non aver paura di creare altri metodi per semplificare)
                switch (this.color){
                    case RED:
                            same[0] = coord[0];
                            same[1] = coord[1] + 1;
                            diff[0] = coord[0] + 1;
                            diff[1] = coord[1] - 1;
                            break;
                    case BLUE:
                            same[0] = coord[0];
                            same[1] = coord[1] - 1;
                            diff[0] = coord[0] + 1;
                            diff[1] = coord[1] + 1;
                            break;
                    case GREEN:
                            same[0] = coord[0];
                            same[1] = coord[1] + 1;
                            diff[0] = coord[0] - 1;
                            diff[1] = coord[1] - 1;
                            break;
                    case PURPLE:
                            same[0] = coord[0];
                            same[1] = coord[1] - 1;
                            diff[0] = coord[0] - 1;
                            diff[1] = coord[1] + 1;
                            break;
                }
                if (!marked.contains(coord) && !marked.contains(diff) && !marked.contains(same)){
                    if (pBoard.getCard(same) != null && pBoard.getCard(diff) != null){
                        if (pBoard.getCard(same).getColor().equals(this.color) && pBoard.getCard(diff).getColor().equals(Color.getAssociatedColor(this.color))){
                            point += this.basePoint;
                            marked.add(coord);
                            marked.add(diff);
                            marked.add(same);
                        }
                    }
                }
            }
        }
        player.addPoints(point);
    }
}
