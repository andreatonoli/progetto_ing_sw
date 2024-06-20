package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * The Achievement interface represents an achievement in the game.
 * It provides methods to get the achievement's id, calculate points, get base points,
 * get the color of the achievement, get the symbols on the card, get the symbol value,
 * and compare two achievements.
 *
 * This interface is a part of the model component in the MVC pattern.
 */
public interface Achievement extends Serializable {

    /**
     * Gets the unique identifier of the achievement.
     *
     * @return the unique identifier of the achievement.
     */
    int getId();

    /**
     * Calculates the points made by a player upon completing this achievement.
     *
     * @param player the player who is receiving the points.
     */
    void calcPoints(Player player);

    /**
     * Gets the base points of the achievement.
     *
     * @return the minimum points that the player will receive when they complete the achievement.
     */
    int getPoints();

    /**
     * Gets the color of the achievement.
     *
     * @return the color that the cards in a specific pattern have to be to complete the achievement.
     */
    Color getColor();

    /**
     * Gets the symbols on the card.
     *
     * @return the symbols that are needed to complete the achievement.
     */
    ArrayList<Symbols> getSymbols();

    /**
     * Gets the symbol value.
     *
     * @return the symbol that needs to be collected 3 times to complete the achievement.
     */
    Symbols getSymbol();

    /**
     * Compares this achievement with another to see if they are the same.
     *
     * @param achievementToCompare the achievement to compare with this one.
     * @return true if the two achievements are the same, false otherwise.
     */
    boolean equals(Achievement achievementToCompare);
}