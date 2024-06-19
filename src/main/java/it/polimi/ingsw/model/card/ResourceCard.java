package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;


public class ResourceCard extends Face {

    /**
     * points given by the card when placed.
     */
    private final int point;

    /**
     * Resource Card constructor.
     *
     * @param corners corners of the card.
     * @param point   points given by the card when placed.
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
