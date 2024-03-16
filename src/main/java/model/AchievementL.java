package model;

public class AchievementL implements Achievement{
    private int basePoint;
    private String color;
    /**
     * Builds cards which achievement is "create a L with two cards of the same color, and one different".
     * They've all the same base points (3)
     * @param color color of 2/3 of the cards
     */
    public AchievementL(String color){
        this.basePoint = 3;
        this.color = color;
    }
    @Override
    public void calcPoints() {

    }
}
