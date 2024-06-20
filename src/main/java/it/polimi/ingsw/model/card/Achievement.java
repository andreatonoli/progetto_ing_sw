package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.io.Serializable;

public interface Achievement extends Serializable {

    /**
     * Gets the achievement's id.
     * @return the achievement's id.
     */
    int getId();
    /**
     * This method calculates the points made by a player upon completing an achievement.
     * @param player player who is receiving the points.
     */
    void calcPoints(Player player);
    /**
     * Gets the base points of the achievement.
     * @return the minimum points that the player will receive when it completes the achievement.
     */
    int getPoints();
    /**
     * Gets the color of the achievement.
     * @return the needed color the cards in a specific pattern have to be.
     */
    Color getColor();
    /**
     * Gets for the symbols there on the card.
     * @return the needed symbols to get to complete the achievement.
     */
    ArrayList<Symbols> getSymbols();
    /**
     * Gets for the symbol value.
     * @return the needed symbol to collect 3 times to complete the achievement.
     */
    Symbols getSymbol();

    /**
     * Compares two cards to see if there is any difference.
     * @param achievementToCompare card we want to compare with {@code this}.
     * @return true if the two cards are the same.
     */
    boolean equals(Achievement achievementToCompare);

}
