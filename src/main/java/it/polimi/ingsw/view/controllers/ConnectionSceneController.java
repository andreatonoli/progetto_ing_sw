package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ConnectionSceneController {

    @FXML
    private void rmiButtonClicked(ActionEvent e){ Gui.addReturnValue("rmi"); }

    @FXML
    private void socketButtonClicked(ActionEvent e){ Gui.addReturnValue("socket"); }

}
