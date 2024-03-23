package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerBoard {
    private Player player;
    private final Card starterCard; //Se si vuole far tornare di tipo starter bisogna creare un metodo drawCard apposito per lei
    private HashMap<int[], Card> cardPosition;
    private HashMap<Symbols,Integer> symbolCount;

    public PlayerBoard(){

        this.cardPosition = new HashMap<>();

        this.symbolCount= new HashMap<>();

        for (int j=0; j<2; j++)
            //draw card da modificare
            player.getCardInHand()[j] = player.getGame().getGameBoard().drawCard(player.getGame().getGameBoard().getResourceDeck());
        player.getCardInHand()[2] = player.getGame().getGameBoard().drawCard(player.getGame().getGameBoard().getGoldDeck());
        starterCard  = player.getGame().getGameBoard().drawCard(player.getGame().getGameBoard().getStarterDeck());
        for (int j=0; j<2; j++)
            player.getPersonalObj()[j] = player.getGame().getGameBoard().drawCard();

        setCardPosition(starterCard, new int[]{0,0});
    }
    // passa l'array da un'altra parte, lì viene fatta la decisione e poi richiama setChosenObj
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

    public HashMap<int[], Card> getCardPosition(){
        return cardPosition;
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

}
