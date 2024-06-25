package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;

public class GenericController {

    GuiInputHandler guiHandler;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
    }

}
