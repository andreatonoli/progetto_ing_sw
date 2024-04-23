package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class PlayerBoard implements Serializable {
    public static final int OFFSET = 128;
    private Player player;
    private Card starterCard; //Se si vuole far tornare di tipo starter bisogna creare un metodo drawCard apposito per lei
    private HashMap<Integer, Card> cardPosition;
    private HashMap<Symbols,Integer> symbolCount;

    public PlayerBoard(){

        this.cardPosition = new HashMap<>();
        this.symbolCount= new HashMap<>();
    }
    // passa l'array da un'altra parte, lì viene fatta la decisione e poi richiama setChosenObj
    /**
     * requires valid coordinates.
     * the controller will place the card wherever is possible and then call this method to update the game board
     * @param placedCard is the card that has been placed
     * @param coordinates is the position where the card has been placed
     */
    public void setCardPosition(Card placedCard, int[] coordinates) {
        int x = coordinates[0] + OFFSET;
        int y = coordinates[1] + OFFSET;
        cardPosition.put(y + ((1<<10) * x), placedCard);
    }

    public HashMap<Integer, Card> getCardPositon(){
        return this.cardPosition;
    }

    /**
     * method to get the coordinates of the card
     * @param card is the card we need to know the position of
     * @return the position of the card
     */
    //TODO: da modificare
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
    public Card getCard(int[] coord){
        int x = coord[0] + OFFSET;
        int y = coord[1] + OFFSET;
        int key = y + (1<<10) * x;
        if (!this.cardPosition.containsKey(key)){
            return null;
        }
        return this.cardPosition.get(key);
    }
    public Set<Integer> getPositionCardKeys(){
        return this.cardPosition.keySet();
    }
    public Integer getSymbolCount(Symbols s){
        if(this.symbolCount.get(s) != null){
            return this.symbolCount.get(s);
        }
        return 0;
    }
    public void setStarterCard(Card starterCard){
        this.starterCard = starterCard;
        setCardPosition(starterCard, new int[]{0,0});
        coverCorner(starterCard, new int[]{0,0});
    }

    public Card getStarterCard(){
        return this.starterCard;
    }

    public void increaseSymbolCount(Symbols symbol){
        if (!symbol.equals(Symbols.NOCORNER) || !symbol.equals(Symbols.EMPTY)){
            this.symbolCount.compute(symbol, (key, value) -> (value == null) ? 1 : value + 1);
        }
    }
    public void decreaseSymbolCount(Symbols symbol){
        if (!symbol.equals(Symbols.NOCORNER)|| !symbol.equals(Symbols.EMPTY)){
            this.symbolCount.compute(symbol, (key, value) -> (value == null || value == 0) ? 0 : value - 1);
        }
    }

    /**
     * When the player board is modified, starting from the last card placed, adds its symbols to the symbol count,
     * covers the corners of the potential card below it and decrements the counter of these symbols
     * @param card last card that was placed
     * @param coordinates of the last card placed
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
                //Add symbols (of the placed card) to counter
                this.increaseSymbolCount(card.getCornerSymbol(position));
            }
            //select the card below the placed card
            checkCoordinates[0] = coordinates[0] + position.getX();
            checkCoordinates[1] = coordinates[1] + position.getY();
            //Cover its corner and removes its symbols from the counter, also cover those placedCard corners which have
            //another corner below them
            if (this.getCard(checkCoordinates) != null) {
                card.setCornerState(position, CornerState.OCCUPIED);
                this.getCard(checkCoordinates).setCornerState(position.getOppositePosition(), CornerState.NOT_VISIBLE);
                this.decreaseSymbolCount(this.getCard(checkCoordinates).getCornerSymbol(position.getOppositePosition()));
            }
        }
    }
    public HashMap<Integer, Card> getCardPosition(){
        return cardPosition;
    }
}

