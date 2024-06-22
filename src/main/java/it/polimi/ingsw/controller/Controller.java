package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.ActionMessage;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.observer.Observable;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller extends Observable {
    /**
     * Reference to the game (model) controlled by {@code this}
     */
    private final Game game;

    /**
     * Unique identifier of the game
     */
    private final int id;

    /**
     * Map to connect the different Connections to their representation on the model
     */
    private final ConcurrentHashMap<Connection, Player> connectedPlayers;

    /**
     * Reference to the turn handler that manages the turns of the players
     */
    private final TurnHandler turnHandler;

    /**
     * Queue that stores all the actions required by the players to be carried out by the controller
     */
    private final BlockingQueue<ActionMessage> actionQueue;

    /**
     * String that stores the username of the player in turn
     */
    private String playerInTurn;

    /**
     * Boolean that stores the state of the setup phase
     */
    private boolean setupFinished = false;

    /**
     * Boolean that stores the state of the ending cycle
     */
    private boolean endingCycle = false;

    /**
     * Boolean that stores the state of the processing action
     */
    private boolean processingAction = false;

    /**
     * Constructor of the class.
     * @param numPlayers number of players in the game.
     * @param id unique identifier of the game.
     */
    public Controller(int numPlayers, int id){
        this.id = id;
        this.game = new Game(numPlayers);
        this.turnHandler = new TurnHandler(game, this);
        this.connectedPlayers = new ConcurrentHashMap<>();
        this.actionQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Getter of the id attribute.
     * @return the unique identifier of the game.
     */
    public int getId(){
        return id;
    }

    /**
     * This method adds the tasks required by the player to the queue.
     * @param action requested by the player.
     */
    public void addAction(ActionMessage action){
        this.actionQueue.add(action);
    }

    /**
     * reads one action from the queue and performs it.
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

    /**
     * This method sets a timer to pick the action from the queue every 100 milliseconds.
     */
    public void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                execAction();
            }
        }, 0, 100);
    }

    /**
     * Permits the client to join the game.
     * @param user of the client who's joining the game.
     * @return {@code true} if the game is full, {@code false} otherwise.
     */
    public boolean joinGame(Connection user) throws FullLobbyExeption {
        if (game.isFull()) {
            user.sendMessage(new GenericMessage("\nthe game is full, please retry.\n"));
            throw new FullLobbyExeption();
        }
        Player player = new Player(user.getUsername(), game);
        this.connectedPlayers.put(user, player);
        this.addObserver(user);
        this.turnHandler.addObserver(user);
        user.setLobby(this);
        game.addPlayer(player);
        getConnectedPlayersMessage();
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
    public void startGame() {
        try {
            game.startGame();
        } catch (NotEnoughPlayersException e) {
            System.err.println(e.getMessage());
            return;
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
        //setting the first player to set up its game
        playerInTurn = game.getFirstPlayer().getUsername();
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
            if (!u.getUsername().equals(playerInTurn)){
                u.sendMessage(new GenericMessage("Waiting for other players to finish their setup"));
            }
        }
        pickQueue();
    }

    /**
     * Picks the top card of the deck and calls addInHand to give it to the player.
     * @param user who wants to draw a card.
     * @param chosenDeck deck from which user wants to draw a card.
     */
    public void drawCard(Connection user, String chosenDeck){
        LinkedList<Card> deck;
        boolean isResource = false;
        if (chosenDeck.equalsIgnoreCase("resource")){
            deck = game.getGameBoard().getResourceDeck();
            isResource = true;
        }
        else if (chosenDeck.equalsIgnoreCase("gold")){
            deck = game.getGameBoard().getGoldDeck();
        }
        else {
            user.sendMessage(new GenericMessage("\nSelected deck does not exist.\n"));
            return;
        }
        try {
            Card drawedCard = this.getPlayerByClient(user).drawCard(deck);
            if (game.getGameBoard().decksAreEmpty() && !endingCycle){
                endingCycle = true;
                turnHandler.startEnd(this.getPlayerByClient(user));
            }
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            notifyAll(new UpdateDeckMessage(deck.getFirst().getColor(), isResource));
            user.sendMessage(new UpdateCardMessage(drawedCard));
        } catch (EmptyException | NotInTurnException | FullHandException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Permits the player to take one card from the board, then replaces it with the same type card drew from the decks.
     * @param user who wants to take the card.
     * @param index of the card taken by the player (0,1 are from commonResources and 2,3 are from commonGold).
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
                user.sendMessage(new GenericMessage("\nThere is no card in this position.\n"));
                return;
            }
            Card drawedCard = this.getPlayerByClient(user).drawCardFromBoard(card);
            if (isGold){
                notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_GOLD_UPDATE, game.getGameBoard().getCommonGold()[index-2], index-2));
                notifyAll(new UpdateDeckMessage(game.getGameBoard().getGoldDeck().getFirst().getColor(), false));
            }
            else{
                notifyAll(new CommonCardUpdateMessage(MessageType.COMMON_RESOURCE_UPDATE, game.getGameBoard().getCommonResource()[index], index));
                notifyAll(new UpdateDeckMessage(game.getGameBoard().getResourceDeck().getFirst().getColor(), true));
            }
            turnHandler.changePlayerState(this.getPlayerByClient(user));
            user.sendMessage(new UpdateCardMessage(drawedCard));
        } catch (CardNotFoundException | NotInTurnException | FullHandException e) {
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Place the card on the chosen coordinates.
     * @param user who asked to place the card.
     * @param card to place.
     * @param coordinates where the player wants to place the card.
     */
    public void placeCard(Connection user, Card card, int[] coordinates) {
        try {
            Player p = this.getPlayerByClient(user);
            boolean present = false;
            for (Card c : p.getCardInHand()){
                if (c.equals(card)){
                    present = true;
                }
            }
            if (!present){
                user.sendMessage(new CardInHandMessage(p.getCardInHand()));
                user.sendMessage(new GenericMessage("\nThis card does not belong to your hand.\n"));
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
            notifyAll(new ScoreUpdateMessage(p.getPoints(), p.getUsername()));
            notifyAll(new PlayerBoardUpdateMessage(p.getPlayerBoard(), p.getUsername()));
        } catch (NotInTurnException | OccupiedCornerException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException e) {
            //If any exception is caught first we send the error message, and then we restore the card the player tried to place
            user.sendMessage(new UpdateCardMessage(card));
            user.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Permits the player to place the starter card on the board.
     * @param user who wants to place the starter card.
     * @param starterCard to be placed.
     */
    public void placeStarterCard(Connection user, Card starterCard){
        Player player = getPlayerByClient(user);
        if (!starterCard.equals(player.getPlayerBoard().getStarterCard())){
            user.sendMessage(new StarterCardMessage(player.getPlayerBoard().getStarterCard()));
            user.sendMessage(new GenericMessage("\nSome error occurred, please retry.\n"));
            return;
        }
        player.getPlayerBoard().setStarterCard(starterCard);
    }

    /**
     * Permits the player to set its personal achievement.
     * @param user to which belongs the chosen achievement.
     * @param achievement to be set.
     */
    public void chooseObj(Connection user, Achievement achievement){
        Player player = getPlayerByClient(user);
        if (!achievement.equals(player.getPersonalObj()[0]) && !achievement.equals(player.getPersonalObj()[1])){
            user.sendMessage(new AchievementMessage(MessageType.PRIVATE_ACHIEVEMENT,player.getPersonalObj()));
            user.sendMessage(new GenericMessage("\nSome error occurred, please retry.\n"));
            return;
        }
        player.setChosenObj(achievement);
        chooseColor(user);
    }

    /**
     * Permits the player to choose the color of its pion.
     * @param user who have to choose the color.
     */
    public void chooseColor(Connection user) {
            user.sendMessage(new ColorRequestMessage(game.getAvailableColors()));
    }

    /**
     * Setter to set the color of the pion of the player.
     * @param user who wants to move the pion.
     * @param color chosen by the player.
     */
    public void setColor(Connection user, Color color){
        if (!game.getAvailableColors().contains(color)){
            this.chooseColor(user);
            user.sendMessage(new ErrorMessage("\nChoose one color among these"));
            return;
        }
        this.game.getAvailableColors().remove(color);
        getPlayerByClient(user).setPionColor(color);
        notifyAll(new ColorResponseMessage(user.getUsername(), color));
        notifyAll(new PlayerBoardUpdateMessage(getPlayerByClient(user).getPlayerBoard(), user.getUsername()));
        playerInTurn = turnHandler.changeSetupPlayer();
        if (playerInTurn == null){
            setupFinished = true;
            game.setGameState(GameState.IN_GAME);
            notifyAll(new GameStateMessage(game.getGameState()));
        }
    }

    /**
     * Getter to get the player in turn.
     * @return the player in turn.
     */
    public String getPlayerInTurn(){
        return playerInTurn;
    }

    /**
     * Method to send a chat message to a player.
     * @param sender of the message.
     * @param receiver of the message.
     * @param message to be sent.
     */
    public void sendChatMessage(Connection sender, Connection receiver, String message){
        try {
            getPlayerByClient(sender).sendChatMessage(getPlayerByClient(receiver), message);
            sender.sendMessage(new ChatMessage(getPlayerByClient(sender).getChat()));
            receiver.sendMessage(new ChatMessage(getPlayerByClient(receiver).getChat()));
        } catch (PlayerNotFoundException e) {
            sender.sendMessage(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Method to send a chat message to all the players.
     * @param sender of the message.
     * @param message to be sent.
     */
    public void sendChatMessage(Connection sender, String message){
        getPlayerByClient(sender).sendChatMessage(message);
        notifyAll(new ChatMessage(getPlayerByClient(sender).getChat()));
    }

    /**
     * Getter to get the player by its connection.
     * @param user to get the player.
     * @return the player.
     */
    public synchronized Player getPlayerByClient(Connection user){
        return this.connectedPlayers.get(user);
    }

    /**
     * Method that will change the playerState of a player who disconnected while in turn.
     * @param username of the player that disconnected.
     */
    public void disconnectedWhileInTurn(String username){
        Player player = game.getPlayerByUsername(username);
        turnHandler.disconnectedWhileInTurn(player);
    }

    /**
     * Method that will change the playerState of a player who disconnected while setting up.
     * @param user who disconnected.
     * @param inTurn boolean that is true if the player was in turn, false otherwise.
     */
    public void disconnectedWhileSetupping(Connection user, Boolean inTurn){
        Player player = game.getPlayerByUsername(user.getUsername());
        PlayerBoard pb = player.getPlayerBoard();
        Color color = game.getAvailableColors().getFirst();
        this.game.getAvailableColors().remove(color);
        if (pb.getCardPosition().isEmpty()){
            pb.setStarterCard(pb.getStarterCard());
            player.setChosenObj(player.getPersonalObj()[0]);
            player.setPionColor(color);
        }
        else if (player.getChosenObj() == null){
            player.setChosenObj(player.getPersonalObj()[0]);
            player.setPionColor(color);
        }
        else {
            player.setPionColor(color);
        }
        notifyAll(new PlayerBoardUpdateMessage(getPlayerByClient(user).getPlayerBoard(), user.getUsername()));
        if (inTurn){
            playerInTurn = turnHandler.changeSetupPlayer();
            if (playerInTurn == null){
                setupFinished = true;
                game.setGameState(GameState.IN_GAME);
                notifyAll(new GameStateMessage(game.getGameState()));
            }
        }
    }

    /**
     * Sends back the model state to the player who has reconnected.
     * Precondition: username is equal to one, and only one, username.
     * @param user who reconnected.
     */
    public void reconnectBackup(Connection user){
        String username = user.getUsername();
        Player reconnectedPlayer = null;
        List<Player> opponents = new ArrayList<>();
        Set<Connection> connectionSet = connectedPlayers.keySet();
        for (Connection c : connectionSet){
            if (c.getUsername().equalsIgnoreCase(username)){
                reconnectedPlayer = getPlayerByClient(c);
                connectedPlayers.remove(c);
                connectedPlayers.put(user, reconnectedPlayer);
                this.removeObserver(c);
                this.addObserver(user);
                this.turnHandler.removeObserver(c);
                this.turnHandler.addObserver(user);
                reconnectedPlayer.setDisconnected(false);
            }
            else {
                opponents.add(getPlayerByClient(c));
            }
        }
        user.sendMessage(new ReconnectionMessage(game.getGameBoard(), reconnectedPlayer, opponents));
    }

    /**
     * Getter of the game attribute.
     * @return the game.
     */
    public Game getGame(){
        return this.game;
    }

    /**
     * Getter of the turnHandler attribute.
     * @return the turnHandler.
     */
    public TurnHandler getTurnHandler() {
        return this.turnHandler;
    }

    /**
     * Sends a message to all the players connected to the server with the number of the connected players.
     */
    public void getConnectedPlayersMessage(){
        notifyAll(new GenericMessage("Players: " + this.connectedPlayers.keySet().size() + "/" + this.game.getLobbySize()));
    }

    /**
     * Removes the player from the server.
     */
    public void removeFromServer(){
        boolean last = false;
        int count = 0;
        Set<Connection> connections = connectedPlayers.keySet();
        for (Connection c : connections){
            count++;
            if (count == game.getLobbySize()){
                last = true;
            }
            c.removeFromServer(last);
        }
    }
}
