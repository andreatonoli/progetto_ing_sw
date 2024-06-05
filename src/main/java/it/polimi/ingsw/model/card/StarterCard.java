package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Condition;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.List;

public class StarterCard extends Face{
    /**
     * Starter Card constructor.
     *
     * @param corners Array of corners, corners[0] = top-left, corner[1] = top-right, corner[2] = bottom-right, corner[3] = bottom-left
     */
    public StarterCard(Corner[] corners){
        this.corners = new Corner[4];
        System.arraycopy(corners, 0, this.corners, 0, 4);
    }

}
