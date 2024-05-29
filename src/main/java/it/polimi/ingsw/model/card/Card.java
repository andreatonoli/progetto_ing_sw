package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.List;


public class Card implements Serializable {
    private final Face front;
    private final Face retro;
    private final String type;
    private final int cardNumber;
    private final Color color;
    private Face currentSide;
    private boolean isBack = false;
    public Card (Face front, Face retro, String type, int cardNumber, Color color){
        this.front = front;
        this.retro = retro;
        this.type = type;
        this.cardNumber = cardNumber;
        this.color = color;
        this.currentSide = front;
    }

    public String getType(){
        return this.type;
    }

    public int getCardNumber(){
        return this.cardNumber;
    }

    /**
     * getter to get the color value
     * @return the color of the card
     */
    public Color getColor() {
        return this.color;
    }
    
    public boolean isNotBack(){
        return !isBack;
    }

    /**
     * setter to set side of the card
     */
    public void setCurrentSide(){
        if (this.isBack){
            this.currentSide = front;
            this.isBack = false;
        }
        else{
            this.currentSide = retro;
            this.isBack = true;
        }
    }

    /**
     * getter of the symbols list
     * @return all the symbol in the center of the card
     */
    public List<Symbols> getSymbols(){
        return currentSide.getSymbols();
    }

    /**
     * @param player in the one to check
     * @return if the player could handle the cost of the gold card
     */
    public boolean checkCost(Player player){
        return currentSide.checkCost(player);
    }

    /**
     * used to check if the player has completed the card condition to get the points
     * @param player is the one to check
     */
    public void calcPoints(Player player){
        currentSide.calcPoints(player, this);
    }

    /**
     * getter to get the cost array
     * @return the cost, in terms of symbols, necessary to place the card
     */
    public Integer[] getCost(){
        return currentSide.getCost();
    }

    /**
     * getter to get the points value
     * @return the point number written on the card
     */
    public int getPoints(){
        return currentSide.getPoints();
    }

    /**
     * getter tp get the condition value
     * @return the condition (if present) to calc points. For example 1 point for every visible quill
     */
    public Condition getCondition(){
        return currentSide.getCondition();
    }

    /**
     * getter to get the requiredItem value
     * @return (if present) the item needed for the condition.ITEM
     */
    public Symbols getRequiredItem() {
        return currentSide.getRequiredItem();
    }

    /**
     * getter to get one of the card's corner
     * @param corner is the corner's position
     * @return the corner in the wanted position.
     */
    public Corner getCorner(CornerEnum corner){
        return currentSide.corners[corner.ordinal()];
    }

    /**
     * getter of the corners array
     * @return all the card's corners.
     */
    public Corner[] getCorners(){
        return currentSide.getCorners();
    }

    /**
     * getter to get the symbol in the card's corner
     * @param corner is the corner's position
     * @return the symbol in the corner position.
     */
    public Symbols getCornerSymbol(CornerEnum corner){
        return currentSide.getCornerSymbol(corner);
    }

    /**
     * getter to get the state of the card's corner
     * @param corner is the corner's position
     * @return the corner's state
     */
    public CornerState getCornerState(CornerEnum corner){
        return currentSide.getCornerState(corner);
    }

    /**
     * setter to set the state of the card's corner
     * @param corner is the corner's position
     * @param state is the corner's state
     */
    public void setCornerState(CornerEnum corner, CornerState state){
        currentSide.setCornerState(corner, state);
    }

    /**
     * Checks if two cards are equals
     * @param card which we want to compare with {@code this}
     * @return true if the cards are equals
     */
    public boolean equals(Card card) {
        return card.getType().equalsIgnoreCase(this.type) && (card.getCardNumber() == this.cardNumber);
    }
}
