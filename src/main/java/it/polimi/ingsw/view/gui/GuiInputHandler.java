package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.Ui;
import it.polimi.ingsw.view.gui.controllers.*;
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
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_ADDRESS_SCENE.ordinal())));
    }

    public void nextAddressButtonClicked(String s){
        address = s;
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.CONNECTION_SCENE.ordinal())));
    }

    public void rmiButtonClicked() {
        connection = "rmi";
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_PORT_SCENE.ordinal())));
    }

    public void socketButtonClicked() {
        connection = "socket";
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_PORT_SCENE.ordinal())));
    }

    public void nextPortButtonClicked(String s){
        if(connection.equals("rmi")){
            if(s.equals("default")){
                try{
                    client = new RMIClient(address, Server.rmiPort, this);
                    client.login();
                }
                catch(RemoteException e){
                    System.err.println(e.getMessage());
                }
            }
            else{
                try{
                    client = new RMIClient(address, Integer.parseInt(s), this);
                    client.login();
                }
                catch(RemoteException e){
                    System.err.println(e.getMessage());
                }            
            }
        }
        else{
            if(s.equals("default")){
                client = new SocketClient(address, Server.socketPort, this);
                client.login();
            }
            else{
                client = new SocketClient(address, Integer.parseInt(s), this);
                client.login();
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

    public void nextLobbySizeButtonClicked(int lobbySize) {
        client.setLobbySize(lobbySize);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_SCENE.ordinal())));
    }

    public void nextStarterCardButtonClicked(boolean side, Card starter){
        client.placeStarterCard(side, starter);
    }

    public void chosenAchievement(Achievement selectedAchievement){
        client.chooseAchievement(selectedAchievement);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_SETUP_SCENE.ordinal())));
    }

    public void chosenColor(Color selectedColor){
        client.chooseColor(selectedColor);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_COLOR_SCENE.ordinal())));
    }

    public void drawDeckButtonClicked(String s){
        client.drawCard(s);
    }

    public void drawCardButtonClicked(String s){
        client.drawCardFromBoard(Integer.parseInt(s));
    }

    public void placeCard(Card cardToPlace, int[] placingCoordinates){ client.placeCard(cardToPlace, placingCoordinates); }

    public void sendGlobalMessageButtonClicked(String message){ client.sendChatMessage(message); }

    public void sendMessageButtonClicked(String message, String receiver){
        client.sendChatMessage(receiver, message);
    }

    public void otherPlayersBoardButtonClicked(PlayerBean other, PlayerBean player, GameBean game, ArrayList<PlayerBean> players){
        Platform.runLater(() -> {
            OtherPlayerBoardsSceneController c = (OtherPlayerBoardsSceneController) GuiScenes.getController(GuiScenes.OTHER_PLAYER_BOARDS_SCENE);
            c.setBoard(other, player, game, players);
            Gui.setScene(Gui.getScenes().get(GuiScenes.OTHER_PLAYER_BOARDS_SCENE.ordinal()));
        });
    }

    public void backButtonClicked(PlayerBean player, GameBean game, ArrayList<PlayerBean> opponents){
        Platform.runLater(() -> {
            MainSceneController c = (MainSceneController) GuiScenes.getController(GuiScenes.MAIN_SCENE);
            c.setBoard(player, game, opponents);
            Gui.setScene(Gui.getScenes().get(GuiScenes.MAIN_SCENE.ordinal()));
        });
    }

    @Override
    public void reset() {

    }

    @Override
    public void handleReconnection() {

    }

    @Override
    public String askNickname() {
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.LOGIN_SCENE.ordinal())));
        return null;
    }

    //TODO: TOGLIERE
    @Override
    public void askNickname(int lobby) {

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
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.LOBBY_SIZE_SCENE.ordinal())));
    }

    @Override
    public void askSide(Card starterCard) {
        Platform.runLater(() -> {
            StarterFlipSceneController c = (StarterFlipSceneController) GuiScenes.getController(GuiScenes.STARTER_FLIP_SCENE);
            c.setFace(starterCard);
            Gui.setScene(Gui.getScenes().get(GuiScenes.STARTER_FLIP_SCENE.ordinal()));
        });
    }

    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {
        Platform.runLater(() -> {
            MainSceneController c = (MainSceneController) GuiScenes.getController(GuiScenes.MAIN_SCENE);
            c.setBoard(player, game, players);
            Gui.setScene(Gui.getScenes().get(GuiScenes.MAIN_SCENE.ordinal()));
        });
    }

    @Override
    public void askAchievement(Achievement[] choices) {
        AchievementChoiceSceneController c = (AchievementChoiceSceneController) GuiScenes.getController(GuiScenes.ACHIEVEMENT_CHOICE_SCENE);
        c.setAchievements(choices);
    }

    @Override
    public void askColor(List<Color> colors) {
        Platform.runLater(() -> {
            ColorChoiceSceneController c = (ColorChoiceSceneController) GuiScenes.getController(GuiScenes.COLOR_CHOICE_SCENE);
            c.setColors(colors);
            Gui.setScene(Gui.getScenes().get(GuiScenes.COLOR_CHOICE_SCENE.ordinal()));
        });
    }

    @Override
    public void setMessage(String message, boolean isError) {

    }

    @Override
    public void declareWinners(ArrayList<String> winners) {
        Platform.runLater(() -> {
            WinnersSceneController c = (WinnersSceneController) GuiScenes.getController(GuiScenes.WINNERS_SCENE);
            c.setWinners(winners);
            Gui.setScene(Gui.getScenes().get(GuiScenes.WINNERS_SCENE.ordinal()));
        });
    }
}
