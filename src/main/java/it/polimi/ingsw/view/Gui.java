package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Gui extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Codex Naturalis");
        Button button = new Button("Play!");
        button.setFont(new Font(20));
        button.setPrefSize(150, 600);
        button.setOnAction(event -> {
            stage.getScene().setRoot(this.loginPage());
        });
        VBox v1 = new VBox(button);
        v1.setAlignment(Pos.BASELINE_CENTER);
        VBox v2 = new VBox(v1);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/background.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        v2.setBackground(new Background(i));
        v1.prefHeightProperty().bind(v2.heightProperty());
        v2.prefHeightProperty().bind(stage.heightProperty());
        stage.setScene(new Scene(v2, 1280, 720));
        stage.show();
    }

    public Pane loginPage()
    {
        Label l1 = new Label("Username:  ");
        l1.setFont(new Font(30));
        TextField text = new TextField();
        text.setPrefSize(200,40);
        Label l2 = new Label("Color:  ");
        l2.setFont(new Font(30));
        MenuButton m = new MenuButton();
        m.setPrefSize(200,40);
        Button b1 = new Button("Next");
        b1.setFont(new Font(20));
        b1.setPrefSize(200,40);

        //username and color
        HBox h1 = new HBox(l1,text);
        h1.setAlignment(Pos.CENTER);
        HBox h2 = new HBox(l2,m);
        h2.setAlignment(Pos.CENTER);
        VBox v1 = new VBox(h1 ,h2);
        v1.setAlignment(Pos.CENTER);
        v1.setSpacing(30);

        //next button
        HBox h3 = new HBox(b1);
        h3.setAlignment(Pos.BOTTOM_RIGHT);
        h3.setPadding(new Insets(0,20,20,0));

        BorderPane bp = new BorderPane();
        bp.setCenter(v1);
        bp.setBottom(h3);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/login_wallpaper.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

}
