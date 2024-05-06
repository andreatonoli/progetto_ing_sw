package network.client;

import model.card.Achievement;
import model.card.Card;
import model.enums.PlayerState;
import model.player.PlayerBoard;

import java.util.ArrayList;

public class PlayerBean {
    private String username;
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

    public void setPoints(int points) {
        this.points = points;
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
}