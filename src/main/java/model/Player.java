package model;

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

    /**
     *
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
    public void addSymbolCount(Card placedCard, Corner coveredCorner){
        int[] corner = placedCard.getCorner();

        for (int i=0; i<4; i++)
            if (corner[i].getSymbol() == 'PLANT')
                SymbolCount[0]=SymbolCount[0]+1;
            else if (corner[i].getSymbol() == 'ANIMAL')
                SymbolCount[1]=SymbolCount[1]+1;
            else if (corner[i].getSymbol() == 'FUNGI')
                SymbolCount[2]=SymbolCount[2]+1;
            else if (corner[i].getSymbol() == 'INSECT')
                SymbolCount[3]=SymbolCount[3]+1;
            else if (corner[i].getSymbol() == 'QUILL')
                SymbolCount[4]=SymbolCount[4]+1;
            else if (corner[i].getSymbol() == 'INKWELL')
                SymbolCount[5]=SymbolCount[5]+1;
            else if (corner[i].getSymbol() == 'MANUSCRIPT')
                SymbolCount[6]=SymbolCount[6]+1;

        if (coveredCorner.getSymbol() == 'PLANT')
            SymbolCount[0]=SymbolCount[0]-1;
        else if (coveredCorner.getSymbol() == 'ANIMAL')
            SymbolCount[1]=SymbolCount[1]-1;
        else if (coveredCorner.getSymbol() == 'FUNGI')
            SymbolCount[2]=SymbolCount[2]-1;
        else if (coveredCorner.getSymbol() == 'INSECT')
            SymbolCount[3]=SymbolCount[3]-1;
        else if (coveredCorner.getSymbol() == 'QUILL')
            SymbolCount[4]=SymbolCount[4]-1;
        else if (coveredCorner.getSymbol() == 'INKWELL')
            SymbolCount[5]=SymbolCount[5]-1;
        else if (coveredCorner.getSymbol() == 'MANUSCRIPT')
            SymbolCount[6]=SymbolCount[6]-1;
    }

    public int[] getSymbolCount(){
        return SymbolCount;
    }
}
