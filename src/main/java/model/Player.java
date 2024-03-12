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
    private HashMap<Symbols,Integer> symbolCount= new HashMap<>();

    /**
     * constructor of the player class:
     * @param name is the player's unique username
     * @param game is referred to class game
     */
    public Player(String name, Game game)
    {
        this.username = name;

        for (int j=0; j<2; j++)
            cardInHand[j] = game.getResourceDeck().drawCard();
        cardInHand[2] = game.getGoldDeck().drawCard();
        starterCard  = game.getStarterDeck().drawCard();
        for (int j=0; j<2; j++)
            personalObj[j] = game.getAchievementDeck().drawCard();

        /*
        non so se serve visto come ho implementato l'incremento dei punti

        symbolCount.put(Symbols.FUNGI,0);
        symbolCount.put(Symbols.PLANT,0);
        symbolCount.put(Symbols.ANIMAL,0);
        symbolCount.put(Symbols.INSECT,0);
        symbolCount.put(Symbols.QUILL,0);
        symbolCount.put(Symbols.INKWELL,0);
        symbolCount.put(Symbols.MANUSCRIPT,0);
        */
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

    //manca da inserire l'aumento di punteggio per i centri delle backcard
    //e manca il caso in cui la carta copre più di un angolo
    //bisogna fare un case per il numero di angoli che sono compresi nel corner e
    //in ogni case ci vogliono N (6) case per vedere quali angoli della carta piazzata vengono usati
    public void addSymbolCount(Card placedCard, List<Corner> coveredCorner) {
        Corner[] corner = placedCard.getCorners();

        for (int i = 0; i < 4; i++) {
            switch (corner[i].getSymbol()) {
                case FUNGI:
                    symbolCount.compute(Symbols.FUNGI, (key, value) -> (value == null) ? 1 : value+1);
                    break;
                case PLANT:
                    symbolCount.compute(Symbols.PLANT, (key, value) -> (value == null) ? 1 : value+1);
                    break;
                case ANIMAL:
                    symbolCount.compute(Symbols.ANIMAL, (key, value) -> (value == null) ? 1 : value+1);
                    break;
                case INSECT:
                    symbolCount.compute(Symbols.INSECT, (key, value) -> (value == null) ? 1 : value+1);
                    break;
                case QUILL:
                    symbolCount.compute(Symbols.QUILL, (key, value) -> (value == null) ? 1 : value+1);
                    break;
                case INKWELL:
                    symbolCount.compute(Symbols.INKWELL, (key, value) -> (value == null) ? 1 : value+1);
                    break;
                case MANUSCRIPT:
                    symbolCount.compute(Symbols.MANUSCRIPT, (key, value) -> (value == null) ? 1 : value+1);
                    break;
            }
        }
        for (Corner symbol : coveredCorner) {
            switch (symbol.getSymbol()) {
                case FUNGI:
                    symbolCount.compute(Symbols.FUNGI, (key, value) -> (value == null) ? -1 : value-1);
                    break;
                case PLANT:
                    symbolCount.compute(Symbols.PLANT, (key, value) -> (value == null) ? -1 : value-1);
                    break;
                case ANIMAL:
                    symbolCount.compute(Symbols.ANIMAL, (key, value) -> (value == null) ? -1 : value-1);
                    break;
                case INSECT:
                    symbolCount.compute(Symbols.INSECT, (key, value) -> (value == null) ? -1 : value-1);
                    break;
                case QUILL:
                    symbolCount.compute(Symbols.QUILL, (key, value) -> (value == null) ? -1 : value-1);
                    break;
                case INKWELL:
                    symbolCount.compute(Symbols.INKWELL, (key, value) -> (value == null) ? -1 : value-1);
                    break;
                case MANUSCRIPT:
                    symbolCount.compute(Symbols.MANUSCRIPT, (key, value) -> (value == null) ? -1 : value-1);
                    break;
            }
        }
    }
    public HashMap<Symbols,Integer> getSymbolCount(){
        return symbolCount;
    }
}
