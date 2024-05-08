package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.io.Serializable;

public interface Achievement extends Serializable {
    /**
     * This method calculates the points made by a player upon completing an achievement
     * @param player to calculate the points
     */
    void calcPoints(Player player);
    /**
     * getter for the basePoint value
     * @return the achievement's point that the player will receive each time it completes the achievement.
     */
    int getPoints();
    /**
     * getter for the color value
     * @return the needed color the cards in a specific pattern have to be.
     */
    Color getColor();
    /**
     * getter for the symbols array
     * @return the needed symbols to get to complete the achievement
     */
    ArrayList<Symbols> getSymbols();
    /**
     * getter for the symbol value
     * @return the needed symbol to collect 3 times to complete the achievement
     */
    Symbols getSymbol();

}
