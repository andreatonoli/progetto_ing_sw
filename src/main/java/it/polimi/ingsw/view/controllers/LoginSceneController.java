package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.GuiInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class LoginSceneController extends GenericController{

    @FXML
    private TextField username;
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
            if(username.getText().isEmpty()){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Username error");
                a.setHeaderText("Please write a valid username");
                a.setContentText("Your username is empty.\nPlease choose another username");
                a.show();
            }
            else{
                guiHandler.nextLoginButtonClicked(username.getText());
            }
        });
    }

}

