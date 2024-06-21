package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import it.polimi.ingsw.view.GuiScenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class StartSceneController {

    @FXML
    Button play;

    GuiInputHandler guiHandler;


    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.playButtonClicked();
        });
    }

}
