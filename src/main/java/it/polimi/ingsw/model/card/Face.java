package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.List;

public abstract class Face implements Serializable {

    /**
     * Array containing the face's corners.
     */
    protected Corner[] corners;

    /**
     * Get the list of symbols present in the middle of the face.
     * @return all the symbols in the center of the face.
     */
    public List<Symbols> getSymbols(){
        return null;
    }

    /**
     * Check if the player can afford the face's cost.
     * @param player player to be checked.
     * @return  true if the face has no cost or the player can afford it.
     */
    public boolean checkCost(Player player) {
        return true;
    }

    /**
     * Calculate the points of the face when a card is placed on it and adds them to the player's score.
     * @param player player whose score is being calculated.
     * @param card card which points are calculated.
     */
    public void calcPoints(Player player, Card card){

    }

    /**
     * Getter of the face's cost.
     * @return the cost, in terms of symbols, necessary to place the card.
     */
    public Integer[] getCost() {
        return null;
    }

    /**
     * Getter of the face's base points.
     * @return the face's base points.
     */
    public int getPoints() {
        return 0;
    }

    /**
     * Gets the condition to calculate the points when the card is placed.
     * @return the condition (if present) to calculates points. For example 1 point for every visible quill.
     */
    public Condition getCondition() {
        return Condition.NOTHING;
    }

    /**
     * Gets the item needed to fulfill the condition.
     * @return (if present) the item needed in the ITEM condition.
     */
    public Symbols getRequiredItem() {
        return null;
    }

    /**
     * Gets the corner in {@param corner} position.
     * @param corner corner position (e.g. Top-Right).
     * @return the corner in the wanted position.
     */
    public Corner getCorner(CornerEnum corner){
        return corners[corner.ordinal()];
    }

    /**
     * Getter of the symbol in one specified card's corner.
     * @param corner the position of the corner we want to get the symbol.
     * @return the symbol contained in that corner.
     */
    public Symbols getCornerSymbol(CornerEnum corner){
        return corners[corner.ordinal()].getSymbol();
    }

    /**
     * Getter of one corner's state.
     * @param corner the position of the corner we want to get the state.
     * @return the corner's state.
     */
    public CornerState getCornerState(CornerEnum corner){
        return corners[corner.ordinal()].getState();
    }

    /**
     * Set the card's corner state.
     * @param corner position of the corner we want to set the state.
     * @param state the corner's state we want to set.
     */
    public void setCornerState(CornerEnum corner, CornerState state){
        corners[corner.ordinal()].setState(state);
    }

}
