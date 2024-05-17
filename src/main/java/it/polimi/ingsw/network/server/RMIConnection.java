package it.polimi.ingsw.network.server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.client.RMIClientHandler;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RMIConnection extends Connection {
    private RMIClientHandler client;
    private Controller lobby;
    private transient Server server;
    private String username;
    private Timer catchPing;
    private Timer ping;

    public RMIConnection(Server server, RMIClientHandler client, String username){
        this.client = client;
        this.server = server;
        this.username = username;
        this.setConnectionStatus(true);
    }

    public void ping(){
        ping = new Timer();
        catchPing = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(username+" connesso");
                pingClient();
            }
        }, 0, 500);

        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                ping.cancel();
                catchPing.cancel();
                //aggiungere quello che fa quando si scollega
                lobby.getGame().getPlayerByUsername(username).setDisconnected(true);
                setConnectionStatus(false);
                //
            }
        }, 4000, 4000);
    }
    private void pingClient(){
        try {
            client.pingNetwork();
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + " " + "in pingClient/RMIConnection");
        }
    }
    // se viene ricevuto un riscontro dal client entro 2000 allora riparte il timer
    // altrimenti il client verrà considerato disconnesso
    public void catchPing(){
        catchPing.cancel();
        catchPing = new Timer();
        catchPing.schedule(new TimerTask() {
            @Override
            public void run() {
                ping.cancel();
                catchPing.cancel();
                System.out.println("finito");
                //TODO: metti on disconnection
                lobby.getGame().getPlayerByUsername(username).setDisconnected(true);
                setConnectionStatus(false);
                //aggiungere quello che fa quando si scollega
                //io chiamerei tipo il controller per mandare una notifyall con il messaggio creato apposta
                //e per far salvare i dati del player disconnesso(?????)
            }
        }, 2000, 2000);
    }
    //TODO che se fa se non responde er pupone?

    @Override
    public void setLobby(Controller controller) {
        this.lobby = controller;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            this.client.update(message);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + "RMIConnection/sendMessage");
        }
    }

    @Override
    public void joinGame(List<Controller> startingGames){
        try{
            int response = this.client.joinGame(startingGames.size());
            if (response == startingGames.size()){
                this.server.createLobby(this.username, this.client.setLobbySize());
            }
            else{
                this.server.joinLobby(this.username, response);
            }
        } catch (RemoteException e){
            System.err.println(e.getMessage() + " in RMIConnection/joinGame");
        }
    }

    @Override
    public void createGame(){
        try {
            this.server.createLobby(this.username, this.client.setLobbySize());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public String getUsername(){
        return this.username;
    }

    public void flipCard(Card card){
        this.lobby.flipCard(this, card);
    }

    public void placeStarterCard(Card card) {
        this.lobby.placeStarterCard(this, card);
    }
    public void setAchievement(Achievement achievement){
        this.lobby.chooseObj(this, achievement);
    }

    public void placeCard(Card card, int[] placingCoordinates){
        this.lobby.placeCard(this, card, placingCoordinates);
    }
    public void drawCard(String chosenDeck){
        this.lobby.drawCard(this, chosenDeck);
    }
    public void drawCardFromBoard(int index){
        this.lobby.drawCardFromBoard(this, index);
    }
    @Override
    public void update(Message message) {
        this.sendMessage(message);
    }
}
