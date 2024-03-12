package model;

public class StarterCard extends Card{
    /**
     * Starter Card constructor.
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     * @param card_number unique number to distinguish through various cards
     * @param retro back of the card
     */
    public StarterCard(Corner[] corners, int card_number, CardBack retro){
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.card_number = card_number;
        this.type = "starter";
        this.retro = retro;
    }
}
