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

public class Gui extends Application{

    static Stage stage;
    static FXMLLoader loader;
    static Parent root;
    private ClientInterface client;
    protected static BlockingQueue<String> returnValue = new LinkedBlockingQueue<>();
    private static GuiInputHandler handler;
    private static ArrayList<Parent> scenes = new ArrayList<>();
    private static ArrayList<Object> controllers = new ArrayList<>();


    public static void addReturnValue(String s){
        returnValue.add(s);
    }

    public static void main(String[] args){
        handler = new GuiInputHandler();
        launch(args);
    }

    public static void loadAllScenes(){
        for(GuiScenes s : GuiScenes.values()){
            loader = new FXMLLoader(Gui.class.getResource(GuiScenes.getFxml(s)));
            try {
                root = loader.load();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            scenes.add(root);
            //controllers.add(loader.getController());
        }
    }

    public static ArrayList<Parent> getScenes(){ return scenes; }
    public static ArrayList<Object> getControllers(){ return controllers; }

    public static void setScene(Parent r){
        stage.getScene().setRoot(r);
    }

    @Override
    public void start(Stage s) throws Exception {

        loadAllScenes();
        stage = s;

        stage.setScene(new Scene(scenes.get(GuiScenes.START_SCENE.ordinal())));
        stage.show();

        //Timer t = new Timer();
        //t.schedule(new TimerTask() {
        //    @Override
        //    public void run() {
        //        if(!returnValue.isEmpty()){
        //            t.cancel();
        //            returnValue.poll();
        //            askServerAddress();
        //            askConnection();
        //            askServerPort(connection);
        //        }
        //    }
        //}, 0, 500);

    }

    //@Override
    //public void handleReconnection() {
//
    //}
//
    //@Override
    //public void askNickname(){
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.LOGIN_SCENE);
    //        stage.getScene().setRoot(root);
    //    });
    //    try{
    //        while(returnValue.isEmpty()){
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
    //}
//
    //public String askServerAddress() {
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.SERVER_ADDRESS_SCENE);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    return result;
    //}
//
    //public String askConnection() {
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.CONNECTION_SCENE);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    return result;
    //}
//
    //public int askServerPort(String connectionType) {
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.SERVER_PORT_SCENE);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    if (result.equals("default")) {
    //        if (connectionType.equalsIgnoreCase("rmi")) {
    //            try {
    //                this.client = new RMIClient(address, Server.rmiPort, this);
    //            }
    //            catch(RemoteException e){
    //                System.err.println(e.getMessage());
    //            }
    //            return Server.rmiPort;
    //        }
    //        else {
    //            this.client = new SocketClient(address, Server.socketPort, this);
    //            return Server.socketPort;
    //        }
    //    }
    //    else {
    //        if (connectionType.equalsIgnoreCase("rmi")) {
    //            try {
    //                this.client = new RMIClient(address, Integer.parseInt(result), this);
    //            }
    //            catch(RemoteException e){
    //                System.err.println(e.getMessage());
    //            }
    //        }
    //        else {
    //            this.client = new SocketClient(address, Integer.parseInt(result), this);
    //        }
    //        return Integer.parseInt(result);
    //    }
    //}
//
    //@Override
    //public void selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId) {
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.LOBBIES_SCENE);
    //        LobbiesSceneController c = loader.getController();
    //        //c.setLobbies(startingGamesId);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    if(result.equals("newLobby")){
    //        //return -1;
    //    }
    //    else{
    //        System.out.println(result);
    //        //return Integer.parseInt(result);
    //    }
    //}
//
    //@Override
    //public void askLobbySize() {
    //    int result = 0;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.LOBBY_SIZE_SCENE);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = Integer.parseInt(returnValue.poll());
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.WAITING_SCENE);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    //return result;
    //}
//
    //@Override
    //public void askSide(Card starterCard) {
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.STARTER_FLIP_SCENE);
    //        StarterFlipSceneController c = loader.getController();
    //        c.setFace(starterCard);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    if(result.equals("front")){
    //        //return true;
    //    }
    //    else{
    //        //return false;
    //    }
    //}
//
    //@Override
    //public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.MAIN_SCENE);
    //        MainSceneController c = loader.getController();
    //        c.setBoard(player, game, players);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //        String[] splittedResult = result.split("§");
    //        for(int i = 0; i < splittedResult.length; i++){
    //            System.out.println(splittedResult[i]);
    //        }
    //        switch(splittedResult[0]){
    //            case "1" -> {
    //                client.placeCard(player.getHand()[Integer.parseInt(splittedResult[1])], new int[]{Integer.parseInt(splittedResult[2]), Integer.parseInt(splittedResult[3])});
    //            }
    //            case "2" -> {
    //                client.drawCard(splittedResult[1]);
    //            }
    //            case "3" -> {
    //                client.drawCardFromBoard(Integer.parseInt(splittedResult[1]));
    //            }
    //            case "4" -> {
    //                if(splittedResult[1].equals("global")){ client.sendChatMessage(splittedResult[2]); }
    //                else { client.sendChatMessage(splittedResult[1], splittedResult[2]); }
    //            }
    //            case "5" -> {
    //                for(PlayerBean p : players){
    //                    if(p.getUsername().equals(splittedResult[1]));{
    //                        otherPlayerBoards(player, game, players, p);
    //                    }
    //                }
    //            }
    //        }
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
    //}
//
    //@Override
    //public void askAchievement(Achievement[] choices) {
//
    //}
//
    //@Override
    //public void askColor(List<Color> colors) {
//
    //}
//
    //public void otherPlayerBoards(PlayerBean player, GameBean game, ArrayList<PlayerBean> players, PlayerBean other){
    //    String result = null;
    //    Platform.runLater(() -> {
    //        loadScene(GuiScenes.OTHER_PLAYER_BOARDS_SCENE);
    //        OtherPlayerBoardsSceneController c = loader.getController();
    //        c.setBoard(other);
    //        stage.getScene().setRoot(root);
    //    });
//
    //    try {
    //        while (returnValue.isEmpty()) {
    //            Thread.sleep(200);
    //        }
    //        result = returnValue.poll();
    //    }
    //    catch (InterruptedException e){
    //        System.err.println(e.getMessage());
    //    }
//
    //    if(result.equals("backToMain")){
    //        printViewWithCommands(player, game, players);
    //    }
    //}
//
    //@Override
    //public void setMessage(String message, boolean isError) {
//
    //}
//
    //@Override
    //public void declareWinners(ArrayList<String> winners) {
//
    //}
//
}//
//