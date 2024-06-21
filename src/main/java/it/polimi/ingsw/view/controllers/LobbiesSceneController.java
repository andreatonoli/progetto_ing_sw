package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import javafx.event.ActionEvent;
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

    public void bindEvents(){
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.createLobbyButtonClicked(startingGamesId, gamesWhitDisconnectionsId);
        });
        reconnect.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.reconnectLobbyButtonClicked(startingGamesId, gamesWhitDisconnectionsId);
        });
    }

    public void setLobbies(List<Integer> freeLobbies, List<Integer> freeReconnectLobbies){
        startingGamesId = freeLobbies;
        gamesWhitDisconnectionsId = freeReconnectLobbies;
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
            b.setOnAction(event -> {
                guiHandler.joinLobbyButtonClicked(finalI, freeLobbies, freeReconnectLobbies);
            });

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

