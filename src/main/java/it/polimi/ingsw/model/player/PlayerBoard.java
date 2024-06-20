package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.enums.Symbols;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * The PlayerBoard class represents the game board of a player.
 * It provides methods to manage the cards and symbols of a player.
 *
 * This class is a part of the model component in the MVC pattern.
 */
public class PlayerBoard implements Serializable {
    /**
     * Offset used for calculating card positions.
     */
    public static final int OFFSET = 128;

    /**
     * The starter card of the player.
     */
    private Card starterCard;

    /**
     * A map that holds the position of each card on the board.
     */
    private final HashMap<Integer, Card> cardPosition;

    /**
     * A map that holds the count of each symbol for the player.
     */
    private final HashMap<Symbols,Integer> symbolCount;

    /**
     * Constructor for PlayerBoard.
     * Initializes the cardPosition and symbolCount maps.
     */
    public PlayerBoard(){
        this.cardPosition = new HashMap<>();
        this.symbolCount= new HashMap<>();
    }

    /**
     * Sets the position of a card on the player's board.
     *
     * @param placedCard the card that has been placed.
     * @param coordinates the position where the card has been placed.
     */
    public void setCardPosition(Card placedCard, int[] coordinates) {
        int x = coordinates[0] + OFFSET;
        int y = coordinates[1] + OFFSET;
        cardPosition.put(y + ((1<<10) * x), placedCard);
    }

    /**
     * Gets the positions of all cards on the player's board.
     *
     * @return a map of card positions.
     */
    public HashMap<Integer, Card> getCardPositon(){
        return this.cardPosition;
    }

    /**
     * Gets the coordinates of a specific card on the player's board.
     *
     * @param card the card whose position is to be retrieved.
     * @return the coordinates of the card.
     */
    public int[] getCardCoordinates(Card card){
        int[] coord = new int[]{0,0};
        for (int i : this.getPositionCardKeys()){
            if (cardPosition.get(i).equals(card)){
                coord[0] = (i / 1024) - OFFSET;
                coord[1] = (i % 1024) - OFFSET;
            }
        }
        return coord;
    }

    /**
     * Gets the card at a specific position on the player's board.
     *
     * @param coord the coordinates of the position.
     * @return the card at the given position, or null if no card is present.
     */
    public Card getCard(int[] coord){
        int x = coord[0] + OFFSET;
        int y = coord[1] + OFFSET;
        int key = y + (1<<10) * x;
        if (!this.cardPosition.containsKey(key)){
            return null;
        }
        return this.cardPosition.get(key);
    }

    /**
     * Gets the keys of the card positions on the player's board.
     *
     * @return a set of keys representing the card positions.
     */
    public Set<Integer> getPositionCardKeys(){
        return this.cardPosition.keySet();
    }

    /**
     * Gets the count of a specific symbol for the player.
     *
     * @param s the symbol whose count is to be retrieved.
     * @return the count of the symbol, or 0 if the symbol is not present.
     */
    public Integer getSymbolCount(Symbols s){
        if(this.symbolCount.get(s) != null){
            return this.symbolCount.get(s);
        }
        return 0;
    }

    /**
     * Sets the starter card for the player and places it at position (0,0).
     *
     * @param starterCard the starter card to be set.
     */
    public void setStarterCard(Card starterCard){
        this.starterCard = starterCard;
        setCardPosition(starterCard, new int[]{0,0});
        coverCorner(starterCard, new int[]{0,0});
    }

    /**
     * Gets the starter card of the player.
     *
     * @return the starter card of the player.
     */
    public Card getStarterCard(){
        return this.starterCard;
    }

    /**
     * Gives the player his starter card without placing it on the board.
     *
     * @param card the starter card to be given to the player.
     */
    public void giveStarterCard(Card card){
        this.starterCard = card;
    }

    /**
     * Increases the count of a specific symbol for the player.
     *
     * @param symbol the symbol whose count is to be increased.
     */
    public void increaseSymbolCount(Symbols symbol){
        if (!symbol.equals(Symbols.NOCORNER) && !symbol.equals(Symbols.EMPTY)){
            this.symbolCount.compute(symbol, (key, value) -> (value == null) ? 1 : value + 1);
        }
    }

    /**
     * Decreases the count of a specific symbol for the player.
     *
     * @param symbol the symbol whose count is to be decreased.
     */
    public void decreaseSymbolCount(Symbols symbol){
        if (!symbol.equals(Symbols.NOCORNER) && !symbol.equals(Symbols.EMPTY)){
            this.symbolCount.compute(symbol, (key, value) -> (value == null || value == 0) ? 0 : value - 1);
        }
    }

    /**
     * When the player board is modified, starting from the last card placed, adds its symbols to the symbol count,
     * covers the corners of the potential card below it and decrements the counter of these symbols.
     *
     * @param card the last card that was placed.
     * @param coordinates the coordinates of the last card placed.
     */
    public void coverCorner(Card card, int[] coordinates){
        int[] checkCoordinates = new int[2];
        if (card.getSymbols() != null){
            for (Symbols s : card.getSymbols()){
                this.increaseSymbolCount(s);
            }
        }
        for (CornerEnum position : CornerEnum.values()) {
            if (!card.getCornerSymbol(position).equals(Symbols.NOCORNER)){
                this.increaseSymbolCount(card.getCornerSymbol(position));
            }
            checkCoordinates[0] = coordinates[0] + position.getX();
            checkCoordinates[1] = coordinates[1] + position.getY();
            if (this.getCard(checkCoordinates) != null) {
                card.setCornerState(position, CornerState.OCCUPIED);
                this.getCard(checkCoordinates).setCornerState(position.getOppositePosition(), CornerState.NOT_VISIBLE);
                this.decreaseSymbolCount(this.getCard(checkCoordinates).getCornerSymbol(position.getOppositePosition()));
            }
        }
    }

    /**
     * Gets the positions of all cards on the player's board.
     *
     * @return a map of card positions.
     */
    public HashMap<Integer, Card> getCardPosition(){
        return cardPosition;
    }
}