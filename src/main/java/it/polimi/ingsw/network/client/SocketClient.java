package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.Ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient implements ClientInterface {
    private Socket socket;
    private String username;
    private Ui view;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean disconnected = false;
    private Card starterCard;
    private Card[] commonGold;
    private Card[] commonResources;
    private final Achievement[] commonAchievement;
    private PlayerBean player;
    private ArrayList<PlayerBean> opponents;
    //private HashMap<Integer, Card> board1;
    //private HashMap<Integer, Card> board2;
    //private HashMap<Integer, Card> board3;
    //private HashMap<Integer, Card> board4;
    public SocketClient(String username, String address, int port, Ui view){
        this.username = username;
        this.view = view;
        this.player = new PlayerBean(this.username);
        this.commonAchievement = new Achievement[2];
        this.commonResources = new Card[2];
        this.commonGold = new Card[2];
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
    //TODO: fare lettura parallela dei messaggi
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
            case CARD_HAND:
                //Copied the message body into the player's cards
                System.arraycopy(((CardInHandMessage) message).getHand(), 0, player.getHand(), 0, 3);
                break;
            case COMMON_ACHIEVEMENT:
                System.arraycopy(((AchievementMessage) message).getAchievements(), 0, this.commonAchievement, 0, 2);
                break;
            case PRIVATE_ACHIEVEMENT:
                this.player.setAchievement(this.view.chooseAchievement(((AchievementMessage) message).getAchievements()));
                sendMessage(new SetPrivateAchievementMessage(this.username, this.player.getAchievement()));
                break;
            case COMMON_GOLD_UPDATE:
                if(commonGold[0] == null){
                    commonGold[0] = ((CommonCardUpdateMessage) message).getCard();
                }
                else{
                    commonGold[1] = ((CommonCardUpdateMessage) message).getCard();
                }
                break;
            case COMMON_RESOURCE_UPDATE:
                if(commonResources[0] == null){
                    commonResources[0] = ((CommonCardUpdateMessage) message).getCard();
                }
                else{
                    commonResources[1] = ((CommonCardUpdateMessage) message).getCard();
                }
                break;
            case STARTER_CARD:
                this.starterCard = ((StarterCardMessage) message).getCard();
                this.view.printCard(starterCard);
                boolean choice = this.view.askToFlip();
                if (choice){
                    sendMessage(new FlipRequestMessage(this.username, this.starterCard));
                }
                else{
                    sendMessage(new PlaceStarterRequestMessage(this.username, this.starterCard));
                }
                break;
            case SCOREBOARD_UPDATE:
                //this.view.printView(this.player.getBoard(), this.player.getHand(), this.username, this.commonResources, this.commonGold, this.commonAchievement, this.opponents, this.player.getChat());
                break;
            case PLAYER_STATE:
                PlayerState playerState = ((PlayerStateMessage) message).getState();
                //this.view.printPlayerState(playerState);
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
    public void placeACard() {

    }

    @Override
    public void drawACard() {

    }

    @Override
    public ArrayList<PlayerBean> getPlayers() {
        return null;
    }
}
