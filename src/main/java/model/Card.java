package model;

public abstract class Card implements ICard {
    protected Corner[] corners;
    /** Card's ID is composed of its type and its card_number value */
    protected String type; //maybe useless
    protected int card_number;
    protected Card currentSide = null;
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
        if (card.currentSide.back)
        {
            System.out.println("Qua entro");
            card.currentSide = card;
        }
        else
        {
            System.out.println("Qua non entro");
            card.currentSide = retro;
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
