package model.card;

import model.enums.*;
import model.player.Player;

import java.io.Serializable;
import java.util.List;

public abstract class Card implements Serializable {
    //TODO: fare in modo di sfruttare correttamente l'ereditarietà
    /**
     * array containing the current side card's corners
     */
    protected Corner[] corners;
    /**
     * indicates the card's type
     */
    protected String type;
    /**
     * indicates the card's color
     */
    protected Color color;
    /**
     * is the id of the card
     */
    protected int card_number;
    /**
     * card side that is currently visible
     */
    protected Card currentSide;
    /**
     * true <=> card's current side is back
     */
    protected boolean back;
    /**
     * a reference to the card's front side
     */
    protected Card front;
    /**
     * a reference to the card's back side
     */
    protected CardBack retro;


    /**
     * getter of the symbols list
     * @return all the symbol in the center of the card
     */
    public abstract List<Symbols> getSymbols();

    /**
     * @param player in the one to check
     * @return if the player could handle the cost of the gold card
     */
    public abstract boolean checkCost(Player player);

    /**
     * used to check if the player has completed the card condition to get the points
     * @param player is the one to check
     */
    public abstract void calcPoints(Player player);

    /**
     * getter to get the cost array
     * @return the cost, in terms of symbols, necessary to place the card
     */
    public abstract int[] getCost();

    /**
     * getter to get the points value
     * @return the point number written on the card
     */
    public abstract int getPoints();

    /**
     * getter tp get the condition value
     * @return the condition (if present) to calc points. For example 1 point for every visible quill
     */
    public abstract Condition getCondition();

    /**
     * getter to get the requiredItem value
     * @return (if present) the item needed for the condition.ITEM
     */
    public abstract Symbols getRequiredItem();

    /**
     * getter to get the color value
     * @return the color of the card
     */
    public Color getColor() {
        return color;
    }

    /**
     * setter to set side of the card
     */
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

    /**
     * getter to get one of the card's corner
     * @param corner is the corner's position
     * @return the corner in the wanted position.
     */
    public Corner getCorner(CornerEnum corner){
        return currentSide.corners[corner.ordinal()];
    }

    public boolean isBack(){ return this.back; }

    public CardBack getBack(){ return this.retro; }

    /**
     * getter of the corners array
     * @return all the card's corners.
     */
    public Corner[] getCorners(){ return this.corners; }

    /**
     * getter to get the symbol in the card's corner
     * @param corner is the corner's position
     * @return the symbol in the corner position.
     */
    public Symbols getCornerSymbol(CornerEnum corner){
        return currentSide.corners[corner.ordinal()].getSymbol();
    }

    /**
     * getter to get the state of the card's corner
     * @param corner is the corner's position
     * @return the corner's state
     */
    public CornerState getCornerState(CornerEnum corner){
        return currentSide.corners[corner.ordinal()].getState();
    }

    /**
     * setter to set the state of the card's corner
     * @param corner is the corner's position
     * @param state is the corner's state
     */
    public void setCornerState(CornerEnum corner, CornerState state){
        currentSide.corners[corner.ordinal()].setState(state);
    }
    /**
     * getter of the type value
     * @return the type of the card (e.g. Resource, Gold)
     */
    public String getType(){
        return  this.type;
    }
}
