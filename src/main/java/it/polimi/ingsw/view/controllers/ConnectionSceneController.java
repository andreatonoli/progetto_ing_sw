package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ConnectionSceneController {

    @FXML
    private void rmiButtonClicked(ActionEvent e){ Gui.addReturnValue("rmiConnection"); }

    @FXML
    private void socketButtonClicked(ActionEvent e){ Gui.addReturnValue("socketConnection"); }

}
