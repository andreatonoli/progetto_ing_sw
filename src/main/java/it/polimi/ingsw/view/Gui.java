package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.controllers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Gui extends Application implements Ui{

    Stage stage;
    FXMLLoader loader;
    Parent root;
    static String address;
    static int port;
    static String connection;
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

    public static void setConnection(String c) { connection = c; }
    public static String getConnection() { return connection; }

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
        stage.show();

//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(!returnValue.isEmpty()){
//                    t.cancel();
//                    returnValue.poll();
//                    address = askServerAddress();
//                    connection =  askConnection();
//                    port =  askServerPort(connection);
//                }
//            }
//        }, 0, 500);

    }

    @Override
    public void reset() {

    }

    @Override
    public void handleReconnection() {

    }

    @Override
    public void askNickname() {

    }

    @Override
    public void askNickname(int lobby) {

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
//
//    @Override
//    public void reset() {
//
//    }
//
//    @Override
//    public void handleReconnection() {
//
//    }
//
//    @Override
//    public void askNickname(){
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.LOGIN_SCENE);
//            stage.getScene().setRoot(root);
//        });
//        try{
//            while(returnValue.isEmpty()){
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public void askNickname(int lobby) {
//
//    }
//
//    public String askServerAddress() {
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.SERVER_ADDRESS_SCENE);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        return result;
//    }
//
//    public String askConnection() {
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.CONNECTION_SCENE);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        return result;
//    }
//
//    public int askServerPort(String connectionType) {
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.SERVER_PORT_SCENE);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        if (result.equals("default")) {
//            if (connectionType.equalsIgnoreCase("rmi")) {
//                try {
//                    this.client = new RMIClient(address, Server.rmiPort, this);
//                }
//                catch(RemoteException e){
//                    System.err.println(e.getMessage());
//                }
//                return Server.rmiPort;
//            }
//            else {
//                this.client = new SocketClient(address, Server.socketPort, this);
//                return Server.socketPort;
//            }
//        }
//        else {
//            if (connectionType.equalsIgnoreCase("rmi")) {
//                try {
//                    this.client = new RMIClient(address, Integer.parseInt(result), this);
//                }
//                catch(RemoteException e){
//                    System.err.println(e.getMessage());
//                }
//            }
//            else {
//                this.client = new SocketClient(address, Integer.parseInt(result), this);
//            }
//            return Integer.parseInt(result);
//        }
//    }
//
//    @Override
//    public void selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.LOBBIES_SCENE);
//            LobbiesSceneController c = loader.getController();
//            c.setLobbies(startingGamesId);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        if(result.equals("newLobby")){
//            //return -1;
//        }
//        else{
//            System.out.println(result);
//            //return Integer.parseInt(result);
//        }
//    }
//
//    @Override
//    public void askLobbySize() {
//        int result = 0;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.LOBBY_SIZE_SCENE);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = Integer.parseInt(returnValue.poll());
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.WAITING_SCENE);
//            stage.getScene().setRoot(root);
//        });
//
//        //return result;
//    }
//
//    @Override
//    public void askSide(Card starterCard) {
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.STARTER_FLIP_SCENE);
//            StarterFlipSceneController c = loader.getController();
//            c.setFace(starterCard);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        if(result.equals("front")){
//            //return true;
//        }
//        else{
//            //return false;
//        }
//    }
//
//    @Override
//    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.MAIN_SCENE);
//            MainSceneController c = loader.getController();
//            c.setBoard(player, game, players);
//            stage.getScene().setRoot(root);
//            stage.setMaximized(true);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//            String[] splittedResult = result.split("§");
//            switch(splittedResult[0]){
//                case "1" -> {
//                    client.placeCard(player.getHand()[Integer.parseInt(splittedResult[1])], new int[]{Integer.parseInt(splittedResult[2]), Integer.parseInt(splittedResult[3])});
//                }
//                case "2" -> {
//                    client.drawCard(splittedResult[1]);
//                }
//                case "3" -> {
//                    client.drawCardFromBoard(Integer.parseInt(splittedResult[1]));
//                }
//                case "4" -> {
//                    if(splittedResult[1].equals("global")){ client.sendChatMessage(splittedResult[2]); }
//                    else { client.sendChatMessage(splittedResult[1], splittedResult[2]); }
//                }
//                case "5" -> {
//                    for(PlayerBean p : players){
//                        if(p.getUsername().equals(splittedResult[1]));{
//                            otherPlayerBoards(player, game, players, p);
//                        }
//                    }
//                }
//            }
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public void askAchievement(Achievement[] choices) {
//
//    }
//
//    @Override
//    public void askColor(List<Color> colors) {
//
//    }
//
//    public void otherPlayerBoards(PlayerBean player, GameBean game, ArrayList<PlayerBean> players, PlayerBean other){
//        String result = null;
//        Platform.runLater(() -> {
//            loadScene(GuiScenes.OTHER_PLAYER_BOARDS_SCENE);
//            OtherPlayerBoardsSceneController c = loader.getController();
//            c.setBoard(other);
//            stage.getScene().setRoot(root);
//        });
//
//        try {
//            while (returnValue.isEmpty()) {
//                Thread.sleep(200);
//            }
//            result = returnValue.poll();
//        }
//        catch (InterruptedException e){
//            System.err.println(e.getMessage());
//        }
//
//        if(result.equals("backToMain")){
//            printViewWithCommands(player, game, players);
//        }
//    }
//
//    @Override
//    public void setMessage(String message, boolean isError) {
//
//    }
//
//    @Override
//    public void declareWinners(ArrayList<String> winners) {
//
//    }

}
