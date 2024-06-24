package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class LoginSceneController extends GenericController{

    @FXML
    private TextField username;
    @FXML
    private Button next;

    private GuiInputHandler guiHandler;
    private int nextAction = 0;
    private int nextActionValue;
    private List<Integer> freeLobbies = null;
    private List<Integer> freeReconnectLobbies = null;

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
                handleNextAction();
            }
        });
    }

    public void setNextAction(int i){
        this.nextAction = i;
    }

    public void setNextActionValue(int i){
        this.nextActionValue = i;
    }

    public void setFreeLobbies(List<Integer> freeLobbies){
        this.freeLobbies = freeLobbies;
    }

    public void setFreeReconnectLobbies(List<Integer> freeReconnectLobbies){
        this.freeReconnectLobbies = freeReconnectLobbies;
    }

    public void handleNextAction(){
        switch (nextAction){
            case 1:
                guiHandler.nextLobbySizeButtonClicked(nextActionValue);
                break;
            case 2:
                guiHandler.joinLobbyButtonClicked(nextActionValue, freeLobbies, freeReconnectLobbies);
                break;
            case 3:
                guiHandler.reconnectButtonClicked(nextActionValue, freeLobbies, freeReconnectLobbies);
                break;
            default:
                break;
        }
    }

}

