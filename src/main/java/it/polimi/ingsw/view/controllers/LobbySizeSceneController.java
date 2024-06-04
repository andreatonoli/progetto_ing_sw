package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class LobbySizeSceneController {

    @FXML
    private TextField text = new TextField();

    @FXML
    private void nextButtonClicked(ActionEvent e){
        if(text.getText().isEmpty() || Integer.parseInt(text.getText()) < 2 || Integer.parseInt(text.getText()) > 4 ){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Lobby size error");
            a.setHeaderText("Please write a valid size");
            a.setContentText("Your lobby size is either empty or not a number.\nPlease choose another size");
            a.show();
        }
        else{
            Gui.addReturnValue(text.getText());
        }
    }

}

