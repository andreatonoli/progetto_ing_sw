package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ServerAddressSceneController extends GenericController{

    @FXML
    private TextField address;
    @FXML
    private Button next;

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
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(address.getText().isEmpty()){
                guiHandler.nextAddressButtonClicked("localhost");
            }
            else{
                guiHandler.nextAddressButtonClicked(address.getText());
            }
        });
    }

}

