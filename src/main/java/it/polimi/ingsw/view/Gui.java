package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Gui extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Codex Naturalis");
        Button button = new Button("Play!");
        button.setPrefSize(200, 800);
        button.setOnAction(event -> {
            //chiama cambio scena
        });
        VBox container = new VBox(button);
        container.setPrefSize(200, 150);
        container.setAlignment(Pos.BASELINE_CENTER);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/background.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        container.setBackground(new Background(i));
        stage.setScene(new Scene(container));
        stage.setFullScreen(true);
        stage.show();
    }

}
