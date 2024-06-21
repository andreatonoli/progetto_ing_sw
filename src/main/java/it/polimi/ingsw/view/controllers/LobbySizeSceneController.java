package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LobbySizeSceneController extends GenericController{

    @FXML
    private TextField lobbySize;
    @FXML
    private Button next;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(lobbySize.getText().isEmpty() || Integer.parseInt(lobbySize.getText()) < 2 || Integer.parseInt(lobbySize.getText()) > 4 ){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Lobby size error");
                a.setHeaderText("Please write a valid size");
                a.setContentText("Your lobby size is either empty or not a number.\nPlease choose another size");
                a.show();
            }
            else{
                guiHandler.nextLobbySizeButtonClicked(Integer.parseInt(lobbySize.getText()));
            }
        });
    }

}

