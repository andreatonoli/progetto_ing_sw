package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;


public class ResourceCard extends Face {
    /**
     * point given by the card
     */
    private final int point;
    /**
     * Resource Card constructor.
     *
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     * @param point   sometimes resource cards give points when placed
     */
    public ResourceCard(Corner[] corners, int point){
        this.corners = new Corner[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
        this.point = point;
    }

    @Override
    public void calcPoints(Player player, Card card) {
        player.addPoints(this.point);
    }

    @Override
    public int getPoints(){ return this.point; }

}
