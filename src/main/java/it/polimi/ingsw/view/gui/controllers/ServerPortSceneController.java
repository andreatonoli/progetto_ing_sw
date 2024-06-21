package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ServerPortSceneController extends GenericController{

    @FXML
    private TextField port;
    @FXML
    private Button next;

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(port.getText().isEmpty()){
                guiHandler.nextPortButtonClicked("default");
            }
            else{
                guiHandler.nextPortButtonClicked(port.getText());
            }
        });
    }

}

