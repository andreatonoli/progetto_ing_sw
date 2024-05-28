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
     * unique player name
     */
    private final String username;
    /**
     *
     */
    private boolean firstToEnd;
    private boolean firstToPlay;
    private Color pionColor;
    private final Card[] cardInHand;
    private final Achievement[] personalObj;
    private Achievement chosenObj;
    private int points = 0;
    private PlayerState playerState;
    private final ArrayList<String> chat;
    private Game game;
    private final PlayerBoard playerBoard;
    private boolean disconnected;
    private int objCompleted = 0;

    /**
     * constructor of the player class:
     * @param name is the player's unique username
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

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
        if (this.disconnected){
            game.addDisconnections(1);
        }
        else {
            game.addDisconnections(-1);
        }
    }

    public Color getPionColor() {
        return pionColor;
    }

    public void setPionColor(Color pionColor) {
        this.pionColor = pionColor;
    }

    /**
     * getter used to know the player name
     * @return player's username
     */
    public String getUsername(){
        return username;
    }

    /**
     * getter to get the chosenObj of the player
     * @return player's chosen objective
     */
    public Achievement getChosenObj() {
        return chosenObj;
    }

    /**
     * getter to get the cardInHand value
     * @return the player's cards
     */
    public Card[] getCardInHand() { //cercare valore per definire "no carta"
        return cardInHand;
    }

    /**
     * getter to get the personalObj value
     * @return the objective the player can choose from
     */
    public Achievement[] getPersonalObj() {
        return personalObj;
    }

    /**
     * gets the chat log
     * @return the chat log
     */
    public ArrayList<String> getChat() {
        return chat;
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
     * setter to set the chosenObj value
     * @param choice is the objective the player have chosen from the personalObj
     */
    public void setChosenObj(Achievement choice){
        this.chosenObj = choice;
    }
    /**
     * getter to get the current score of the player
     * @return the player's points
     */
    public int getPoints(){
        return points;
    }

    /**
     * adder to add points to the score
     *
     * @param pointsToAdd are the points that will be added
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

    public boolean isFirstToEnd(){
        return firstToEnd;
    }

    /**
     * set the boolean value used to know who is the starting player
     */
    public void setFirstToPlay(){
        firstToPlay = true;
    }

    public boolean isFirstToPlay(){
        return firstToPlay;
    }

    /**
     * used when a player need to send a message to another player
     * @param receiver is the player that is receiving the message
     * @param message is the core of the message
     */
    public void sendChatMessage(Player receiver, String message){
        try{
            this.game.getChat().forwardMessage(this, receiver, false, message);
        }
        catch(PlayerNotFoundException e) {
            System.out.println("Player not found");
        }
    }

    /**
     * used when a player need to send a message to all the other players
     * @param message is the core of the message
     */
    public void sendChatMessage(String message){
        try{
            this.game.getChat().forwardMessage(this, null, true, message);
        }
        catch(PlayerNotFoundException e) {
            System.out.println("Player not found");
        }
    }
    //TODO: commentare
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
     * getter to get the playerBoard parameter
     * @return the player board of this player
     */
    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    /**
     * adder to add cards at the player hand
     * @param card is the card that will be added to the hand
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
     * counterpart of the method addInHand
     * @param cardToRemove is the card that will be removed by the player's hand
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
     * adder to update the value of objCompleted
     * it'll be called every time the player completes one achievement
     */
    public void addObjCompleted(){
        objCompleted++;
    }

    /**
     * getter to get the objCompleted value
     * @return the number of objective the player have completed
     */
    public int getObjCompleted(){
        return objCompleted;
    }

    /**
     *Picks the top card of the deck and calls addInHand to give it to the player
     * @param deck from which the player choose to pick a card
     */
    public Card drawCard(LinkedList<Card> deck) throws EmptyException, NotInTurnException, FullHandException {
        canDraw(deck);
        Card drawedCard = deck.getFirst();
        this.addInHand(drawedCard);
        deck.removeFirst();
        return drawedCard;
    }
    
    /**
     * Check if the player can draw from a specified deck
     * @param deck the player wants to draw from
     * @throws EmptyException the deck is empty
     * @throws NotInTurnException the player is not in DRAW_CARD state
     * @throws FullHandException player's hand is full so there's no space for another card
     */
    public void canDraw(LinkedList<Card> deck) throws EmptyException, NotInTurnException, FullHandException{
        if (deck.isEmpty()){
            throw new EmptyException();
        }
        if (this.getPlayerState().equals(PlayerState.NOT_IN_TURN) || this.getPlayerState().equals(PlayerState.PLAY_CARD)){
            throw new NotInTurnException();
        }
        for (int i = 0; i < 3; i++) {
            if (this.getCardInHand()[i] == null){
                return;
            }
        }
        throw new FullHandException();
    }

    /**
     * Permits the player to take one card from the board, then replaces it with the same type card drawed from the decks
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





