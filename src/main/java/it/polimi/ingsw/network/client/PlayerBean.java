package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;

import java.util.ArrayList;

public class PlayerBean {
    private String username;
    private Card starterCard;
    private Card[] hand;
    private Achievement achievement;
    private int points;
    private PlayerState state;
    private ArrayList<String> chat;
    private PlayerBoard board;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PlayerBean(String username){
        this.hand = new Card[3];
        this.chat = new ArrayList<>();
        this.username = username;
        this.points = 0;
        this.state = PlayerState.NOT_IN_TURN;
    }
    public Card[] getHand() {
        return hand;
    }
    //TODO: forse meglio farlo con carte e equals
    public Card getCard(int i){
        return hand[i];
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public int getPoints() {
        return points;
    }

    public PlayerState getState() {
        return state;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public PlayerBoard getBoard() {
        return board;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public void setCardinHand(Card card){
        for (int i = 0; i < hand.length; i++){
            if (hand[i] == null){
                hand[i] = card;
                break;
            }
        }
    }
    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }
    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }

    public void setBoard(PlayerBoard board) {
        this.board = board;
    }

    public void removeCardFromHand(Card card){
        for (int i = 0; i < this.hand.length && this.hand[i] != null; i++) {
            if (this.hand[i].equals(card)){
                this.hand[i] = null;
                break;
            }
        }
    }
    public void setPoints(int points){
        this.points = points;
    }
    public Card getStarterCard(){
        return starterCard;
    }
    public void setStarterCard(Card card){
        this.starterCard = card;
    }
}