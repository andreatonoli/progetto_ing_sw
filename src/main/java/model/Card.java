package model;

public abstract class Card implements ICard {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type;
    protected int card_number;
    protected Card currentSide;
    public Corner[] getCorners() {
        return corners;
    }
    public Corner getCorner(int index){
        return corners[index];
    }
}
