package model;

import java.util.HashMap;
import java.util.List;

public class Player {
    private final String username;
    private boolean firstToEnd;
    private boolean firstToPlay;
    private Card[] cardInHand = new Card[3];
    private AchievementCard[] personalObj = new AchievementCard[2];
    private AchievementCard chosenObj;
    private final StarterCard starterCard;
    private int points = 0;
    private PlayerState playerState;
    private HashMap<Symbols,Integer> symbolCount;

    /**
     * constructor of the player class:
     * @param name is the player's unique username
     * @param game is referred to class game
     */
    public Player(String name, Game game)
    {
        this.username = name;

        symbolCount= new HashMap<>();

        for (int j=0; j<2; j++)
            cardInHand[j] = game.getResourceDeck().drawCard();
        cardInHand[2] = game.getGoldDeck().drawCard();
        starterCard  = game.getStarterDeck().drawCard();
        for (int j=0; j<2; j++)
            personalObj[j] = game.getAchievementDeck().drawCard();
    }

    // passa l'array da un'altra parte, lì viene fatta la decisione e poi richiama setChosenObj
    public AchievementCard[] getPersonalObj()
    {
        return personalObj;
    }


    public void setChosenObj(AchievementCard chosenObj1)
    {
        this.chosenObj = chosenObj1;
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
        if (this.points >= 20)
            firstToEnd = true;
        if (this.points > 29)
            this.points = 29;
        return this.points;
    }

    public void isFirstToPlay(String username){
        firstToPlay = this.username.equals(username);
    }

    /**
     * adder to increment the values for each symbol in the hashmap symbolCount
     * @param placedCard is the card that is getting placed
     * @param coveredCorner are the corner that are getting covered by the placedCard
     */
    public void addSymbolCount(Card placedCard, List<Corner> coveredCorner) {
        Corner[] corner = placedCard.getCorners();
        if (placedCard.back){
            for (int i=0; i<placedCard.getBack(placedCard).getSymbols().size(); i++){
                symbolCount.compute(placedCard.getBack(placedCard).getSymbols().get(i), (key, value) -> (value == null) ? 1 : value + 1);
            }
        }
        for (int i = 0; i < 4; i++) {
            symbolCount.compute(corner[i].getSymbol(), (key, value) -> (value == null) ? 1 : value + 1);
        }
        for (Corner cCorner : coveredCorner) {
            symbolCount.compute(cCorner.getSymbol(), (key, value) -> (value == null) ? -1 : value-1);
        }
    }

    public HashMap<Symbols,Integer> getSymbolCount(){
        return symbolCount;
    }
}
