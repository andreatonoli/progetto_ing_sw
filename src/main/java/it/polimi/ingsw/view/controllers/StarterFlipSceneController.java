package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class StarterFlipSceneController {

    @FXML
    private Button b1;

    ImageView frontView;
    ImageView backView;
    ImageView miniFrontView;
    ImageView miniBackView;

    public void setFace(Card starter){
        int number = starter.getCardNumber() + 80;
        Image front = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number) + ".png"));
        frontView = new ImageView(front);
        frontView.setFitHeight(42*6);
        frontView.setFitWidth(63*6);
        miniFrontView = new ImageView(front);
        miniFrontView.setFitHeight(42*3);
        miniFrontView.setFitWidth(63*3);
        Image back = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(number) + ".png"));
        backView = new ImageView(back);
        backView.setFitHeight(42*6);
        backView.setFitWidth(63*6);
        miniBackView = new ImageView(back);
        miniBackView.setFitHeight(42*3);
        miniBackView.setFitWidth(63*3);
        b1.setGraphic(frontView);
    }

    @FXML
    public void starterFlipped(ActionEvent e) {
        if(b1.getGraphic().equals(frontView)){
            b1.setGraphic(backView);
        }
        else{
            b1.setGraphic(frontView);
        }
    }

    @FXML
    public void nextButtonClicked(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Confirm");
        ButtonType no = new ButtonType("Cancel");
        a.setTitle("Placing starter card");
        a.setHeaderText("Do you want to place the card on this side?");
        if(b1.getGraphic().equals(frontView)){
            a.setGraphic(miniFrontView);
        }
        else{
            a.setGraphic(miniBackView);
        }
        a.getButtonTypes().clear();
        a.getButtonTypes().addAll(no, yes);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isEmpty()){
            a.close();
        }
        else if(result.get().getText().equals("Confirm")){
            if(b1.getGraphic().equals(frontView)){
                Gui.addReturnValue("front");
            }
            else{
                Gui.addReturnValue("back");
            }
        }
        else if(result.get().getText().equals("Cancel")){
            a.close();
        }
    }
}
