package model;

import java.util.Map;
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

    private HashMap<int[], Card> cardPosition;

    /**
     * constructor of the player class:
     * @param name is the player's unique username
     * @param game is referred to class game
     */
    public Player(String name, Game game, Board board)
    {
        cardPosition = new HashMap<>();

        this.username = name;

        symbolCount= new HashMap<>();

        for (int j=0; j<2; j++)
            cardInHand[j] = board.drawCardR(board.getResourceDeck());
        cardInHand[2] = board.drawCardG(board.getGoldDeck());
        starterCard  = board.drawCardS(board.getStarterDeck());
        for (int j=0; j<2; j++)
            personalObj[j] = board.drawCardA(board.getAchievementDeck());


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
            for (int i=0; i<placedCard.getBack().getSymbols().size(); i++){
                symbolCount.compute(placedCard.getBack().getSymbols().get(i), (key, value) -> (value == null) ? 1 : value + 1);
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

    /**
     * requires valid coordinates.
     * the controller will place the card wherever is possible and then call this method to update the game board
     * @param placedCard is the card that has been placed
     * @param coordinates is the position where the card has been placed
     */
    public void setCardPosition(Card placedCard, int[] coordinates) {
        cardPosition.put(coordinates,placedCard);
    }

    /**
     * method to get the coordinates of the card
     * @param card is the card we need to know the position of
     * @return the position of the card
     */
    public int[] getCardPosition(Card card){
        for (Map.Entry<int[], Card> entry : cardPosition.entrySet()){
            if (entry.getValue()==card){
                return entry.getKey();
            }
        }
        return new int[]{0,0};
    }

}





