package it.polimi.ingsw.model.card;

public class StarterCard extends Face{

    /**
     * Starter Card constructor.
     *
     * @param corners corners of the card.
     */
    public StarterCard(Corner[] corners){
        this.corners = new Corner[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
    }

}
