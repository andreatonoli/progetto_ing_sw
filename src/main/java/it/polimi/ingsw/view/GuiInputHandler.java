package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.controllers.LobbiesSceneController;
import it.polimi.ingsw.view.controllers.ReconnectSceneController;
import it.polimi.ingsw.view.controllers.StarterFlipSceneController;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GuiInputHandler implements Ui {

    static GuiInputHandler instance;
    private ClientInterface client;
    private String address;
    private String connection;

    public static GuiInputHandler getInstance(){
        if(instance == null){
            instance = new GuiInputHandler();
        }
        return instance;
    }

    public void playButtonClicked(){
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_ADDRESS_SCENE.ordinal()));
        });
    }

    public void nextAddressButtonClicked(String s){
        address = s;
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.CONNECTION_SCENE.ordinal()));
        });
    }

    public void rmiButtonClicked() {
        connection = "rmi";
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_PORT_SCENE.ordinal()));
        });
    }

    public void socketButtonClicked() {
        connection = "socket";
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_PORT_SCENE.ordinal()));
        });
    }

    public void nextPortButtonClicked(String s){
        if(connection.equals("rmi")){
            if(s.equals("default")){
                try{
                    client = new RMIClient(address, Server.rmiPort, this);
                }
                catch(RemoteException e){
                    System.err.println(e.getMessage());
                }
            }
            else{
                try{
                    client = new RMIClient(address, Integer.parseInt(s), this);
                }
                catch(RemoteException e){
                    System.err.println(e.getMessage());
                }            
            }
        }
        else{
            if(s.equals("default")){
                client = new SocketClient(address, Server.socketPort, this);
            }
            else{
                client = new SocketClient(address, Integer.parseInt(s), this);
            }
        }
    }
    
    public void nextLoginButtonClicked(String username){
        client.setNickname(username);
    }
    
    public void joinLobbyButtonClicked(int selectedLobby, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        client.setOnConnectionAction(selectedLobby, startingGamesId, gamesWithDisconnectionsId);
    }

    public void createLobbyButtonClicked(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        client.setOnConnectionAction(-1, startingGamesId, gamesWithDisconnectionsId);
    }

    public void reconnectLobbyButtonClicked(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        Platform.runLater(() -> {
            ReconnectSceneController c = (ReconnectSceneController) GuiScenes.getController(GuiScenes.LOBBIES_SCENE);
            c.setLobbies(startingGamesId, gamesWithDisconnectionsId);
            Gui.setScene(Gui.getScenes().get(GuiScenes.RECONNECT_SCENE.ordinal()));
        });
    }
    
    public void reconnectButtonClicked(int selectedLobby, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        client.setOnConnectionAction(selectedLobby, startingGamesId, gamesWithDisconnectionsId);
    }

    public void nextLobbySizeButtonClicked(int lobbySize){
        client.setLobbySize(lobbySize);
    }

    public void nextStarterCardButtonClicked(boolean side, Card starter){
        client.placeStarterCard(side, starter);
    }

    @Override
    public void handleReconnection() {

    }

    @Override
    public void askNickname() {
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.LOGIN_SCENE.ordinal()));
        });
    }
    

    @Override
    public void selectGame(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId) {
        Platform.runLater(() -> {
            LobbiesSceneController c = (LobbiesSceneController) GuiScenes.getController(GuiScenes.LOBBIES_SCENE);
            c.setLobbies(startingGamesId, gamesWithDisconnectionsId);
            Gui.setScene(Gui.getScenes().get(GuiScenes.LOBBIES_SCENE.ordinal()));
        });
    }

    @Override
    public void askLobbySize() {
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.LOBBY_SIZE_SCENE.ordinal()));
        });
    }

    @Override
    public void askSide(Card starterCard) {
        Platform.runLater(() -> {
            StarterFlipSceneController c = (StarterFlipSceneController) GuiScenes.getController(GuiScenes.LOBBIES_SCENE);
            c.setFace(starterCard);
            Gui.setScene(Gui.getScenes().get(GuiScenes.STARTER_FLIP_SCENE.ordinal()));
        });
    }

    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {

    }

    @Override
    public void askAchievement(Achievement[] choices) {

    }

    @Override
    public void askColor(List<Color> colors) {

    }

    @Override
    public void setMessage(String message, boolean isError) {

    }

    @Override
    public void declareWinners(ArrayList<String> winners) {

    }
}
