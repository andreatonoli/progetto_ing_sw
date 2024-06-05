package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.List;

public abstract class Face implements Serializable {

    /**
     * array containing the current side card's corners
     */
    protected Corner[] corners;

    /**
     * getter of the symbols list
     * @return all the symbol in the center of the card
     */
    public List<Symbols> getSymbols(){
        return null;
    }

    /**
     * @param player in the one to check
     * @return if the player could handle the cost of the gold card
     */
    public boolean checkCost(Player player) {
        return true;
    }

    /**
     * used to check if the player has completed the card condition to get the points
     * @param player is the one to check
     */
    public void calcPoints(Player player, Card card){

    }

    /**
     * getter to get the cost array
     * @return the cost, in terms of symbols, necessary to place the card
     */
    public Integer[] getCost() {
        return null;
    }

    /**
     * getter to get the points value
     * @return the point number written on the card
     */
    public int getPoints() {
        return 0;
    }

    /**
     * getter tp get the condition value
     * @return the condition (if present) to calc points. For example 1 point for every visible quill
     */
    public Condition getCondition() {
        return Condition.NOTHING;
    }

    /**
     * getter to get the requiredItem value
     * @return (if present) the item needed for the Condition.ITEM
     */
    public Symbols getRequiredItem() {
        return null;
    }

    /**
     * getter to get one of the card's corner
     * @param corner is the corner's position
     * @return the corner in the wanted position.
     */
    public Corner getCorner(CornerEnum corner){
        return corners[corner.ordinal()];
    }

    /**
     * getter of the corners array
     * @return all the card's corners.
     */
    public Corner[] getCorners(){
        return this.corners;
    }

    /**
     * getter to get the symbol in the card's corner
     * @param corner is the corner's position
     * @return the symbol in the corner position.
     */
    public Symbols getCornerSymbol(CornerEnum corner){
        return corners[corner.ordinal()].getSymbol();
    }

    /**
     * getter to get the state of the card's corner
     * @param corner is the corner's position
     * @return the corner's state
     */
    public CornerState getCornerState(CornerEnum corner){
        return corners[corner.ordinal()].getState();
    }

    /**
     * setter to set the state of the card's corner
     * @param corner is the corner's position
     * @param state is the corner's state
     */
    public void setCornerState(CornerEnum corner, CornerState state){
        corners[corner.ordinal()].setState(state);
    }

}
