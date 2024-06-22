package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a bean used to store the player state and the player resources, hand and achievements.
 * It collects the information about the player sent by the server and, it is used by the client to update the UI.
 */
public class PlayerBean implements Serializable {

    /**
     * The username of the player.
     */
    private String username;

    /**
     * The starter card of the player.
     */
    private Card starterCard;

    /**
     * The hand of the player.
     */
    private Card[] hand;

    /**
     * The achievement of the player.
     */
    private Achievement achievement;

    /**
     * The points of the player.
     */
    private int points;

    /**
     * The state of the player.
     */
    private PlayerState state;

    /**
     * The chat of the player.
     */
    private ArrayList<String> chat;

    /**
     * The player board of the player.
     */
    private PlayerBoard board;

    /**
     * The color of the pion of the player.
     */
    private Color pionColor;

    /**
     * The constructor of the class. It initializes the hand, the chat, the points and the state of the player.
     * @param username the username of the player.
     */
    public PlayerBean(String username){
        this.hand = new Card[3];
        this.chat = new ArrayList<>();
        this.username = username;
        this.points = 0;
        this.state = PlayerState.DRAW_CARD;
    }

    /**
     * This method returns the username of the player.
     * @return the username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method sets the username of the player.
     * @param username the username of the player.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method returns the hand of the player.
     * @return the hand of the player.
     */
    public Card[] getHand() {
        return hand;
    }

    /**
     * This method returns the card at the specified index of the hand.
     * @param i the index of the card.
     * @return the card at the specified index of the hand.
     */
    public Card getCard(int i){
        return hand[i];
    }

    /**
     * This method returns the achievement of the player.
     * @return the achievement of the player.
     */
    public Achievement getAchievement() {
        return achievement;
    }

    /**
     * This method returns the points of the player.
     * @return the points of the player.
     */
    public int getPoints() {
        return points;
    }

    /**
     * This method returns the state of the player.
     * @return the state of the player.
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * This method returns the chat of the player.
     * @return the chat of the player.
     */
    public ArrayList<String> getChat() {
        return chat;
    }

    /**
     * This method returns the player board of the player.
     * @return the player board of the player.
     */
    public PlayerBoard getBoard() {
        return board;
    }

    /**
     * This method sets the hand of the player.
     * @param hand the hand of the player.
     */
    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    /**
     * This method sets the card in the hand of the player.
     * @param card the card to set.
     */
    public void setCardinHand(Card card){
        for (int i = 0; i < hand.length; i++){
            if (hand[i] == null){
                hand[i] = card;
                break;
            }
        }
    }

    /**
     * This method sets the achievement of the player.
     * @param achievement the achievement of the player.
     */
    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    /**
     * This method sets the state of the player.
     * @param state the state of the player.
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * This method sets the chat of the player.
     * @param chat the chat of the player.
     */
    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }

    /**
     * This method sets the player board of the player.
     * @param board the player board of the player.
     */
    public void setBoard(PlayerBoard board) {
        this.board = board;
    }

    /**
     * This method remove a card from the player's hand.
     * @param card the card to remove.
     */
    public void removeCardFromHand(Card card){
        for (int i = 0; i < this.hand.length && this.hand[i] != null; i++) {
            if (this.hand[i].equals(card)){
                this.hand[i] = null;
                break;
            }
        }
    }

    /**
     * This method sets the points of the player.
     * @param points the points of the player.
     */
    public void setPoints(int points){
        this.points = points;
    }

    /**
     * This method returns the starter card of the player.
     * @return the starter card of the player.
     */
    public Card getStarterCard(){
        return starterCard;
    }

    /**
     * This method sets the starter card of the player.
     * @param card the starter card of the player.
     */
    public void setStarterCard(Card card){
        this.starterCard = card;
    }

    /**
     * This method returns the color of the pion of the player.
     * @return the color of the pion of the player.
     */
    public Color getPionColor() {
        return pionColor;
    }

    /**
     * This method sets the color of the pion of the player.
     * @param pionColor the color of the pion of the player.
     */
    public void setPionColor(Color pionColor) {
        this.pionColor = pionColor;
    }
}