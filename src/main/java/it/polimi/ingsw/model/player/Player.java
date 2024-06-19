package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Chat;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.exceptions.*;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {

    /**
     * Unique player name.
     */
    private final String username;

    /**
     * Boolean value that is true if the player is the first to reach 20 points.
     */
    private boolean firstToEnd;

    /**
     * Boolean value that is true if the player is the first to play in the game.
     */
    private boolean firstToPlay;

    /**
     * Represents the color chosen by the player.
     */
    private Color pionColor;

    /**
     * Contains all the cards that are in the player's hand.
     */
    private final Card[] cardInHand;

    /**
     * Contains the two personal achievements the player can choose from.
     */
    private final Achievement[] personalObj;

    /**
     * The personal objective chosen by the player.
     */
    private Achievement chosenObj;

    /**
     * Player's points.
     */
    private int points = 0;

    /**
     * State of the player (e.g. DRAW_CARD).
     */
    private PlayerState playerState;

    /**
     * List of messages sent by and to the player.
     */
    private final ArrayList<String> chat;

    /**
     * Link to the game which the player belongs to.
     */
    private final Game game;

    /**
     * Link to the player's board where he can place his cards.
     */
    private final PlayerBoard playerBoard;

    /**
     * Indicates whether a player is connected or not.
     */
    private boolean disconnected;

    /**
     * Number of achievements completed.
     */
    private int objCompleted = 0;

    /**
     * Constructor of the player class.
     *
     * @param name The player's username, unique in the lobby.
     * @param game The game the player is participating to.
     */
    public Player(String name, Game game)
    {
        this.username = name;
        this.playerState = PlayerState.NOT_IN_TURN;
        this.cardInHand = new Card[3];
        this.personalObj = new Achievement[2];
        this.chat = new ArrayList<>();
        this.game = game;
        this.playerBoard = new PlayerBoard();
        this.disconnected = false;
        this.firstToEnd = false;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * Sets the status of the player (disconnected or connected). Updates the number of disconnected players in the game.
     * @param disconnected Status of the connection of the client linked to this player.
     */
    public void setDisconnected(boolean disconnected) {
        //Updates the connection status of the client that controls this player.
        this.disconnected = disconnected;
        //Updates the number of disconnected players in the game.
        if (this.disconnected){
            game.addDisconnections(1);
        }
        else {
            game.addDisconnections(-1);
        }
    }

    /**
     * Gets the color chosen by the player.
     * @return The color of the player's token.
     */
    public Color getPionColor() {
        return pionColor;
    }

    /**
     * Sets the color of the player's token.
     * @param pionColor Color chosen by the player.
     */
    public void setPionColor(Color pionColor) {
        this.pionColor = pionColor;
    }

    /**
     * Gets the player's name.
     * @return Player's username.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Gets the objective chosen by the player.
     * @return Objective chosen by the player.
     */
    public Achievement getChosenObj() {
        return chosenObj;
    }

    /**
     * Gets all the card in the player's hand.
     * @return An array (size = 3) containing all the cards in the player's hand.
     */
    public Card[] getCardInHand() { //cercare valore per definire "no carta"
        return cardInHand;
    }

    /**
     * Gets the two objective the player can choose from.
     * @return The array containing the two objectives the player can choose from.
     */
    public Achievement[] getPersonalObj() {
        return personalObj;
    }

    /**
     * Gets the chat log.
     * @return A list of 25 messages sent to and by the player.
     */
    public ArrayList<String> getChat() {
        return chat;
    }

    /**
     * Changes the player's state.
     * @param playerState the new state of the player.
     */
    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    /**
     * Gets the current state of the player.
     * @return the current player state.
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * Sets the objective chosen by the player.
     * @param choice The objective the player has chosen from the personalObj array.
     */
    public void setChosenObj(Achievement choice){
        this.chosenObj = choice;
    }

    /**
     * Gets the current score of the player.
     * @return The player's score.
     */
    public int getPoints(){
        return points;
    }

    /**
     * Adds some points to the player.
     *
     * @param pointsToAdd Points that the player will receive.
     */
    public void addPoints(int pointsToAdd){
        this.points = this.points + pointsToAdd;
        if (this.points >= 20){
            firstToEnd = true;
        }
        if (this.points > 29){
            this.points = 29;
        }
    }

    /**
     * Indicates if the player was the first to reach 20 points.
     * @return True only if the player was the first to reach 20 points.
     */
    public boolean isFirstToEnd(){
        return firstToEnd;
    }

    /**
     * Sets that the player is the first to play. //TODO: commenta meglio
     */
    public void setFirstToPlay(){
        firstToPlay = true;
    }

    /**
     * Indicates if the player was the first to have played.
     * @return True only if the player was the first to have played.
     */
    public boolean isFirstToPlay(){
        return firstToPlay;
    }

    /**
     * Player sends a message to another player.
     * @param receiver Receiver of the message.
     * @param message Message text.
     * @throws PlayerNotFoundException When the receiver is not part of the lobby.
     */
    public void sendChatMessage(Player receiver, String message) throws PlayerNotFoundException{
        this.game.getChat().forwardMessage(this, receiver, false, message);
    }

    /**
     * Player sends a global message (received by all the players in the lobby).
     * @param message Message text.
     */
    public void sendChatMessage(String message) {
        try {
            this.game.getChat().forwardMessage(this, null, true, message);
        } catch (PlayerNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Adds the message into the chat log. If the size of the log is greater than {@link Chat.CHATDIM} the oldest message is deleted.
     * @param sender The sender of the message.
     * @param message Message Text.
     */
    public void displayMessage(Player sender, String message){
        if(chat.size() >= Chat.CHATDIM){
            chat.set(Chat.CHATDIM - 1, sender.getUsername() + ": " + message);
            Collections.rotate(chat, 1);
        }
        else{
            chat.addFirst(sender.getUsername() + ": " + message);
        }
    }

    /**
     * Gets the player's board.
     * @return The personal board of this player.
     */
    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    /**
     * Adds a card to the player's hand
     * @param card Card to add to the hand.
     */
    public void addInHand(Card card){
        for (int i = 0; i < this.cardInHand.length; i++) {
            if (this.cardInHand[i] == null){
                this.cardInHand[i] = card;
                break;
            }
        }
    }

    /**
     * Remove a card from the player's hand if present.
     * @param cardToRemove Card to remove from the player's hand.
     */
    public void removeFromHand(Card cardToRemove){
        for (int i = 0; i < this.cardInHand.length; i++) {
            if (this.cardInHand[i] != null && this.cardInHand[i].equals(cardToRemove)){
                this.cardInHand[i] = null;
                break;
            }
        }
    }

    /**
     * Adds one to the counter that keeps track of how many objectives the player has completed.
     */
    public void addObjCompleted(){
        objCompleted++;
    }

    /**
     * Gets the counter of how many objectives the player has completed.
     * @return The number of objective the player has completed.
     */
    public int getObjCompleted(){
        return objCompleted;
    }

    /**
     * Picks the top card of the deck and adds it to the player's hand.
     * @param deck deck from which the player draws.
     * @throws EmptyException if the player tries to draw from an empty deck.
     * @throws NotInTurnException if the player is not in turn.
     * @throws FullHandException if the player has 3 cards in hand.
     */
    public Card drawCard(LinkedList<Card> deck) throws EmptyException, NotInTurnException, FullHandException {
        canDraw(deck);
        Card drawedCard = deck.getFirst();
        this.addInHand(drawedCard);
        deck.removeFirst();
        return drawedCard;
    }
    
    /**
     * Check if the player can draw from a specified deck.
     * @param deck deck from which the player wants to draw from.
     * @throws EmptyException if the deck is empty.
     * @throws NotInTurnException if the player is not in {@code DRAW_CARD} state.
     * @throws FullHandException if player's hand is full so there's no space for another card.
     */
    public void canDraw(LinkedList<Card> deck) throws EmptyException, NotInTurnException, FullHandException{
        if (deck.isEmpty()){
            throw new EmptyException();
        }
        if (this.playerState.equals(PlayerState.NOT_IN_TURN) || this.playerState.equals(PlayerState.PLAY_CARD)){
            throw new NotInTurnException();
        }
        for (int i = 0; i < 3; i++) {
            if (this.cardInHand[i] == null){
                return;
            }
        }
        throw new FullHandException();
    }

    /**
     * Permits the player to take one card from the board, then replaces it with the same type card drew from the decks
     * @param card taken by the player
     */
    public Card drawCardFromBoard(Card card) throws CardNotFoundException, NotInTurnException, FullHandException{
        GameBoard gameBoard = this.game.getGameBoard();
        Card taken = null;
        int takenIndex;
        canDrawFromBoard(card);
        if (card.getType().equalsIgnoreCase("Resource")){
            if (card.equals(gameBoard.getCommonResource()[0])){
                taken = gameBoard.getCommonResource()[0];
                gameBoard.getCommonResource()[0] = null;
                takenIndex = 0;
            }
            else{
                taken = gameBoard.getCommonResource()[1];
                gameBoard.getCommonResource()[1] = null;
                takenIndex = 1;
            }
            this.addInHand(taken);
            gameBoard.replaceResourceCard(takenIndex);
        }
        else if (card.getType().equalsIgnoreCase("Gold")){
            if (card.equals(gameBoard.getCommonGold()[0])){
                taken = gameBoard.getCommonGold()[0];
                gameBoard.getCommonGold()[0] = null;
                takenIndex = 0;
            }
            else{
                taken = gameBoard.getCommonGold()[1];
                gameBoard.getCommonGold()[1] = null;
                takenIndex = 1;
            }
            this.addInHand(taken);
            gameBoard.replaceGoldCard(takenIndex);
        }
        return taken;
    }

    /**
     * Checks if the player can draw from the board
     * @param card the player wants to take
     * @throws CardNotFoundException the card the player wants to draw isn't on the board
     * @throws NotInTurnException the player is not in the DRAW_CARD state
     * @throws FullHandException player's hand is full
     */
    public void canDrawFromBoard(Card card) throws CardNotFoundException, NotInTurnException, FullHandException{
        GameBoard gBoard = this.game.getGameBoard();
        if (this.getPlayerState().equals(PlayerState.NOT_IN_TURN) || this.getPlayerState().equals(PlayerState.PLAY_CARD)){
            throw new NotInTurnException();
        }
        if (card.getType().equalsIgnoreCase("Resource")){
            if (!card.equals(gBoard.getCommonResource()[0]) && !card.equals(gBoard.getCommonResource()[1])){
                throw new CardNotFoundException();
            }
        }
        else if (card.getType().equalsIgnoreCase("Gold")){
            if (!card.equals(gBoard.getCommonGold()[0]) && !card.equals(gBoard.getCommonGold()[1])){
                throw new CardNotFoundException();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (this.getCardInHand()[i] == null){
                return;
            }
        }
        throw new FullHandException();
    }

    /**
     * Place card then removes it from the player's hand
     * @param card to place
     * @param coordinates of the place where the card will be placed
     * @throws NotInTurnException player is not in PLACE_CARD state
     * @throws OccupiedCornerException one of the corners CardToBePlaced will cover is already covered or is hidden
     * @throws CostNotSatisfiedException player doesn't own enough resources to place a gold card
     * @throws AlreadyUsedPositionException player tries to place a card in a position already occupied by another card
     * @throws InvalidCoordinatesException player tries to place a card on a spot not linked with any other card
     */
    public void placeCard(Card card, int[] coordinates) throws OccupiedCornerException, NotInTurnException, AlreadyUsedPositionException, CostNotSatisfiedException, InvalidCoordinatesException {
        CornerEnum corner = null;
        boolean validCoordinates = false;
        for (CornerEnum c : CornerEnum.values()){
            int[] linkingCoordinates = new int[]{coordinates[0] + c.getX(), coordinates[1] + c.getY()};
            if (playerBoard.getCard(linkingCoordinates) != null){
                corner = c;
                validCoordinates = true;
                break;
            }
        }
        if (!validCoordinates){
            throw new InvalidCoordinatesException();
        }
        canPlace(corner, coordinates, card);
        this.playerBoard.setCardPosition(card, coordinates);
        this.playerBoard.coverCorner(card, coordinates);
        card.calcPoints(this);
        removeFromHand(card);
    }

    /**
     * check if the card can be placed in the designed spot
     * @param cornerPosition position of one of the corner the card will be placed on
     * @param coordinates position of the card containing the corner on cornerPosition
     * @param cardToBePlaced card the player wants to place
     * @throws NotInTurnException player is not in PLACE_CARD state
     * @throws OccupiedCornerException one of the corners CardToBePlaced will cover is already covered or is hidden
     * @throws CostNotSatisfiedException player doesn't own enough resources to place a gold card
     * @throws AlreadyUsedPositionException player tries to place a card in a position already occupied by another card
     */
    public void canPlace(CornerEnum cornerPosition, int[] coordinates, Card cardToBePlaced) throws NotInTurnException, OccupiedCornerException, CostNotSatisfiedException, AlreadyUsedPositionException{
        int[] newCoordinates = new int[2];
        int[] coord = new int[2];
        coord[0] = coordinates[0] + cornerPosition.getX();
        coord[1] = coordinates[1] + cornerPosition.getY();
        //Check if that position is available
        if (this.playerBoard.getCard(coordinates) != null){
            throw new AlreadyUsedPositionException();
        }
        //check if the player placing the card is the player in turn
        if (this.playerState.equals(PlayerState.NOT_IN_TURN) || this.playerState.equals(PlayerState.DRAW_CARD)){
            throw new NotInTurnException();
        }
        //check if the corner we are placing the card on is available
        if (this.playerBoard.getCard(coord).getCornerState(Objects.requireNonNull(cornerPosition.getOppositePosition())).equals(CornerState.NOT_VISIBLE) || this.playerBoard.getCard(coord).getCornerState(cornerPosition.getOppositePosition()).equals(CornerState.OCCUPIED)){
            throw new OccupiedCornerException();
        }
        if(!cardToBePlaced.checkCost(this)){
            throw new CostNotSatisfiedException();
        }
        //check if the card cover other corners and if those corner are available
        //Calculates the coveredCard position
        coord[0] = coord[0]-cornerPosition.getX();
        coord[1] = coord[1]-cornerPosition.getY();
        //for each corner of the placed card checks if the corner below is visible
        for (CornerEnum c: CornerEnum.values()){
            if(!cardToBePlaced.getCornerSymbol(c).equals(Symbols.NOCORNER)){
                newCoordinates[0] = coord[0]+c.getX();
                newCoordinates[1] = coord[1]+c.getY();
                if (this.playerBoard.getCard(newCoordinates) != null){
                    if (this.playerBoard.getCard(newCoordinates).getCornerState(Objects.requireNonNull(c.getOppositePosition())).equals(CornerState.NOT_VISIBLE) || this.playerBoard.getCard(newCoordinates).getCornerState(c.getOppositePosition()).equals(CornerState.OCCUPIED)){
                        throw new OccupiedCornerException();
                    }
                }
            }
        }
    }
}





