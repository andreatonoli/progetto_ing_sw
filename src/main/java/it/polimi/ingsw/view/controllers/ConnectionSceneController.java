package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ConnectionSceneController extends GenericController{

    private GuiInputHandler guiHandler;

    @FXML
    private Button rmi;
    @FXML
    private Button socket;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        rmi.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.rmiButtonClicked();
        });
        socket.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.socketButtonClicked();
        });

    }

}
