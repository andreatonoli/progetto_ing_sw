package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;

public class GenericController {

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
    }

}
