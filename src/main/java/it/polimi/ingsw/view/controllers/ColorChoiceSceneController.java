package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.Gui;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class ColorChoiceSceneController {

    @FXML
    private HBox h = new HBox();

    ImageView[] views = null;
    ImageView[] miniViews = null;

    @FXML
    public void initialize() {

    }

    public void setColors(List<Color> colors){
        Image red = new Image(getClass().getResourceAsStream("/images/red.png"));
        ImageView redView = new ImageView(red);
        redView.setFitHeight(150);
        redView.setFitWidth(150);
        ImageView miniRedView = new ImageView(red);
        miniRedView.setFitHeight(50);
        miniRedView.setFitWidth(50);

        Image blue = new Image(getClass().getResourceAsStream("/images/blue.png"));
        ImageView blueView = new ImageView(blue);
        blueView.setFitHeight(150);
        blueView.setFitWidth(150);
        ImageView miniBlueView = new ImageView(blue);
        miniBlueView.setFitHeight(50);
        miniBlueView.setFitWidth(50);

        Image green = new Image(getClass().getResourceAsStream("/images/green.png"));
        ImageView greenView = new ImageView(green);
        greenView.setFitHeight(150);
        greenView.setFitWidth(150);
        ImageView miniGreenView = new ImageView(green);
        miniGreenView.setFitHeight(50);
        miniGreenView.setFitWidth(50);

        Image yellow = new Image(getClass().getResourceAsStream("/images/yellow.png"));
        ImageView yellowView = new ImageView(yellow);
        yellowView.setFitHeight(150);
        yellowView.setFitWidth(150);
        ImageView miniYellowView = new ImageView(yellow);
        miniYellowView.setFitHeight(50);
        miniYellowView.setFitWidth(50);

        this.views = new ImageView[]{redView, blueView, greenView, yellowView};
        this.miniViews = new ImageView[]{miniRedView, miniBlueView, miniGreenView, miniYellowView};

        for (int i = 0; i < colors.size(); i++){
            Button b = new Button ();
            b.setStyle("-fx-background-radius: 5em;" +
                       "-fx-background-color: transparent;" +
                       "-fx-border-color: transparent"
            );
            b.setPrefSize(150, 150);
            switch (colors.get(i)) {
                case RED -> b.setGraphic(views[0]);
                case BLUE -> b.setGraphic(views[1]);
                case GREEN -> b.setGraphic(views[2]);
                case YELLOW -> b.setGraphic(views[3]);
            }
            int finalI = i;
            b.setOnAction(event -> {
                ColorButtonClicked(colors.get(finalI));
            });

            h.getChildren().add(b);
            //add padding to the top only for the first row
            if(i == 0){
                h.setPadding(new Insets(30, 0, 0, 0));
            }
            h.setAlignment(Pos.CENTER);
            h.setSpacing(50);
        }
    }

    private void ColorButtonClicked(Color colorClicked){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Confirm");
        ButtonType no = new ButtonType("Cancel");
        a.setTitle("Choosing color");
        a.setHeaderText("Do you want to choose this color?");
        switch (colorClicked) {
            case RED -> a.setGraphic(miniViews[0]);
            case BLUE -> a.setGraphic(miniViews[1]);
            case GREEN -> a.setGraphic(miniViews[2]);
            case YELLOW -> a.setGraphic(miniViews[3]);
        }
        a.getButtonTypes().clear();
        a.getButtonTypes().addAll(no, yes);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isEmpty()){
            a.close();
        }
        else if(result.get().getText().equals("Confirm")){
            Gui.addReturnValue(String.valueOf(colorClicked));
        }
        else if(result.get().getText().equals("Cancel")){
            a.close();
        }
    }

}

