package Controller;
import model.*;
import model.exceptions.*;

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
        try {
            System.out.println("Drawing a card...");
            canDraw(player, deck);
            Card drawedCard = deck.getFirst();
            player.addInHand(drawedCard);
            deck.removeFirst();
            System.out.println("Successfully drew a card");
        } catch (EmptyException e) {
            System.out.println("Cannot draw - The deck is empty");
        } catch (NotInTurnException e) {
            System.out.println("Cannot draw - Player is not in draw card state");
        } catch (FullHandException e) {
            System.out.println("Cannot draw - Player's hand is full");
        }
    }

    /**
     * Check if the player can draw from a specified deck
     * @param player who has to be checked
     * @param deck the player wants to draw from
     * @throws EmptyException the deck is empty
     * @throws NotInTurnException the player is not in DRAW_CARD state
     * @throws FullHandException player's hand is full so there's no space for another card
     */
    public void canDraw(Player player, LinkedList<Card> deck) throws EmptyException, NotInTurnException, FullHandException{
        if (deck.isEmpty()){
            throw new EmptyException();
        }
        if (player.getPlayerState().equals(PlayerState.NOT_IN_TURN) || player.getPlayerState().equals(PlayerState.PLAY_CARD)){
            throw new NotInTurnException();
        }
        for (int i = 0; i < 3; i++) {
            if (player.getCardInHand()[i] == null){
                return;
            }
        }
        throw new FullHandException();
    }

    /**
     * Place card then removes it from the player's hand
     * @param player who asked to place the card
     * @param card to place
     * @param coordinates of the card which corner will be covered after the placement
     * @param corner where player wants to place the card
     */
    public void placeCard(Player player, Card card, int[] coordinates, CornerEnum corner) {
        try {
            System.out.println("Placing a card...");
            canPlace(corner, coordinates, player, card);
            //coordinates of the new card
            int[] newCoordinates = new int[2];
            newCoordinates[0] = coordinates[0] + corner.getX();
            newCoordinates[1] = coordinates[1] + corner.getY();
            player.getPlayerBoard().setCardPosition(card, newCoordinates);
            player.getPlayerBoard().coverCorner(card, newCoordinates);
            card.calcPoints(player);
            player.removeFromHand(card);
            System.out.println("Card successfully placed");
        } catch (NotInTurnException e) {
            if (player.getPlayerState().equals(PlayerState.NOT_IN_TURN)){
                System.out.println("Cannot place - Its not your turn");
            }
            else{
                System.out.println("Cannot place - Draw a card");
            }
        } catch (OccupiedCornerException e) {
            System.out.println("Cannot place - One of the corner covered by your card is already occupied");
        } catch (CostNotSatisfiedException e) {
            System.out.println("Cannot place - Not enough resources to place the gold card");
        } catch (AlreadyUsedPositionException e) {
            System.out.println("Cannot place - There is already a card in " + coordinates[0] + " " + coordinates[1]);
        }
    }

    /**
     * check if the card can be placed in the designed spot
     * @param cornerPosition position of one of the corner the card will be placed on
     * @param coordinates position of the card containing the corner on cornerPosition
     * @param player is the player who are placing the card
     * @param cardToBePlaced card the player wants to place
     * @throws NotInTurnException player is not in PLACE_CARD state
     * @throws OccupiedCornerException one of the corners CardToBePlaced will cover is already covered or is hidden
     * @throws CostNotSatisfiedException player doesn't own enough resources to place a gold card
     * @throws AlreadyUsedPositionException player tries to place a card in a position already occupied by another card
     */
    public void canPlace(CornerEnum cornerPosition, int[] coordinates, Player player, Card cardToBePlaced) throws NotInTurnException, OccupiedCornerException, CostNotSatisfiedException, AlreadyUsedPositionException{
        int[] newCoordinates = new int[2];
        int[] coord = new int[2];
        PlayerBoard pBoard = player.getPlayerBoard();
        System.arraycopy(coordinates, 0, coord, 0, 2);
        //check if the player placing the card is the player in turn
        if (player.getPlayerState().equals(PlayerState.NOT_IN_TURN) || player.getPlayerState().equals(PlayerState.DRAW_CARD)){
            throw new NotInTurnException();
        }
        //check if the corner we are placing the card on is available
        if (pBoard.getCard(coord).getCornerState(cornerPosition).equals(CornerState.NOT_VISIBLE)){
            throw new OccupiedCornerException();
        }
        if(!cardToBePlaced.checkCost(player)){
            throw new CostNotSatisfiedException();
        }
        //check if the card cover other corners and if those corner are available
        //Calculates the PlacedCard position
        coord[0] = coord[0]+cornerPosition.getX();
        coord[1] = coord[1]+cornerPosition.getY();
        //Check if that position is available
        if (pBoard.getCard(coord) != null){
            throw new AlreadyUsedPositionException();
        }
        //for each corner of the placed card checks if the corner below is visible
        for (CornerEnum c: CornerEnum.values()){
            if(!cardToBePlaced.getCornerSymbol(c).equals(Symbols.NOCORNER)){
                newCoordinates[0] = coord[0]+c.getX();
                newCoordinates[1] = coord[1]+c.getY();
                if (pBoard.getCard(newCoordinates) != null){
                    if (pBoard.getCard(newCoordinates).getCornerState(c.getOppositePosition()).equals(CornerState.NOT_VISIBLE)){
                        throw new OccupiedCornerException();
                    }
                }
            }
        }
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
