package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.card.Achievement;
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

public class AchievementChoiceSceneController {

    @FXML
    private Button b1;

    @FXML
    private Button b2;

    ImageView achievement1View;
    ImageView achievement2View;
    ImageView miniAchievement1View;
    ImageView miniAchievement2View;

    public void setAchievements(Achievement[] achievements){
        int number1 = achievements[0].getId() + 86;
        int number2 = achievements[1].getId() + 86;
        Image a1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number1) + ".png"));
        achievement1View = new ImageView(a1);
        achievement1View.setFitHeight(42*6);
        achievement1View.setFitWidth(63*6);
        miniAchievement1View = new ImageView(a1);
        miniAchievement1View.setFitHeight(42*3);
        miniAchievement1View.setFitWidth(63*3);
        Image a2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number2) + ".png"));
        achievement2View = new ImageView(a2);
        achievement2View.setFitHeight(42*6);
        achievement2View.setFitWidth(63*6);
        miniAchievement2View = new ImageView(a2);
        miniAchievement2View.setFitHeight(42*3);
        miniAchievement2View.setFitWidth(63*3);
        b1.setGraphic(achievement1View);
        b2.setGraphic(achievement2View);
    }

    @FXML
    public void chosenFirstAchievement(ActionEvent e){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Confirm");
        ButtonType no = new ButtonType("Cancel");
        a.setTitle("Choosing achievement card");
        a.setHeaderText("Do you want to choose this achievement?");
        a.setGraphic(miniAchievement1View);
        a.getButtonTypes().clear();
        a.getButtonTypes().addAll(no, yes);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isEmpty()){
            a.close();
        }
        else if(result.get().getText().equals("Confirm")){
            Gui.addReturnValue("chosenFirst");
        }
        else if(result.get().getText().equals("Cancel")){
            a.close();
        }
    }

    @FXML
    public void chosenSecondAchievement(ActionEvent e){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Confirm");
        ButtonType no = new ButtonType("Cancel");
        a.setTitle("Choosing achievement card");
        a.setHeaderText("Do you want to choose this achievement?");
        a.setGraphic(miniAchievement2View);
        a.getButtonTypes().clear();
        a.getButtonTypes().addAll(no, yes);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isEmpty()){
            a.close();
        }
        else if(result.get().getText().equals("Confirm")){
            Gui.addReturnValue("chosenSecond");
        }
        else if(result.get().getText().equals("Cancel")){
            a.close();
        }
    }

}
