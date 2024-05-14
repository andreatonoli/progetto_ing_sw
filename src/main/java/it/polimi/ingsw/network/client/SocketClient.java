package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
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

public class SocketClient implements ClientInterface {
    private Socket socket;
    private String username;
    private Ui view;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean disconnected = false;
    private final GameBean game;
    private PlayerBean player;
    private ArrayList<PlayerBean> opponents;
    public SocketClient(String username, String address, int port, Ui view){
        this.username = username;
        this.view = view;
        this.player = new PlayerBean(this.username);
        this.opponents = new ArrayList<>();
        this.game = new GameBean();
        this.startClient(address, port);
    }
    public void startClient(String address, int port){
        try {
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            sendMessage(new LoginResponseMessage(this.username));
            //TODO: capire come far chiudere la connessione
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
            update(message);
        } catch (IOException | ClassNotFoundException e) {
            this.onDisconnect();
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(Message message){
        try {
            out.writeObject(message);
            out.reset();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void update(Message message){
        String name;
        switch (message.getType()){
            case USERNAME_REQUEST:
                System.out.println("Username is already taken, please choose another: ");
                this.username = this.view.askNickname();
                sendMessage(new LoginResponseMessage(this.username));
                player.setUsername(this.username);
                break;
            case FREE_LOBBY:
                int freeLobbySize = ((FreeLobbyMessage) message).getLobbyNumber();
                int response = this.view.selectGame(freeLobbySize);
                if (response == freeLobbySize){
                    int lobbySize = this.view.setLobbySize();
                    sendMessage(new NumPlayerResponseMessage(this.username, lobbySize));
                }
                else {
                    sendMessage(new LobbyIndexMessage(this.username, response));
                }
                break;
            case NUM_PLAYER_REQUEST:
                int lobbySize = this.view.setLobbySize();
                sendMessage(new NumPlayerResponseMessage(this.username, lobbySize));
                break;
            case OPPONENTS:
                ArrayList<String> playersName = ((OpponentsMessage) message).getPlayers();
                for (String s : playersName){
                    if(!s.equalsIgnoreCase(username)){
                        opponents.add(new PlayerBean(s));
                    }
                }
                break;
            case CARD_HAND:
                //Copied the message body into the player's cards
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, player.getHand(), 0, 3);
                break;
            case COMMON_ACHIEVEMENT:
                System.arraycopy(((AchievementMessage) message).getAchievements(), 0, game.getCommonAchievement(), 0, 2);
                break;
            case PRIVATE_ACHIEVEMENT:
                this.player.setAchievement(this.view.chooseAchievement(((AchievementMessage) message).getAchievements()));
                sendMessage(new SetPrivateAchievementMessage(this.username, this.player.getAchievement()));
                break;
            case COMMON_GOLD_UPDATE:
                if(game.getCommonGold()[0] == null){
                    game.setCommonGold(0,((CommonCardUpdateMessage) message).getCard());
                }
                else{
                    game.setCommonGold(1,((CommonCardUpdateMessage) message).getCard());
                }
                break;
            case COMMON_RESOURCE_UPDATE:
                if(game.getCommonResources()[0] == null){
                    game.setCommonResources(0,((CommonCardUpdateMessage) message).getCard());
                }
                else{
                    game.setCommonResources(1,((CommonCardUpdateMessage) message).getCard());
                }
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
                this.view.printStarterCard(starterCard);
                boolean choice = this.view.askSide();
                if (!choice) {
                    starterCard.setCurrentSide();
                }
                player.setStarterCard(starterCard);
                sendMessage(new PlaceStarterRequestMessage(username, starterCard));
                break;
            case CARD_UPDATE:
                Card drawedCard = ((UpdateCardMessage) message).getCard();
                player.setCardinHand(drawedCard);
                this.view.printViewWithCommands(this.player, this.game, this.opponents);                break;
            case SCORE_UPDATE:
                name = ((ScoreUpdateMessage) message).getName();
                int points = ((ScoreUpdateMessage) message).getPoint();
                if (name.equalsIgnoreCase(username)){
                    player.addPoints(points);
                }
                else {
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equalsIgnoreCase(name)){
                            p.addPoints(points);
                        }
                    }
                }
                this.view.printViewWithCommands(this.player, this.game, this.opponents);                break;
            case PLAYER_STATE:
                PlayerState playerState = ((PlayerStateMessage) message).getState();
                this.player.setState(playerState);
                break;
            case PLAYERBOARD_UPDATE:
                PlayerBoard playerBoard = ((PlayerBoardUpdateMessage) message).getpBoard();
                name = ((PlayerBoardUpdateMessage) message).getName();
                if (name.equalsIgnoreCase(username)){
                    player.setBoard(playerBoard);
                    this.view.printViewWithCommands(this.player, this.game, this.opponents);               }
                else{
                    for (PlayerBean p : opponents){
                        if (p.getUsername().equals(name)){
                            p.setBoard(playerBoard);
                        }
                    }
                }
                break;
            case GENERIC_MESSAGE:
                this.view.showText(message.toString());
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

    }

    @Override
    public void sendChatMessage(String receiver, String message) {

    }

    @Override
    public void placeCard(Card card, int[] placingCoordinates) {
        if (this.player.getState().equals(PlayerState.PLAY_CARD)){
            sendMessage(new PlaceMessage(username, card, placingCoordinates));
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
