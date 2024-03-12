package model;

public abstract class Card implements ICard {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type; //maybe useless
    protected int card_number;
    protected Card currentSide = this;
    protected boolean back;
    protected CardBack retro;
    public Corner[] getCorners() {
        return corners;
    }
    public Corner getCorner(int index){
        return corners[index];
    }
    public void flipSide(Card card)
    {
        if (back)
        {
            card.currentSide = card;
        }
        else
        {
            card.currentSide = card.getBack(card);
        }
    }
    //FUNZIONI INITULI
    public String toInt()
    {
        return this.currentSide.type;
    }
    public CardBack getBack(Card card){
        return card.retro;
    }
}
