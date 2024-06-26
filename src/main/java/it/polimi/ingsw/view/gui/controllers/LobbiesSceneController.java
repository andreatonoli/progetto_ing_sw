package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiInputHandler;
import it.polimi.ingsw.view.gui.GuiScenes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class LobbiesSceneController extends GenericController {

    @FXML
    private VBox v;
    @FXML
    private Button create;
    @FXML
    private Button reconnect;

    GuiInputHandler guiHandler;
    List<Integer> startingGamesId;
    List<Integer> gamesWhitDisconnectionsId;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    /**
     * Method that binds the events to the buttons.
     */
    public void bindEvents(){
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.createLobbyButtonClicked(startingGamesId, gamesWhitDisconnectionsId));
        reconnect.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.reconnectLobbyButtonClicked(startingGamesId, gamesWhitDisconnectionsId));
    }

    /**
     * Method that sets the lobbies to join.
     * @param freeLobbies the lobbies to join
     * @param freeReconnectLobbies the lobbies to reconnect to
     */
    public void setLobbies(List<Integer> freeLobbies, List<Integer> freeReconnectLobbies){
        startingGamesId = freeLobbies;
        gamesWhitDisconnectionsId = freeReconnectLobbies;
        v.getChildren().clear();
        for (Integer i : freeLobbies){
            HBox h = new HBox();
            Text t = new Text("Lobby " + i);
            t.setFont(new Font(70));
            t.setId("setup-text");
            Button b = new Button ("Join lobby " + i);
            b.setFont(new Font(30));
            b.setPrefSize(230, 30);
            b.setId("setup-small-button");
            int finalI = i;
            b.setOnAction(event -> Platform.runLater(() -> {
                LoginSceneController lsc = (LoginSceneController) GuiScenes.getController(GuiScenes.LOGIN_SCENE);
                lsc.setNextAction(2);
                lsc.setNextActionValue(finalI);
                lsc.setFreeLobbies(freeLobbies);
                lsc.setFreeReconnectLobbies(freeReconnectLobbies);
                Gui.setScene(Gui.getScenes().get(GuiScenes.LOGIN_SCENE.ordinal()));
            }));

            h.getChildren().addAll(t, b);
            h.setSpacing(40);
            h.setAlignment(Pos.CENTER);
            v.getChildren().add(h);
        }

        //add padding to the bottom only for the last row
        HBox h = new HBox();
        h.setPadding(new Insets(0, 0, 50, 0));

    }

}

