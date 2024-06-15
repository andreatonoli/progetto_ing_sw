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
    private ImageView a1;

    @FXML
    private ImageView a2;

    Image achievement1;
    Image achievement2;
    ImageView miniAchievement1View;
    ImageView miniAchievement2View;

    public void setAchievements(Achievement[] achievements){
        int number1 = achievements[0].getId() + 86;
        int number2 = achievements[1].getId() + 86;
        achievement1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number1) + ".png"));
        miniAchievement1View = new ImageView(achievement1);
        miniAchievement1View.setFitHeight(42*3);
        miniAchievement1View.setFitWidth(63*3);
        achievement2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number2) + ".png"));
        miniAchievement2View = new ImageView(achievement2);
        miniAchievement2View.setFitHeight(42*3);
        miniAchievement2View.setFitWidth(63*3);
        a1.setImage(achievement1);
        a2.setImage(achievement2);
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
