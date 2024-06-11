package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Gui extends Application implements Ui{

    Stage stage = new Stage();
    FXMLLoader loader;
    Parent root;
    private ClientInterface client;
    protected static BlockingQueue<String> returnValue = new LinkedBlockingQueue<>();
    //red, blue, green, yellow
    int[] freeColors = {1, 1, 1, 1};

    public static void addReturnValue(String s){
        returnValue.add(s);
    }


    public static void main(String[] args){
        launch(args);
    }

    public void loadScene(GuiScenes s) {
        this.loader = new FXMLLoader(getClass().getResource(GuiScenes.getFxml(s)));
        try {
            this.root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage s) throws Exception {

        this.stage = s;

        loader = new FXMLLoader(getClass().getResource(GuiScenes.getFxml(GuiScenes.START_SCENE)));
        Parent root = loader.load();
        stage.setScene(new Scene(root));

        List<Integer> freelobbies = new ArrayList<>(List.of(3, 5, 7));

        //StarterCard s1 = new StarterCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));

        Achievement a1 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)), 13);
        Achievement a2 = new AchievementDiagonal(Color.PURPLE, 4);
        Achievement[] a = new Achievement[]{a1, a2};

        List<Color> colors = new ArrayList<>(List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));

        loader = new FXMLLoader(getClass().getResource(GuiScenes.getFxml(GuiScenes.COLOR_CHOICE_SCENE)));
        root = loader.load();
        ColorChoiceSceneController c = loader.getController();
        c.setColors(colors);
        stage.getScene().setRoot(root);
        stage.show();

    }

    @Override
    public void handleReconnection() {

    }

    @Override
    public String askNickname(){
        String result = null;
        loadScene(GuiScenes.LOGIN_SCENE);
        stage.getScene().setRoot(root);
        try{
            while(returnValue.isEmpty()){
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        return result;
    }

    @Override
    public String askServerAddress() {
        String result = null;
        loadScene(GuiScenes.SERVER_ADDRESS_SCENE);
        stage.getScene().setRoot(root);

        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        return result;
    }

    @Override
    public int askServerPort(String connectionType) {
        String result = null;
        loadScene(GuiScenes.SERVER_PORT_SCENE);
        stage.getScene().setRoot(root);
        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        if(result.equals("default")){
            if(connectionType.equalsIgnoreCase("RMI")){
                return Server.rmiPort;
            }
            else{
                return Server.socketPort;
            }
        }
        else{
            return Integer.parseInt(result);
        }
    }

    @Override
    public int selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
        String result = null;
        loadScene(GuiScenes.LOBBIES_SCENE);
        LobbiesSceneController c = loader.getController();
        c.setLobbies(startingGamesId);
        stage.getScene().setRoot(root);
        stage.show();

        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        if(result.equals("newLobby")){
            return -1;
        }
        else{
            return Integer.parseInt(result);
        }
    }

    @Override
    public int setLobbySize() {
        int result = 0;
        loadScene(GuiScenes.LOBBY_SIZE_SCENE);
        stage.getScene().setRoot(root);

        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = Integer.parseInt(returnValue.poll());
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean askSide(Card starterCard) {
        String result = null;
        loadScene(GuiScenes.STARTER_FLIP_SCENE);
        StarterFlipSceneController c = loader.getController();
        //c.setFace(starter);
        stage.getScene().setRoot(root);

        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        if(result.equals("front")){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {

    }

    @Override
    public Achievement chooseAchievement(Achievement[] choices) {
        String result = null;
        loadScene(GuiScenes.ACHIEVEMENT_CHOICE_SCENE);
        AchievementChoiceSceneController c = loader.getController();
        c.setAchievements(choices);
        stage.getScene().setRoot(root);

        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        if(result.equals("chosenFirst")){
            return choices[0];
        }
        else{
            return choices[1];
        }
    }

    @Override
    public Color chooseColor(List<Color> colors) {
        String result = null;
        loadScene(GuiScenes.COLOR_CHOICE_SCENE);
        ColorChoiceSceneController c = loader.getController();
        c.setColors(colors);
        stage.getScene().setRoot(root);

        try {
            while (returnValue.isEmpty()) {
                Thread.sleep(200);
            }
            result = returnValue.poll();
        }
        catch (InterruptedException e){
            System.err.println(e.getMessage());
        }

        return colors.get(Integer.parseInt(result));
    }

    @Override
    public void setMessage(String message, boolean isError) {

    }

    @Override
    public void declareWinners(ArrayList<String> winners) {

    }
    
}
