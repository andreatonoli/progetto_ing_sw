package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerAddressSceneController {

    @FXML
    private TextField text = new TextField();

    @FXML
    private void nextButtonClicked(ActionEvent e){
        if(text.getText().isEmpty()){
            Gui.addReturnValue("localhost");
        }
        else{
            Gui.addReturnValue(text.getText());
        }
    }

}

