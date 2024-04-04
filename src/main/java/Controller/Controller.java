package Controller;
import model.*;

import java.util.LinkedList;

public class Controller {
    private Game game; //reference to model
    public Controller(Game game){
        this.game = game;
    }
    /**
     *Picks the top card of the deck and calls addInHand to give it to the player
     * @param player who wants to draw a card
     * @param deck from which the player choose to pick a card
     */
    public void drawCard(Player player, LinkedList<Card> deck){
        if (canDraw(player, deck)){
            Card drawedCard = deck.getFirst();
            player.addInHand(drawedCard);
            deck.removeFirst();
        }
        //else => ritorno un errore in base a cosa sbaglio (o eccezione)
    }
    /**
     * checks if the player can draw a card from a specified deck.
     * @param player to control
     * @return the possibility to draw a card
     */
    public boolean canDraw(Player player, LinkedList<Card> deck){
        //player cannot draw a card if the deck is empty, his hand is full (dim >= 3), hasn't already played a card or if it's not his turn
        if (deck.isEmpty()){
            return false;
        }
        if (player.getPlayerState().equals(PlayerState.NOT_IN_TURN) || player.getPlayerState().equals(PlayerState.PLAY_CARD)){
            return false;
        }
        for (int i = 0; i < 3; i++) {
            if (player.getCardInHand()[i] == null){
                return true;
            }
        }
        return false;
    }

    /**
     * Place card then removes it from the player's hand
     * @param card to place
     * @param coordinates of the card which corner will be covered after the placement
     * @param corner where player wants to place the card
     */
    public void placeCard(Player player, Card card, int[] coordinates, CornerEnum corner) {
        if (canPlace(corner, coordinates, player, card)) {
            //coordinates of the new card
            int[] newCoordinates = new int[2];
            newCoordinates[0] = coordinates[0] + corner.getX();
            newCoordinates[1] = coordinates[1] + corner.getY();
            if(!card.checkCost(player)){
                //TODO: Tiro eccezione
                return;
            }
            player.getPlayerBoard().setCardPosition(card, newCoordinates);
            player.getPlayerBoard().coverCorner(card, newCoordinates);
            card.calcPoint(player);
            player.removeFromHand(card);
        }
    }
    /**
     * check if the card can be placed in the designed spot
     * @param cornerPosition position of one of the corner the card will be placed on
     * @param coordinates position of the card containing the corner on cornerPosition
     * @param player is the player who are placing the card
     * @return contains a boolean
     */
    public boolean canPlace(CornerEnum cornerPosition, int[] coordinates, Player player, Card cardToBePlaced){
        int[] newCoordinates = new int[2];
        int[] coord = new int[2];
        System.arraycopy(coordinates, 0, coord, 0, 2);
        boolean legit = true;
        //check if the player placing the card is the player in turn
        if (player.getPlayerState().equals(PlayerState.NOT_IN_TURN) || player.getPlayerState().equals(PlayerState.DRAW_CARD)){
            return false;
        }
        //check if the corner we are placing the card on is available
        if (player.getPlayerBoard().getCard(coord).getCornerState(cornerPosition).equals(CornerState.NOT_VISIBLE)){
            return false;
        }
        //check if the card cover other corners and if those corner are available
        //Calculates the PlacedCard position
        coord[0] = coord[0]+cornerPosition.getX();
        coord[1] = coord[1]+cornerPosition.getY();
        //for each corner of the placed card checks if the corner below is visible
        for (CornerEnum c: CornerEnum.values()){
            if(!cardToBePlaced.getCorner(c).getSymbol().equals(Symbols.NOCORNER)){
                newCoordinates[0] = coord[0]+c.getX();
                newCoordinates[1] = coord[1]+c.getY();
                if (player.getPlayerBoard().getCard(newCoordinates) != null){
                    if (player.getPlayerBoard().getCard(newCoordinates).getCornerState(c.getOppositePosition()).equals(CornerState.NOT_VISIBLE)){
                        legit = false;
                    }
                }
            }
        }
        return legit;
    }

    /**
     * Changes the side shown to the player
     * @param card to be flipped
     */
    public void flipCard(Card card){
        card.setCurrentSide();
    }

    /**
     * Gives the player the secret achievement he chose
     * @param player who chose the card
     * @param choice index of the chosen card
     */
    public void chooseObj(Player player, int choice){
        if (choice <= 1){
            player.setChosenObj(player.getPersonalObj()[choice]);
        }
    }
}
