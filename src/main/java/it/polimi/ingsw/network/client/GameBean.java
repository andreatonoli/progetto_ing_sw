package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;

public class GameBean {
    private Card[] commonResources;
    private Card[] commonGold;
    private final Achievement[] commonAchievement;
    private Color resourceDeckRetro;
    private Color resourceGoldRetro;

    public GameBean(){
        this.commonAchievement = new Achievement[2];
        this.commonGold = new Card[2];
        this.commonResources = new Card[2];
    }

    public Card[] getCommonResources() {
        return commonResources;
    }

    public void setCommonResources(int i, Card card) {
        this.commonResources[i] = card;
    }

    public Card[] getCommonGold() {
        return commonGold;
    }

    public void setCommonGold(int i, Card card) {
        this.commonGold[i] = card;
    }

    public Achievement[] getCommonAchievement() {
        return commonAchievement;
    }

    public Color getResourceDeckRetro() {
        return resourceDeckRetro;
    }

    public void setResourceDeckRetro(Color resourceDeckRetro) {
        this.resourceDeckRetro = resourceDeckRetro;
    }

    public Color getGoldDeckRetro() {
        return resourceGoldRetro;
    }

    public void setGoldDeckRetro(Color resourceGoldRetro) {
        this.resourceGoldRetro = resourceGoldRetro;
    }
}
