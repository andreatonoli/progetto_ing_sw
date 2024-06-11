package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
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

public class ReconnectSceneController {

    @FXML
    private VBox v = new VBox();

    @FXML
    public void initialize() {

    }

    public void setLobbies(List<Integer> freeLobbies){
        for (Integer i : freeLobbies){
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
                reconnectLobbyButtonClicked(finalI);
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

    private void reconnectLobbyButtonClicked(Integer selectedLobby){ Gui.addReturnValue(String.valueOf(selectedLobby)); }
}

