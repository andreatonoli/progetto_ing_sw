package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LobbiesSceneController {

    @FXML
    private VBox v = new VBox();

    @FXML
    public void initialize() {

    }

    public void setLobbies(Integer freeLobbies){
        for (int i = 0; i < freeLobbies; i++){
            HBox h = new HBox();
            Text t = new Text("Lobby " + (i + 1));
            t.setFont(new Font(70));
            t.setId("setup-text");
            Button b = new Button ("Join lobby " + (i + 1));
            b.setFont(new Font(30));
            b.setPrefSize(230, 30);
            b.setId("setup-small-button");
            int finalI = i;
            b.setOnAction(event -> {
                joinLobbyButtonClicked(finalI);
            });

            h.getChildren().addAll(t, b);
            h.setSpacing(40);
            //add padding to the top only for the first row
            if(i == 0){
                h.setPadding(new Insets(30, 0, 0, 0));
            }
            //add padding to the bottom only for the last row
            if(i == freeLobbies - 1){
                h.setPadding(new Insets(0, 0, 50, 0));
            }
            h.setAlignment(Pos.CENTER);
            v.getChildren().add(h);
        }
    }

    private void joinLobbyButtonClicked(Integer selectedLobby){ Gui.addReturnValue(String.valueOf(selectedLobby)); }

    @FXML
    private void createLobbyButtonClicked(){ Gui.addReturnValue(String.valueOf("newLobby")); }

}

