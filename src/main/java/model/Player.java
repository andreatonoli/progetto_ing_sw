package model;

import model.exceptions.PlayerNotFoundException;

import java.util.*;

public class Player {
    private final String username;
    private boolean firstToEnd;
    private boolean firstToPlay;
    private Card[] cardInHand;
    private Achievement[] personalObj;
    private Achievement chosenObj;
    private int points = 0;
    private PlayerState playerState;
    private ArrayList<String> chat;
    private Game game;
    private PlayerBoard playerBoard;

    /**
     * constructor of the player class:
     * @param name is the player's unique username
     */
    public Player(String name, GameBoard board, Game game)
    {
        this.username = name;
        this.playerState = PlayerState.NOT_IN_TURN;
        this.cardInHand = new Card[3];
        this.personalObj = new Achievement[2];
        this.chat = new ArrayList<String>();
        this.game = game;
        this.playerBoard = new PlayerBoard();
    }

    /**
     * getter used to know the player name
     * @return player's username
     */
    public String getUsername(){
        return username;
    }

    public Achievement getChosenObj() {
        return chosenObj;
    }

    public Card[] getCardInHand() { //cercare valore per definire "no carta"
        return cardInHand;
    }

    public Achievement[] getPersonalObj() {
        return personalObj;
    }
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

    public int addPoints(int pointsToAdd){
        this.points = this.points + pointsToAdd;
        if (this.points >= 20)
            firstToEnd = true;
        if (this.points > 29)
            this.points = 29;
        return this.points;
    }

    public void isFirstToPlay(String username){
        firstToPlay = this.username.equals(username);
    }

    public void sendMessage(Player receiver, String message){
        try{
            this.game.getChat().forwardMessage(this, receiver, false, message);
        }
        catch(PlayerNotFoundException e) {
            System.out.println("Player not found");
        }
    }

    public void sendMessage(String message){
        try{
            this.game.getChat().forwardMessage(this, null, true, message);
        }
        catch(PlayerNotFoundException e) {
            System.out.println("Player not found");
        }
    }

    public void displayMessage(Player sender, String message){
        if(chat.size() >= Chat.CHATDIM){
            chat.set(Chat.CHATDIM - 1, message);
            Collections.rotate(chat, 1);
        }
        else{
            chat.addFirst(message);
        }
        //Stampa per test
        //for(int i = 0; i < chat.size(); i++){
        //    System.out.println(chat.get(i));
        //}
    }

    //temporaneo
    public void setGame(Game game){
        this.game = game;
    }

    public Game getGame(){
        return this.game;
    }

    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }
    public void addInHand(Card card){
        for (int i = 0; i < this.cardInHand.length; i++) {
            if (this.cardInHand[i] == null){
                this.cardInHand[i] = card;
                break;
            }
        }
    }
}





