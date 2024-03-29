package model;

import java.util.Objects;

public abstract class Card {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type; //maybe useless
    protected Color color;
    protected int card_number;
    protected Card currentSide = this;
    protected boolean back;
    protected CardBack retro;
    public Corner[] getCorners() {
        return corners;
    }
    public Corner getCorner(CornerEnum corner){
        return corners[corner.ordinal()];
    }
    public CardBack getBack(){
        return this.retro;
    }
    public Color getColor() {
        return color;
    }
    public boolean isBack(Card card)
    {
        return this.back;
    }
    public Symbols getCornerSymbol(CornerEnum corner){
        return this.corners[corner.ordinal()].getSymbol();
    }
    //public Card getSide(Card card);
    //public void setSide(Card card);
    //public void flipSide(Card card);
    //public void placeCard(Card card);
    //public int getPoint(Card card);
    //public void flipSide(Card card);
    //public Symbol getCardsymbol();
}
