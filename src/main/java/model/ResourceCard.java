package model;

import model.Card;

public class ResourceCard extends Card {
    private int point;

    /**
     * Resource Card constructor.
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     * @param card_number unique number to distinguish through various cards
     * @param point sometimes resource cards give points when placed
     */
    public ResourceCard(Corner[] corners, int card_number, int point){
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.type = "resource";
        this.card_number = card_number;
        this.point = point;
    }
}
