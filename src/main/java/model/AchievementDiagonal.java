package model;

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
    @Override
    public void calcPoints() {

    }
}
