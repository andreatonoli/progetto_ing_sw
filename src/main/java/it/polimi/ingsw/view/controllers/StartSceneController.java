package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StartSceneController {

    @FXML
    private void playButtonClicked(ActionEvent e){ Gui.addReturnValue("readyToStart"); }

}
