package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.Ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient implements ClientInterface {
    private Socket socket;
    private String username;
    private final Ui view;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean disconnected = false;
    private GameBean game;
    private PlayerBean player;
    private ArrayList<PlayerBean> opponents;
    private final BlockingQueue<Message> messageQueue;
    private boolean processingAction;
    private final Object outputLock = new Object();
    public SocketClient(String address, int port, Ui view){
        this.view = view;
        this.opponents = new ArrayList<>();
        this.game = new GameBean();
        messageQueue = new LinkedBlockingQueue<>();
        processingAction = false;
        pickQueue();
        new Thread(() -> this.startClient(address, port)).start();
    }
    //TODO: mi collego con username null e non lo setto mai più
    public void startClient(String address, int port){
        try {
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            sendMessage(new LoginResponseMessage());
            while(!disconnected){
                readMessage();
            }
        } catch (IOException e) {
            this.onDisconnect();
            System.err.println(e.getMessage());
            System.out.println("Connection successfully ended");
        }
    }
    public void readMessage(){
        Message message;
        try {
            message = (Message) in.readObject();
            if (message.getType().equals(MessageType.PING)){
                sendMessage(new CatchPingMessage(this.username));
            }
            else{
                messageQueue.add(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            this.onDisconnect();
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(Message message){
        if (!disconnected) {
            try {
                synchronized (outputLock) {
                    out.writeObject(message);
                    out.reset();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void pickQueue(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                processQueue();
            }
        }, 0, 500);
    }

    private void processQueue() {
        if (!messageQueue.isEmpty() && !processingAction) {
            Message message = messageQueue.poll();
            processingAction = true;
            this.update(message);
            processingAction = false;
            if (!messageQueue.isEmpty()) {
                processQueue();
            }
        }
    }

    //TODO: stampare view con comandi al passaggio del turno
    public void update(Message message){
        String name;
        switch (message.getType()){
            case RECONNECTION:
                this.player = ((ReconnectionMessage) message).getPlayerBean();
                this.game = ((ReconnectionMessage) message).getGameBean();
                //TODO cerca di passare copia della lista
                this.opponents = ((ReconnectionMessage) message).getOpponents();
                break;
            case USERNAME_REQUEST:
                System.out.println("Username is already taken, please choose another: ");
                this.username = this.view.askNickname();
                player.setUsername(this.username);
                int number = ((UsernameRequestMessage) message).getNumber();
                if (((UsernameRequestMessage) message).isCreation()){
                    sendMessage(new NumPlayerResponseMessage(username, number));
                }
                else {
                    sendMessage(new LobbyIndexMessage(this.username, number));
                }
                break;
            case NUM_PLAYER_REQUEST:
                int lobbySize = this.view.setLobbySize();
                this.username = view.askNickname();
                this.player = new PlayerBean(this.username);
                sendMessage(new NumPlayerResponseMessage(this.username, lobbySize));
                break;
            case FREE_LOBBY:
                List<Integer> startingGamesId = ((FreeLobbyMessage) message).getstartingGamesId();
                List<Integer> gamesWhitDisconnectionsId = ((FreeLobbyMessage) message).getgamesWhitDisconnectionsId();
                int response = this.view.selectGame(startingGamesId, gamesWhitDisconnectionsId);
                this.username = this.view.askNickname();
                this.player = new PlayerBean(username);
                if (response == -1){
                    int numOfPlayers = this.view.setLobbySize();
                    sendMessage(new NumPlayerResponseMessage(username, numOfPlayers));
                }
                else if (startingGamesId.contains(response)){
                    sendMessage(new LobbyIndexMessage(this.username, response));
                }
                else if (gamesWhitDisconnectionsId.contains(response)){
                    sendMessage(new ReconnectLobbyIndexMessage(this.username, response));
                }
                break;
            case GAME_STATE:
                GameState state = ((GameStateMessage) message).getState();
                game.setState(state);
                if (state.ordinal() >= 2){
                    this.view.printViewWithCommands(player, game, opponents);
                }
                break;
            case OPPONENTS:
                ArrayList<String> playersName = ((OpponentsMessage) message).getPlayers();
                for (String s : playersName){
                    if(!s.equalsIgnoreCase(username)){
                        opponents.add(new PlayerBean(s));
                    }
                }
                break;
            case COMMON_GOLD_UPDATE:
                int index = ((CommonCardUpdateMessage) message).getIndex();
                game.setCommonGold(index, ((CommonCardUpdateMessage) message).getCard());
                break;
            case COMMON_RESOURCE_UPDATE:
                int index1 = ((CommonCardUpdateMessage) message).getIndex();
                game.setCommonResources(index1, ((CommonCardUpdateMessage) message).getCard());
                break;
            case COMMON_ACHIEVEMENT:
                System.arraycopy(((AchievementMessage) message).getAchievements(), 0, game.getCommonAchievement(), 0, 2);
                break;
            case DECK_UPDATE:
                Color color = ((UpdateDeckMessage) message).getColor();
                boolean isResource = ((UpdateDeckMessage) message).getIsResource();
                if (isResource){
                    game.setResourceDeckRetro(color);
                }
                else{
                    game.setGoldDeckRetro(color);
                }
                break;
            case STARTER_CARD:
                Card starterCard = ((StarterCardMessage) message).getCard();
                boolean choice = this.view.askSide(starterCard);
                if (!choice) {
                    starterCard.setCurrentSide();
                }
                player.setStarterCard(starterCard);
                sendMessage(new PlaceStarterRequestMessage(username, starterCard));
                break;
            case CARD_HAND:
                //Copied the message body into the player's cards
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, player.getHand(), 0, 3);
                break;
            case PRIVATE_ACHIEVEMENT:
                this.player.setAchievement(this.view.chooseAchievement(((AchievementMessage) message).getAchievements()));
                sendMessage(new SetPrivateAchievementMessage(this.username, this.player.getAchievement()));
                break;
            case PLAYER_STATE:
                PlayerState playerState = ((PlayerStateMessage) message).getState();
                name = ((PlayerStateMessage) message).getName();
                if (username.equals(name)) {
                    player.setState(playerState);
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            p.setState(playerState);
                        }
                    }
                }
                break;
            case COLOR_REQUEST:
                Color chosenColor = this.view.chooseColor(((ColorRequestMessage) message).getColors());
                player.setPionColor(chosenColor);
                sendMessage(new ColorResponseMessage(username, chosenColor));
                break;
            case COLOR_RESPONSE:
                Color setColor = ((ColorResponseMessage) message).getColor();
                name = message.getSender();
                if (name.equalsIgnoreCase(username)){
                    player.setPionColor(setColor);
                }
                else{
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equalsIgnoreCase(name)){
                            p.setPionColor(setColor);
                            break;
                        }
                    }
                }
                break;
            case PLAYERBOARD_UPDATE:
                PlayerBoard playerBoard = ((PlayerBoardUpdateMessage) message).getpBoard();
                name = ((PlayerBoardUpdateMessage) message).getName();
                if (name.equalsIgnoreCase(username)){
                    player.setBoard(playerBoard);
                    this.view.printViewWithCommands(this.player, this.game, this.opponents);
                }
                else{
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            p.setBoard(playerBoard);
                        }
                    }
                }
                break;
            case CARD_UPDATE:
                Card drawedCard = ((UpdateCardMessage) message).getCard();
                player.setCardinHand(drawedCard);
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case SCORE_UPDATE:
                name = ((ScoreUpdateMessage) message).getName();
                int points = ((ScoreUpdateMessage) message).getPoint();
                if (name.equalsIgnoreCase(username)){
                    player.setPoints(points);
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equalsIgnoreCase(name)){
                            p.setPoints(points);
                        }
                    }
                }
                break;
            case CHAT:
                System.err.println(username);
                player.setChat(((ChatMessage) message).getChat());
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case WAITING_RECONNECTION:
                name = ((WaitingReconnectionMessage) message).getUsername();
                if (username.equals(name)) {
                    player.setState(PlayerState.NOT_IN_TURN);
                }
                this.view.printViewWithCommands(this.player, this.game, this.opponents);
                break;
            case DECLARE_WINNER:
                ArrayList<String> winners = ((WinnerMessage) message).getWinners();
                this.view.declareWinners(winners);
                sendMessage(new RemoveFromServerMessage(username));
                this.onDisconnect();
                break;
            case GENERIC_MESSAGE:
                if (game.getState().ordinal() > 1) {
                    this.view.setMessage(message.toString(), false);
                }
                else {
                    System.out.println(message);
                }
                break;
            case ERROR:
                this.view.setMessage(message.toString(), true);
                this.view.printViewWithCommands(player, game, opponents);
                break;
            default:
                break;
        }
    }
    public void onDisconnect(){
        try {
            this.disconnected = true;
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sendChatMessage(String message) {
        sendMessage(new AddToChatMessage(username, message));
    }

    @Override
    public void sendChatMessage(String receiver, String message) {
        sendMessage(new AddToChatMessage(username, receiver, message));
    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (this.player.getState().equals(PlayerState.PLAY_CARD)){ //TODO: imparare a leggere una griglia per capire se così traspongo giuste le coordinate
            sendMessage(new PlaceMessage(username, card, placingCoordinates));
            player.removeCardFromHand(card);
        }
        else{
            //TODO: errore profondo
        }
    }

    @Override
    public void drawCard(String chosenDeck) {
        if (this.player.getState().equals(PlayerState.DRAW_CARD)) {
            sendMessage(new DrawMessage(username, chosenDeck));
        }
        else {
            //TODO: errore profondo
        }
    }

    @Override
    public void drawCardFromBoard(int index) {
        if (this.player.getState().equals(PlayerState.DRAW_CARD)) {
            sendMessage(new DrawFromBoardMessage(username, index - 1));
        }
        else{
            //TODO: errore profondo
        }
    }

    @Override
    public ArrayList<PlayerBean> getPlayers() {
        return null;
    }
}
