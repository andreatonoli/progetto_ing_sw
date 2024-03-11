package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private final String username;
    private boolean firstToEnd;
    private boolean firstToPlay;
    private Card[] cardInHand = new Card[3];
    private AchievementCard[] personalObj = new AchievementCard[2];
    private final StarterCard starterCard;
    private int points = 0;
    private PlayerState playerState;
    //
    private int[] SymbolCount = {0,0,0,0,0,0,0};

    /**
     * constructor of the player class:
     * @param name is the player's unique username
     * @param sCard is the player's starting card
     * @param obj array of 2 achievement card, contains the player's personal objectives
     * @param gCard is the player's initial golden card
     * @param hand array of 2 resource card, contains the player's initial resource card
     */
    public Player(String name,StarterCard sCard ,AchievementCard[] obj, GoldCard gCard,ResourceCard[] hand){
        this.username = name;
        this.starterCard = sCard;
        System.arraycopy(obj, 0, personalObj, 0, 2);
        System.arraycopy(hand, 0, cardInHand, 0, 2);
        cardInHand[2] = gCard;
    }

    public String getUsername(){
        return username;
    }

    public int getPoints(){
        return points;
    }

    public int addPoints(int pointToAdd){
        this.points = this.points + pointToAdd;
        if (this.points > 29)
            this.points = 29;
    }
}
