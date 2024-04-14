package model;

import java.util.List;

public abstract class Card {
    //TODO: fare in modo di sfruttare correttamente l'ereditarietà
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type; //Non abusare => probabilmente divernterà enum
    protected Color color;
    protected int card_number;
    protected Card currentSide;
    protected boolean back; //true <=> card's current side is back
    protected Card front;
    protected CardBack retro;
    public abstract List<Symbols> getSymbols();
    public abstract boolean checkCost(Player player);
    public abstract void calcPoint(Player player);
    public Color getColor() {
        return color;
    }
    public void setCurrentSide(){
        if (currentSide.back){
            this.currentSide = front;
            this.back = false;
        }
        else{
            this.currentSide = retro;
            this.back = true;
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

    /**
     * @return the type of the card (e.g. Resource, Gold)
     */
    public String getType(){
        return  this.type;
    }
}
