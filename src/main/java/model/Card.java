package model;

import java.util.List;

public abstract class Card {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type; //Non abusare => probabilmente divernterà enum
    protected Color color;
    protected int card_number;
    protected Card currentSide;
    protected boolean back;
    protected Card front;
    protected CardBack retro;
    public abstract List<Symbols> getSymbols();
    public abstract boolean checkCost(Player player);
    public abstract void calcPoint(Player player);
    public Color getColor() {
        return color;
    }
    public void setCurrentSide(){
        if (currentSide.equals(retro)){
            this.currentSide = front;
        }
        else{
            this.currentSide = retro;
        }
    }
    public Corner getCorner(CornerEnum corner){
        return currentSide.corners[corner.ordinal()];
    }
    public Symbols getCornerSymbol(CornerEnum corner){
        return currentSide.corners[corner.ordinal()].getSymbol();
    }
    public CornerState getCornerState(CornerEnum corner){
        return currentSide.corners[corner.ordinal()].getState();
    }
    public void setCornerState(CornerEnum corner, CornerState state){
        currentSide.corners[corner.ordinal()].setState(state);
    }
}
