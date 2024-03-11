package model;

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

    /**
     * getter used to know the player name
     * @return player's username
     */
    public String getUsername(){
        return username;
    }

    /**
     * setter to change the player state
     * @param playerState is the new state of the player
     */
    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    /**
     * getter to get the current player state
     * @return the current player state
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * getter to get the current score of the player
     * @return the player's points
     */
    public int getPoints(){
        return points;
    }

    public int addPoints(int pointsToAdd){
        this.points = this.points + pointsToAdd;
        if (this.points > 29)
            this.points = 29;
        return this.points;
    }

    public void isFirstToPlay(String username){
        firstToPlay = this.username.equals(username);
    }

    public void isFirstToEnd(String username){
        firstToEnd = this.username.equals(username);
    }

    //manca da inserire l'aumento di punteggio per i centri delle backcard
    //e manca il caso in cui la carta copre più di un angolo
    public void addSymbolCount(Card placedCard, Corner coveredCorner) {
        Corner[] corner = placedCard.getCorners();

        for (int i = 0; i < 4; i++) {
            switch (corner[i].getSymbol()) {
                case PLANT:
                    SymbolCount[0]++;
                    break;
                case ANIMAL:
                    SymbolCount[1]++;
                    break;
                case FUNGI:
                    SymbolCount[2]++;
                    break;
                case INSECT:
                    SymbolCount[3]++;
                    break;
                case QUILL:
                    SymbolCount[4]++;
                    break;
                case INKWELL:
                    SymbolCount[5]++;
                    break;
                case MANUSCRIPT:
                    SymbolCount[6]++;
                    break;
            }
        }

        switch (coveredCorner.getSymbol()) {
            case PLANT:
                SymbolCount[0]--;
                break;
            case ANIMAL:
                SymbolCount[1]--;
                break;
            case FUNGI:
                SymbolCount[2]--;
                break;
            case INSECT:
                SymbolCount[3]--;
                break;
            case QUILL:
                SymbolCount[4]--;
                break;
            case INKWELL:
                SymbolCount[5]--;
                break;
            case MANUSCRIPT:
                SymbolCount[6]--;
                break;
        }
    }
    public int[] getSymbolCount(){
        return SymbolCount;
    }
}
