package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class WinnersSceneController extends GenericController{

    @FXML
    Text header;
    @FXML
    VBox v;

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
    }

    public void setWinners(ArrayList<String> winners){
        if(winners.size() < 2){
            header.setText("The winner is:");
        }
        else{
            header.setText("The winners are:");
        }
        for(String s : winners){
            Text t = new Text(s);
            t.setId("setup-text");
            v.setSpacing(10);
            v.getChildren().add(t);
        }
    }


}
