package model;

import java.util.ArrayList;

public class AchievementItem implements Achievement{
    private int basePoint;
    private ArrayList<Symbols> symbol;
    public AchievementItem(int basePoint, ArrayList<Symbols> symbols){
        this.basePoint = basePoint;
        this.symbol = symbols;
    }
    @Override
    public void calcPoints() {

    }
}
