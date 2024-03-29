package model;

public abstract class Card {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type; //maybe useless
    protected Color color;
    protected int card_number;
    protected Card currentSide;
    protected boolean back; //forse useless
    protected Card front;
    protected CardBack retro;
    public Corner[] getCorners() {
        return currentSide.corners;
    }
    public Corner getCorner(CornerEnum corner){
        return currentSide.corners[corner.ordinal()];
    }
    public CardBack getBack(){
        return this.retro;
    }
    public Color getColor() {
        return color;
    }
    public Symbols getCornerSymbol(CornerEnum corner){
        return currentSide.corners[corner.ordinal()].getSymbol();
    }
    public boolean isBack(Card card)
    {
        return this.back;
    }
    //public Card getSide(Card card);
    //public void setSide(Card card);
    //public void flipSide(Card card);
    //public void placeCard(Card card);
    //public int getPoint(Card card);
    //public void flipSide(Card card);

    public int getCardNumber(){
        return this.card_number;
    }
}
