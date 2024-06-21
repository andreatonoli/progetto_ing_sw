package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import it.polimi.ingsw.view.GuiScenes;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class ReconnectSceneController extends GenericController{

    @FXML
    private VBox v = new VBox();

    public void setLobbies(List<Integer> freeLobbies, List<Integer> freeReconnectLobbies){
        for (Integer i : freeReconnectLobbies){
            HBox h = new HBox();
            Text t = new Text("Lobby " + i);
            t.setFont(new Font(70));
            t.setId("setup-text");
            Button b = new Button ("Reconnect to lobby " + i);
            b.setFont(new Font(30));
            b.setPrefSize(320, 30);
            b.setId("setup-small-button");
            int finalI = i;
            b.setOnAction(event -> {
                Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.LOGIN_SCENE.ordinal())));
                LoginSceneController lsc = (LoginSceneController) GuiScenes.getController(GuiScenes.LOGIN_SCENE);
                lsc.setNextAction(3);
                lsc.setNextActionValue(finalI);
                lsc.setFreeLobbies(freeLobbies);
                lsc.setFreeReconnectLobbies(freeReconnectLobbies);
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

