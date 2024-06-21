package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiInputHandler;
import it.polimi.ingsw.view.gui.GuiScenes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class StarterFlipSceneController extends GenericController{

    @FXML
    private ImageView card;
    @FXML
    private Button flip;
    @FXML
    private Button next;

    Image front;
    Image back;
    ImageView miniFrontView;
    ImageView miniBackView;
    Card starter;

    GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        flip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(card.getImage().equals(front)){
                card.setImage(back);
            }
            else{
                card.setImage(front);
            }
        });
        next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType yes = new ButtonType("Confirm");
            ButtonType no = new ButtonType("Cancel");
            a.setTitle("Placing starter card");
            a.setHeaderText("Do you want to place the card on this side?");
            if(card.getImage().equals(front)){
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
                if(card.getImage().equals(front)){
                    guiHandler.nextStarterCardButtonClicked(true, starter);
                    Platform.runLater(() -> {
                        Gui.setScene(Gui.getScenes().get(GuiScenes.ACHIEVEMENT_CHOICE_SCENE.ordinal()));
                    });
                }
                else{
                    guiHandler.nextStarterCardButtonClicked(false, starter);
                    Platform.runLater(() -> {
                        Gui.setScene(Gui.getScenes().get(GuiScenes.ACHIEVEMENT_CHOICE_SCENE.ordinal()));
                    });
                }
            }
            else if(result.get().getText().equals("Cancel")){
                a.close();
            }
        });

    }

    public void setFace(Card starter){
        this.starter = starter;
        int number = starter.getCardNumber() + 80;
        front = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(number) + ".png"));
        miniFrontView = new ImageView(front);
        miniFrontView.setFitHeight(42*3);
        miniFrontView.setFitWidth(63*3);
        back = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(number) + ".png"));
        miniBackView = new ImageView(back);
        miniBackView.setFitHeight(42*3);
        miniBackView.setFitWidth(63*3);
        card.setImage(front);
    }

}
