package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class StartSceneController extends GenericController{

    @FXML
    Button play;

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    /**
     * Method that binds the events to the buttons.
     */
    public void bindEvents(){
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.playButtonClicked();
        });
    }

}
