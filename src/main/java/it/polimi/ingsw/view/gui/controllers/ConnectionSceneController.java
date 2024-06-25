package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
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

    /**
     * Method that binds the events to the buttons.
     */
    public void bindEvents(){
        rmi.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.rmiButtonClicked();
        });
        socket.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.socketButtonClicked();
        });

    }

}
