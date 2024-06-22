package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;

import java.io.Serializable;

/**
 * This class is a bean used to store the game state and the common resources, gold and achievements.
 * It collects the information about the game sent by the server, and it is used by the client to update the UI.
 */
public class GameBean implements Serializable {

    /**
     * The game state.
     */
    private GameState state;

    /**
     * The common resources.
     */
    private final Card[] commonResources;

    /**
     * The common gold.
     */
    private final Card[] commonGold;

    /**
     * The common achievements.
     */
    private final Achievement[] commonAchievement;

    /**
     * The color of the resource deck.
     */
    private Color resourceDeckRetro;

    /**
     * The color of the gold deck.
     */
    private Color resourceGoldRetro;

    /**
     * The constructor of the class. It initializes the common resources, gold and achievements and sets the game state to WAIT_PLAYERS.
     */
    public GameBean(){
        this.commonAchievement = new Achievement[2];
        this.commonGold = new Card[2];
        this.commonResources = new Card[2];
        this.state = GameState.WAIT_PLAYERS;
    }

    /**
     * This method returns the common resources.
     * @return the common resources.
     */
    public Card[] getCommonResources() {
        return commonResources;
    }

    /**
     * This method sets the common resources.
     * @param i the index of the common resources.
     * @param card the card to set.
     */
    public void setCommonResources(int i, Card card) {
        this.commonResources[i] = card;
    }

    /**
     * This method returns the common gold.
     * @return the common gold.
     */
    public Card[] getCommonGold() {
        return commonGold;
    }

    /**
     * This method sets the common gold.
     * @param i the index of the common gold.
     * @param card the card to set.
     */
    public void setCommonGold(int i, Card card) {
        this.commonGold[i] = card;
    }

    /**
     * This method returns the common achievements.
     * @return the common achievements.
     */
    public Achievement[] getCommonAchievement() {
        return commonAchievement;
    }

    /**
     * This method returns the color of the resource deck.
     * @return the color of the resource deck.
     */
    public Color getResourceDeckRetro() {
        return resourceDeckRetro;
    }

    /**
     * This method sets the color of the resource deck.
     * @param resourceDeckRetro the color of the resource deck.
     */
    public void setResourceDeckRetro(Color resourceDeckRetro) {
        this.resourceDeckRetro = resourceDeckRetro;
    }

    /**
     * This method returns the color of the gold deck.
     * @return the color of the gold deck.
     */
    public Color getGoldDeckRetro() {
        return resourceGoldRetro;
    }

    /**
     * This method sets the color of the gold deck.
     * @param resourceGoldRetro the color of the gold deck.
     */
    public void setGoldDeckRetro(Color resourceGoldRetro) {
        this.resourceGoldRetro = resourceGoldRetro;
    }

    /**
     * This method sets the common achievements.
     * @param i the index of the common achievements.
     * @param card the card to set.
     */
    public void setCommonAchievement(int i, Achievement card) {
        this.commonAchievement[i] = card;
    }

    /**
     * This method returns the game state.
     * @return the game state.
     */
    public GameState getState(){
        return state;
    }

    /**
     * This method sets the game state.
     * @param state the game state.
     */
    public void setState(GameState state){
        this.state = state;
    }
}
