package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerBoard {
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
        cardPosition.put(coordinates[0] + ((1<<10) * coordinates[1]), placedCard);
    }
    /**
     * method to get the coordinates of the card
     * @param card is the card we need to know the position of
     * @return the position of the card
     */
    //public int[] getCardPosition(Card card){
    //    for (Map.Entry<int[], Card> entry : cardPosition.entrySet()){
    //        if (entry.getValue()==card){
    //            return entry.getKey();
    //        }
    //    }
    //    return new int[]{0,0};
    //}

    public Card getCard(int[] coord){
        int key = coord[0] + (1<<10) * coord[1];
        if (!this.cardPosition.containsKey(key)){
            return null;
        }
        return this.cardPosition.get(key);
    }
    public Set<Integer> getPositionCardKeys(){
        return this.cardPosition.keySet();
    }
    public int[] getCoordFromKey(Integer key){
        return new int[]{key % 1024, key / 1024};
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
    public void setStarterCard(Card starterCard){
        this.starterCard = starterCard;
    }

    public Card getStarterCard(){
        return this.starterCard;
    }

    //TOGLI
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
    public void coverCorner(Card card, int[] coordinates){
        int[] checkCoordinates = new int[2];
        for (CornerEnum position : CornerEnum.values()) {
            if (!card.getCorner(position).getSymbol().equals(Symbols.NOCORNER)){
                //Add symbols (of the placed card) to counter
                this.increaseSymbolCount(card.getCornerSymbol(position));
                //select the card below the placed card
                checkCoordinates[0] = coordinates[0] + position.getX();
                checkCoordinates[1] = coordinates[1] + position.getY();
                //Cover its corner
                if (this.getCard(checkCoordinates) != null) {
                    this.getCard(checkCoordinates).getCorner(position.getOppositePosition()).setState(CornerState.NOT_VISIBLE);
                    //Manca aggiungere simboli della starter card nel symbolcount
                    //this.decreaseSymbolCount(this.getCard(checkCoordinates).getCardSymbol(position));
                }
                //Remove corner's symbol from the counter
            }
        }
    }
}
