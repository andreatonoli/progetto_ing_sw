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
    public String askNickname() {
        Platform.runLater(() -> {
            Gui.setScene(Gui.getScenes().get(GuiScenes.LOGIN_SCENE.ordinal()));
        });
        return "";
    }

    @Override
    public String askServerAddress() {
        return "";
    }

    @Override
    public int askServerPort(String connectionType) {
        return 0;
    }

    @Override
    public int selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
        return 0;
    }

    @Override
    public int setLobbySize() {
        return 0;
    }

    @Override
    public boolean askSide(Card starterCard) {
        return false;
    }

    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {

    }

    @Override
    public Achievement chooseAchievement(Achievement[] choices) {
        return null;
    }

    @Override
    public Color chooseColor(List<Color> colors) {
        return null;
    }

    @Override
    public void setMessage(String message, boolean isError) {

    }

    @Override
    public void declareWinners(ArrayList<String> winners) {

    }
}
