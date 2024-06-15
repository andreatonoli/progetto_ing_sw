package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class StarterFlipSceneController {

    @FXML
    private ImageView i;

    Image front;
    Image back;
    ImageView miniFrontView;
    ImageView miniBackView;

    public void setFace(Card starter){
        int number = starter.getCardNumber() + 80;
        front = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number) + ".png"));
        miniFrontView = new ImageView(front);
        miniFrontView.setFitHeight(42*3);
        miniFrontView.setFitWidth(63*3);
        back = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(number) + ".png"));
        miniBackView = new ImageView(back);
        miniBackView.setFitHeight(42*3);
        miniBackView.setFitWidth(63*3);
        i.setImage(front);
    }

    @FXML
    public void starterFlipped(ActionEvent e) {
        if(i.getImage().equals(front)){
            i.setImage(back);
        }
        else{
            i.setImage(front);
        }
    }

    @FXML
    public void nextButtonClicked(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Confirm");
        ButtonType no = new ButtonType("Cancel");
        a.setTitle("Placing starter card");
        a.setHeaderText("Do you want to place the card on this side?");
        if(i.getImage().equals(front)){
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
            if(i.getImage().equals(front)){
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
