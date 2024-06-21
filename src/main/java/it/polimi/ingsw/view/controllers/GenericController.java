package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.GuiInputHandler;
import javafx.fxml.FXML;

public class GenericController {

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
    }

}
