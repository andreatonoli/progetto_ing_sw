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
    private int points;
    private PlayerState playerState;
    private int[] SymbolCount = {0,0,0,0};


    public Player(String name,StarterCard sCard ,AchievementCard[] obj, GoldCard gCard,ResourceCard[] hand){
        this.username = name;
        this.starterCard = sCard;
        System.arraycopy(obj, 0, personalObj, 0, 2);
        System.arraycopy(hand, 0, cardInHand, 0, 2);
        cardInHand[2] = gCard;
    }
}
