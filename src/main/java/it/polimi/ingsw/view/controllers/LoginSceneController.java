package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginSceneController {

    @FXML
    private TextField text = new TextField();

    @FXML
    private void nextButtonClicked(ActionEvent e){
        if(text.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Username error");
            a.setHeaderText("Please write a valid username");
            a.setContentText("Your username is empty.\nPlease choose another username");
            a.show();
        }
        else{
            Gui.addReturnValue(text.getText());
        }
    }

}

