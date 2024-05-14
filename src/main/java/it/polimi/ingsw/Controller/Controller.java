package it.polimi.ingsw.Controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.observer.Observable;

import java.util.*;

//TODO: replace System.out.println with messages
//TODO: scelta achievement privato
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
        notifyAll(new GenericMessage("Players: " + this.connectedPlayers.keySet().size() + "/" + this.game.getLobbySize()));
        if (game.isFull()){
            ArrayList<String> players = new ArrayList<>();
            for (Connection c : connectedPlayers.keySet()){
                players.add(c.getUsername());
            }
            notifyAll(new OpponentsMessage(players));
            this.startGame();
            return true;
        }
        return false;
    }

    /**
     * Notifies the clients of all the changes done from the model's method startGame. It sends the users their cards,
     * the scoreboard, their starter card, asks the color they want and selects the first player to play.
     */
    private void startGame(){
        game.startGame();
        //Sends the common resource and gold cards
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_RESOURCE_UPDATE, game.getGameBoard().getCommonResource()[0]));
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_RESOURCE_UPDATE, game.getGameBoard().getCommonResource()[1]));
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_GOLD_UPDATE, game.getGameBoard().getCommonResource()[0]));
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_GOLD_UPDATE, game.getGameBoard().getCommonResource()[1]));
        //Sends the common achievements to the players
        notifyAll(new AchievementMessage(MessageType.COMMON_ACHIEVEMENT, game.getGameBoard().getCommonAchievement()));
        //Sends the starter card to each player
        for (Connection u : this.connectedPlayers.keySet()){
            u.sendMessage(new StarterCardMessage(getPlayerByClient(u).getPlayerBoard().getStarterCard()));
        }
    }
    //TODO: boh, secondo me fa caha
    //TODO: farlo chiamare solo per i client che hanno piazzato la starterCard, quindi vado a fare solo notify e non notify all
    private void commonCardSetup(Connection u){
        //TODO: gestire scelta del colore (farla in modo sequenziale in base all'ordine?
        Player p = getPlayerByClient(u);
        //Sends the players their hand
        u.sendMessage(new CardInHandMessage(p.getCardInHand()));
        //Sends the players the private achievements to choose from
        u.sendMessage(new AchievementMessage(MessageType.PRIVATE_ACHIEVEMENT, p.getPersonalObj()));
        //Send players their state
        notifyAll(new PlayerStateMessage(p.getPlayerState(), p.getUsername()));
    }
    /**
     *Picks the top card of the deck and calls addInHand to give it to the player
     * @param user who wants to draw a card
     * @param chosenDeck deck from which user wants to draw a card
     */
    public void drawCard(Connection user, String chosenDeck){
        LinkedList<Card> deck;
        if (chosenDeck.equalsIgnoreCase("resource")){
            deck = game.getGameBoard().getResourceDeck();
        }
        else{
            deck = game.getGameBoard().getGoldDeck();
        }
        try {
            Card drawedCard = this.getPlayerByClient(user).drawCard(deck);
            user.sendMessage(new UpdateCardMessage(drawedCard));
            notifyAll(new UpdateDeckMessage(deck.getFirst().getBack()));
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState(),user.getUsername()));
        } catch (EmptyException | NotInTurnException | FullHandException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Permits the player to take one card from the board, then replaces it with the same type card drawed from the decks
     * @param user who wants to take the card
     * @param index of the card taken by the player (0,1 are from commonResources and 2,3 are from commonGold)
     */
    public void drawCardFromBoard(Connection user, int index){
        try {
            Card card;
            if(index <= 1){
                card = game.getGameBoard().getCommonResource()[index];
            }
            else{
                card = game.getGameBoard().getCommonGold()[index-2];
            }
            Card drawedCard = this.getPlayerByClient(user).drawCardFromBoard(card);
            user.sendMessage(new UpdateCardMessage(drawedCard));
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState(),user.getUsername()));
        } catch (CardNotFoundException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        } catch (NotInTurnException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        } catch (FullHandException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }
    /**
     * Place card then removes it from the player's hand
     * @param user who asked to place the card
     * @param card to place
     * @param coordinates where the player wants to place
     */
    public void placeCard(Connection user, Card card, int[] coordinates) {
        try {
            this.getPlayerByClient(user).placeCard(card, coordinates);
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            notifyAll(new PlayerBoardUpdateMessage(this.getPlayerByClient(user).getPlayerBoard(), user.getUsername()));
            notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState(), user.getUsername()));
        } catch (NotInTurnException | OccupiedCornerException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }


    /**
     * Changes the side shown to the player
     * @param card to be flipped
     */
    public void flipCard(Connection user, Card card){
        card.setCurrentSide();
        user.sendMessage(new StarterCardMessage(card));
    }
    public void placeStarterCard(Connection user, Card starterCard){
        Player player = getPlayerByClient(user);
        player.getPlayerBoard().setStarterCard(starterCard);
        commonCardSetup(user);
    }

    /**
     * Permits the player to set its personal achievement
     * @param user to which belongs the chosen achievement
     * @param achievement to be set
     */
    public void chooseObj(Connection user, Achievement achievement){
        Player player = getPlayerByClient(user);
        player.setChosenObj(achievement);
        notifyAll(new PlayerBoardUpdateMessage(player.getPlayerBoard(), user.getUsername()));
        //Notifies the player his state (i.e. it's his turn or not)
        user.sendMessage(new PlayerStateMessage(player.getPlayerState(), user.getUsername()));
    }
    private Player getPlayerByClient(Connection user){
        return this.connectedPlayers.get(user);
    }

    //TODO: eliminare
    public Game getGame(){
        return this.game;
    }
}
