package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.ActionMessage;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.observer.Observable;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller extends Observable {
    /**
     * Reference to the game (model) controlled by {@code this}
     */
    private final Game game;
    private int id;
    /**
     * Map to connect the different Connections (client handlers) to their representation on the model (Player)
     */
    private final Map<Connection, Player> connectedPlayers;
    private final TurnHandler turnHandler;
    /**
     * Queue that stores all the actions required by the players to be carried out by the controller
     */
    private final BlockingQueue<ActionMessage> actionQueue;
    private String playerInTurn;
    private boolean setupFinished = false;
    private boolean endingCycle = false;
    private boolean processingAction = false;

    //TODO: mettere controllo lato server per controllare che client non scammi

    public Controller(int numPlayers, int id){
        this.id = id;
        this.game = new Game(numPlayers, id);
        this.turnHandler = new TurnHandler(game);
        this.connectedPlayers = Collections.synchronizedMap(new HashMap<>());
        this.actionQueue = new LinkedBlockingQueue<>();
    }

    public int getId(){
        return id;
    }

    /**
     * This method adds the tasks required by the player to the queue
     * @param action requested by the player
     */
    public void addAction(ActionMessage action){
        this.actionQueue.add(action);
    }

    /**
     * reads one action from the queue and performs it
     */
    public void execAction(){
        if (!actionQueue.isEmpty() && !processingAction) {
            ActionMessage nextAction = actionQueue.poll();
            processingAction = true;
            if (setupFinished || nextAction.getApplicant().getUsername().equals(playerInTurn)){
                nextAction.getCommand().execute();
            }
            else if(!setupFinished){
                actionQueue.add(nextAction);
            }
            processingAction = false;
            if (setupFinished && !actionQueue.isEmpty()) {
                execAction();
            }
        }
    }

    private void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                execAction();
            }
        }, 0, 100);
    }


    /**
     * Permits the client to join the game
     * @param user of the client who's joining the game
     * @return {@code true} if the game is full, {@code false} otherwise
     */
    public boolean joinGame(Connection user){
        Player player = new Player(user.getUsername(), game);
        this.connectedPlayers.put(user, player);
        this.addObserver(user);
        this.turnHandler.addObserver(user);
        user.setLobby(this);
        game.addPlayer(player);
        notifyAll(new GenericMessage("Players: " + this.connectedPlayers.keySet().size() + "/" + this.game.getLobbySize()));
        if (game.isFull()){
            game.setGameState(GameState.START);
            notifyAll(new GameStateMessage(game.getGameState()));
            this.startGame();
            return true;
        }
        return false;
    }

    /**
     * Notifies the clients of all the changes done from the model's method startGame. It sends the users their cards,
     * the scoreboard, their starter card, asks the color they want and selects the first player to play.
     */
    private void startGame() {
        try {
            game.startGame();
        } catch (NotEnoughPlayersException e) {
            System.err.println(e.getMessage());
        }
        //send all players their opponents name
        ArrayList<String> players = new ArrayList<>();
        for (Connection c : connectedPlayers.keySet()) {
            players.add(c.getUsername());
        }
        notifyAll(new OpponentsMessage(players));
        //Sends the common resource and gold cards
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_RESOURCE_UPDATE, game.getGameBoard().getCommonResource()[0], 0));
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_RESOURCE_UPDATE, game.getGameBoard().getCommonResource()[1], 1));
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_GOLD_UPDATE, game.getGameBoard().getCommonGold()[0], 0));
        notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_GOLD_UPDATE, game.getGameBoard().getCommonGold()[1], 1));
        //Sends the common achievements to the players
        notifyAll(new AchievementMessage(MessageType.COMMON_ACHIEVEMENT, game.getGameBoard().getCommonAchievement()));
        //sends the color of the first card of the deck
        notifyAll(new UpdateDeckMessage(game.getGameBoard().getResourceDeck().getFirst().getColor(), true));
        notifyAll(new UpdateDeckMessage(game.getGameBoard().getGoldDeck().getFirst().getColor(), false));
        //Sends each player their private information
        for (Connection u : this.connectedPlayers.keySet()) {
            Player p = getPlayerByClient(u);
            //Sends the player their starter card
            u.sendMessage(new StarterCardMessage(getPlayerByClient(u).getPlayerBoard().getStarterCard()));
            //Sends the players their hand
            u.sendMessage(new CardInHandMessage(p.getCardInHand()));
            //Sends the players the private achievements to choose from
            u.sendMessage(new AchievementMessage(MessageType.PRIVATE_ACHIEVEMENT, p.getPersonalObj()));
            //Send players their state
            notifyAll(new PlayerStateMessage(p.getPlayerState(), p.getUsername()));
        }
        playerInTurn = game.getFirstPlayer().getUsername();
        pickQueue();
    }

    /**
     *Picks the top card of the deck and calls addInHand to give it to the player
     * @param user who wants to draw a card
     * @param chosenDeck deck from which user wants to draw a card
     */
    public void drawCard(Connection user, String chosenDeck){
        LinkedList<Card> deck;
        boolean isResource = false;
        if (chosenDeck.equalsIgnoreCase("resource")){
            deck = game.getGameBoard().getResourceDeck();
            isResource = true;
        }
        else{
            deck = game.getGameBoard().getGoldDeck();
        }
        try {
            Card drawedCard = this.getPlayerByClient(user).drawCard(deck);
            if (game.getGameBoard().decksAreEmpty() && !endingCycle){
                endingCycle = true;
                turnHandler.startEnd(this.getPlayerByClient(user));
            }
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            //notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState(), user.getUsername()));
            notifyAll(new UpdateDeckMessage(deck.getFirst().getColor(), isResource));
            user.sendMessage(new UpdateCardMessage(drawedCard));
        } catch (EmptyException | NotInTurnException | FullHandException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Permits the player to take one card from the board, then replaces it with the same type card drew from the decks
     * @param user who wants to take the card
     * @param index of the card taken by the player (0,1 are from commonResources and 2,3 are from commonGold)
     */
    public void drawCardFromBoard(Connection user, int index){
        try {
            Card card;
            boolean isGold;
            if(index <= 1){
                card = game.getGameBoard().getCommonResource()[index];
                isGold = false;
            }
            else if(index <=3){
                card = game.getGameBoard().getCommonGold()[index-2];
                isGold = true;
            }
            else{
                //TODO: errore profondo
                return;
            }
            Card drawedCard = this.getPlayerByClient(user).drawCardFromBoard(card);
            if (isGold){
                notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_GOLD_UPDATE, game.getGameBoard().getCommonGold()[index-2], index-2));
            }
            else{
                notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_RESOURCE_UPDATE, game.getGameBoard().getCommonResource()[index], index));
            }
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            //notifyAll(new PlayerStateMessage(this.getPlayerByClient(user).getPlayerState(), user.getUsername()));
            user.sendMessage(new UpdateCardMessage(drawedCard));
        } catch (CardNotFoundException | NotInTurnException | FullHandException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }
    /**
     * Place the card on the chosen coordinates
     * @param user who asked to place the card
     * @param card to place
     * @param coordinates where the player wants to place the card
     */
    public void placeCard(Connection user, Card card, int[] coordinates) {
        try {
            Player p = this.getPlayerByClient(user);
            boolean present = false;
            //TODO: cercare algoritmo migliore
            for (Card c : p.getCardInHand()){
                if (c.equals(card)){
                    present = true;
                }
            }
            if (!present){
                //TODO: errore profondo
                return;
            }
            //Place card and change player's state
            p.placeCard(card, coordinates);
            if (p.isFirstToEnd() && !endingCycle){
                endingCycle = true;
                turnHandler.startEnd(this.getPlayerByClient(user));
            }
            turnHandler.changePlayerState(p);
            //notifies all players the changed made by user
            //notifyAll(new PlayerStateMessage(p.getPlayerState(), p.getUsername()));
            notifyAll(new ScoreUpdateMessage(p.getPoints(), p.getUsername()));
            notifyAll(new PlayerBoardUpdateMessage(p.getPlayerBoard(), p.getUsername()));
        } catch (NotInTurnException | OccupiedCornerException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException e) {
            //If any exception is caught first we send the error message, and then we restore the card the player tried to place
            user.sendMessage(new UpdateCardMessage(card));
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    public void placeStarterCard(Connection user, Card starterCard){
        Player player = getPlayerByClient(user);
        if (!starterCard.equals(player.getPlayerBoard().getStarterCard())){
            //TODO: errore profondo
        }
        player.getPlayerBoard().setStarterCard(starterCard);
    }

    /**
     * Permits the player to set its personal achievement
     * @param user to which belongs the chosen achievement
     * @param achievement to be set
     */
    public void chooseObj(Connection user, Achievement achievement){
        Player player = getPlayerByClient(user);
        if (!achievement.equals(player.getPersonalObj()[0]) && !achievement.equals(player.getPersonalObj()[1])){
            //TODO: errore profondo
        }
        player.setChosenObj(achievement);
        chooseColor(user);
        notifyAll(new PlayerBoardUpdateMessage(player.getPlayerBoard(), user.getUsername()));
    }

    public void chooseColor(Connection user) {
            user.sendMessage(new ColorRequestMessage(game.getAvailableColors()));
    }

    public void setColor(Connection user, Color color){
        this.game.getAvailableColors().remove(color);
        getPlayerByClient(user).setPionColor(color);
        notifyAll(new ColorResponseMessage(user.getUsername(), color));
        playerInTurn = turnHandler.changeSetupPlayer();
        if (playerInTurn == null){
            setupFinished = true;
            game.setGameState(GameState.IN_GAME);
            notifyAll(new GameStateMessage(game.getGameState()));
        }
    }

    public void sendChatMessage(Connection sender, Connection receiver, String message){
        getPlayerByClient(sender).sendChatMessage(getPlayerByClient(receiver), message);
        sender.sendMessage(new ChatMessage(getPlayerByClient(sender).getChat()));
        receiver.sendMessage(new ChatMessage(getPlayerByClient(receiver).getChat()));
    }
    public void sendChatMessage(Connection sender, String message){
        getPlayerByClient(sender).sendChatMessage(message);
        notifyAll(new ChatMessage(getPlayerByClient(sender).getChat()));
    }

    public Player getPlayerByClient(Connection user){
        return this.connectedPlayers.get(user);
    }

    public void disconnectedWhileInTurn(String username){
        Player player = game.getPlayerByUsername(username);
        turnHandler.disconnectedWhileInTurn(player);
    }

    /**
     * Sends back the model state to the player who has reconnected
     * Precondition: user.username is equal to one, and only one, username //TODO: Verificare che la precondizione sia sempre verificata
     * @param user who reconnected
     */
    public void reconnectBackup(Connection user /*, Connection oldConnection*/){
        //Player player = connectedPlayers.get(oldConnection);
        //connectedPlayers.remove(oldConnection);
        //connectedPlayers.put(user,player);
        //user.sendMessage(new ReconnectionMessage(game.getGameBoard(),player));
        //player.setDisconnected(false);
        String username = user.getUsername();
        Player reconnectedPlayer = null;
        List<Player> opponents = new ArrayList<>();
        for (Connection c : connectedPlayers.keySet()){
            if (c.getUsername().equalsIgnoreCase(username)){
                reconnectedPlayer = getPlayerByClient(c);
                connectedPlayers.remove(c);
                connectedPlayers.put(user, reconnectedPlayer);
                reconnectedPlayer.setDisconnected(false);
            }
            else {
                opponents.add(getPlayerByClient(c));
            }
        }
        user.sendMessage(new ReconnectionMessage(game.getGameBoard(), reconnectedPlayer, opponents));
    }

    public Game getGame(){
        return this.game;
    }
}
