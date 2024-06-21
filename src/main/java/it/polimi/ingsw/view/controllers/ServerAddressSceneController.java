package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import javafx.event.ActionEvent;
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

    //@FXML
    //private void nextButtonClicked(ActionEvent e){
    //    if(text.getText().isEmpty()){
    //        Gui.addReturnValue("localhost");
    //    }
    //    else{
    //        Gui.addReturnValue(text.getText());
    //    }
    //}

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

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

