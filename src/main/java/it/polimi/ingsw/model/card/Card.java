package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.List;


public class Card implements Serializable {
    /**
     * Card's front face.
     */
    private final Face front;

    /**
     * Card's retro face.
     */
    private final Face retro;

    /**
     * Card's type (e.g. Resource, gold or starter).
     */
    private final String type;

    /**
     * Unique number to identify the card.
     */
    private final int cardNumber;

    /**
     * Card's color.
     */
    private final Color color;

    /**
     * Face of the card currently shown.
     */
    private Face currentSide;

    /**
     * Boolean attribute indicating whether the card is turned over.
     */
    private boolean isBack = false;

    /**
     * Card constructor that sets the class attributes, the face to be firstly shown is the front face.
     * @param front front face of the card.
     * @param retro retro face of the card.
     * @param type type of the card (e.g. resource, gold or starter).
     * @param cardNumber unique card's identifier.
     * @param color color of the card.
     */
    public Card (Face front, Face retro, String type, int cardNumber, Color color){
        this.front = front;
        this.retro = retro;
        this.type = type;
        this.cardNumber = cardNumber;
        this.color = color;
        this.currentSide = front;
    }

    /**
     * Getter of the type attribute.
     * @return the type of the card.
     */
    public String getType(){
        return this.type;
    }

    /**
     * Getter of the cardNumber attribute.
     * @return the card's id.
     */
    public int getCardNumber(){
        return this.cardNumber;
    }

    /**
     * getter of the color attribute.
     * @return the color of the card.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Return whether the card is flipped or not.
     * @return true if card is showing its front face.
     */
    public boolean isNotBack(){
        return !isBack;
    }

    /**
     * This method flips the card showing the opposite face. It changes the currentSide attribute.
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
     * getter of the symbols list.
     * @return all the symbols in the middle of the card (if present).
     */
    public List<Symbols> getSymbols(){
        return currentSide.getSymbols();
    }

    /**
     * Checks if a player could afford the potential cost to place a card.
     * @param player player to be checked.
     * @return true only if the player can place the card.
     */
    public boolean checkCost(Player player){
        return currentSide.checkCost(player);
    }

    /**
     * Calculates the points given by the card, and adds them to the player score.
     * @param player player whose score is being calculated.
     */
    public void calcPoints(Player player){
        currentSide.calcPoints(player, this);
    }

    /**
     * Getter of the potential card's cost.
     * @return the cost, in terms of symbols, necessary to place the card.
     */
    public Integer[] getCost(){
        return currentSide.getCost();
    }

    /**
     * getter of the points attribute.
     * @return the card's base points.
     */
    public int getPoints(){
        return currentSide.getPoints();
    }

    /**
     * Gets the condition to calculate the points when the card is placed.
     * @return the condition (if present) to calculates points. For example 1 point for every visible quill.
     */
    public Condition getCondition(){
        return currentSide.getCondition();
    }

    /**
     * getter of the requiredItem value.
     * @return (if present) the item needed in the ITEM condition.
     */
    public Symbols getRequiredItem() {
        return currentSide.getRequiredItem();
    }

    /**
     * Gets the corner in {@param corner} position.
     * @param corner corner position (e.g. Top-Right).
     * @return the corner in the wanted position.
     */
    public Corner getCorner(CornerEnum corner){
        return currentSide.getCorner(corner);
    }


    /**
     * getter of the symbol in one specified card's corner.
     * @param corner the position of the corner we want to get the symbol.
     * @return the symbol contained in that corner.
     */
    public Symbols getCornerSymbol(CornerEnum corner){
        return currentSide.getCornerSymbol(corner);
    }

    /**
     * getter of one corner's state.
     * @param corner the position of the corner we want to get the state.
     * @return the corner's state.
     */
    public CornerState getCornerState(CornerEnum corner){
        return currentSide.getCornerState(corner);
    }

    /**
     * set the card's corner state.
     * @param corner position of the corner we want to set the state.
     * @param state the corner's state we want to set.
     */
    public void setCornerState(CornerEnum corner, CornerState state){
        currentSide.setCornerState(corner, state);
    }

    /**
     * Checks if two cards are equals.
     * @param card which we want to compare with {@code this}.
     * @return true if the cards are equals.
     */
    public boolean equals(Card card) {
        return card.getType().equalsIgnoreCase(this.type) && (card.getCardNumber() == this.cardNumber);
    }
}
