package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Player {
    private final String username;
    private boolean firstToEnd;
    private boolean firstToPlay;
    private Card[] cardInHand = new Card[3];
    private ObjectiveCard[] personalObj = new ObjectiveCard[2];
    private final StarterCard starterCard;
    private int points;
    private PlayerState playerState;
    private int[] SymbolCount = {0,0,0,0};


    public Player(String name,StarterCard sCard ,ObjectiveCard obj1, ObjectiveCard obj2, ResourceCard rCard1, ResourceCard rCard2, GoldCard gCard){
        this.username = name;
        cardInHand[0] = rCard1;
        cardInHand[1] = rCard2;
        cardInHand[2] = gCard;
        personalObj[0] = obj1;
        personalObj[1] = obj2;
        this.starterCard = sCard;
    }
}
