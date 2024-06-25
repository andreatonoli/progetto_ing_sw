package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class WinnersSceneController extends GenericController{

    @FXML
    Text header;
    @FXML
    VBox winners;

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
    }

    /**
     * Method that sets the winners of the game.
     * @param winner the winners of the game
     */
    public void setWinners(ArrayList<String> winner){
        if(winner.size() < 2){
            header.setText("The winner is:");
        }
        else{
            header.setText("The winners are:");
        }
        for(String s : winner){
            Text t = new Text(s);
            t.setId("setup-text");
            t.setStyle("-fx-font-size: 70.0;");
            winners.setSpacing(10);
            winners.getChildren().add(t);
        }
    }


}
