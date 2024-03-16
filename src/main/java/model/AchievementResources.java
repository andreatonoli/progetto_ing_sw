package model;

public class AchievementResources implements Achievement{
    private int basePoint;
    private Symbols symbol;
    /**
     * Builds card which achievement is "Collect 3 symbols of the same type"
     * @param symbol symbol to collect
     */
    public AchievementResources(Symbols symbol){
        this.basePoint = 2;
        this.symbol = symbol;
    }
}
