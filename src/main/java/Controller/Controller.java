package Controller;

import model.Game;
import model.card.Card;
import model.enums.CornerEnum;
import model.enums.PlayerState;
import model.exceptions.*;
import model.player.Player;
import network.messages.*;
import network.server.Connection;
import observer.Observable;

import java.util.*;

//TODO: replace System.out.println with messages
//Se ci sono problemi in placeCard piazza una copia del parametro e non il parametro
public class Controller extends Observable {
    /**
     * Reference to the game (model) controlled by {@code this}
     */
    private final Game game;
    private final Map<Connection, Player> connectedPlayers;
    private final TurnHandler turnHandler;

    public Controller(int numPlayers){
        this.game = new Game(numPlayers);
        this.turnHandler = new TurnHandler(game);
        this.connectedPlayers = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Permits the client {@param user} to join the game
     * @param user of the client who's joining the game
     * @return {@code true} if the game is full, {@code false} otherwise
     */
    public boolean joinGame(Connection user){
        Player player = new Player(user.getUsername(), game);
        this.connectedPlayers.put(user, player);
        this.addObserver(user);
        user.setLobby(this);
        game.addPlayer(player);
        if (game.isFull()){
            game.startGame();
            notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState()));
            return true;
        }
        return false;
    }
    /**
     *Picks the top card of the deck and calls addInHand to give it to the player
     * @param user who wants to draw a card
     * @param deck from which the player choose to pick a card
     */
    public void drawCard(Connection user, LinkedList<Card> deck){
        try {
            notify(user,new GenericMessage("Drawing a card..."));
            this.getPlayerByClient(user).drawCard(deck);
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState()));
            notify(user,new GenericMessage("Successfully drew a card"));
        } catch (EmptyException | NotInTurnException | FullHandException e) {
            notify(user,new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Permits the player to take one card from the board, then replaces it with the same type card drawed from the decks
     * @param user who wants to take the card
     * @param card taken by the player
     */
    public void drawCardFromBoard(Connection user, Card card){
        try {
            notify(user,new GenericMessage("Drawing a card..."));
            this.getPlayerByClient(user).drawCardFromBoard(card);
            notify(user,new GenericMessage("Successfully drew a card"));
        } catch (CardNotFoundException e) {
            notify(user,new ErrorMessage(e.getMessage()));
        } catch (NotInTurnException e) {
            notify(user,new ErrorMessage(e.getMessage()));
        } catch (FullHandException e) {
            notify(user,new ErrorMessage(e.getMessage()));
        }
    }
    /**
     * Place card then removes it from the player's hand
     * @param user who asked to place the card
     * @param card to place
     * @param coordinates of the card which corner will be covered after the placement
     * @param corner where player wants to place the card
     */
    public void placeCard(Connection user, Card card, int[] coordinates, CornerEnum corner) {
        try {
            notify(user,new GenericMessage("placing card..."));
            this.getPlayerByClient(user).placeCard(card,coordinates,corner);
            notify(user,new GenericMessage("Successfully palced a card"));
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState()));
        } catch (NotInTurnException e) {
            if (this.getPlayerByClient(user).getPlayerState().equals(PlayerState.NOT_IN_TURN)){
                notify(user,new ErrorMessage(e.getMessage()));
            }
            else{
                notify(user,new ErrorMessage(e.getMessage()));
            }
        } catch (OccupiedCornerException e) {
            notify(user,new ErrorMessage(e.getMessage()));
        } catch (CostNotSatisfiedException e) {
            notify(user,new ErrorMessage(e.getMessage()));
        } catch (AlreadyUsedPositionException e) {
            notify(user,new ErrorMessage(e.getMessage()));        }
    }


    /**
     * Changes the side shown to the player
     * @param card to be flipped
     */
    public void flipCard(Connection user, Card card){
        card.setCurrentSide();
        notify(user, new UpdateCardMessage(card));
    }
    public void placeStarterCard(Connection user, Card starterCard){
        Player player = getPlayerByClient(user);
        player.getPlayerBoard().setStarterCard(starterCard);
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
    private Player getPlayerByClient(Connection user){
        return this.connectedPlayers.get(user);
    }

    //TODO: eliminare
    public Game getGame(){
        return this.game;
    }
}
