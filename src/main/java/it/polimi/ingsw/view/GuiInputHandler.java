package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GuiInputHandler implements Ui {

    static GuiInputHandler instance;
    private ClientInterface client;

    public static GuiInputHandler getInstance(){
        if(instance == null){
            instance = new GuiInputHandler();
        }
        return instance;
    }

    public void playButtonClicked(){

    }

    public void nextAddressButtonClicked(String s){
        Gui.address = s;
    }

    public void rmiButtonClicked() {
        Gui.connection = "rmi";
    }

    public void socketButtonClicked() {
        Gui.connection = "socket";
    }

    public void nextPortButtonClicked(String s){
        if(Gui.connection.equals("rmi")){
            if(s.equals("default")){
                Gui.port = Server.rmiPort;
            }
            else{
                Gui.port = Integer.parseInt(s);
            }
        }
        else{
            if(s.equals("default")){
                Gui.port = Server.socketPort;
            }
            else{
                Gui.port = Integer.parseInt(s);
            }
        }
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

    public String askServerAddress() {
        return "";
    }

    public int askServerPort(String connectionType) {
        return 0;
    }

    @Override
    public void selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {

    }

    @Override
    public void askLobbySize() {

    }

    @Override
    public void askSide(Card starterCard) {

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
