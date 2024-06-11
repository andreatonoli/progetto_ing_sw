package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiScenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StartSceneController {

    @FXML
    private void playButtonClicked(ActionEvent e){
        Gui.addReturnValue("ready");
    }

}
