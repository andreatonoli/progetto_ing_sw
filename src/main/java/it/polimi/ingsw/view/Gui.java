package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Gui extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Codex Naturalis");
        Button b1 = new Button("Play!");
        b1.setFont(new Font(40));
        b1.setPrefSize(150, 70);
        b1.setOnAction(event -> {
            stage.getScene().setRoot(this.lobbySelectionPage(stage));
        });
        HBox h1 = new HBox(b1);
        h1.setAlignment(Pos.CENTER);
        h1.setPadding(new Insets(500, 0, 0, 0));
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/background.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        BorderPane bp = new BorderPane();
        bp.setCenter(h1);
        bp.setBackground(new Background(i));
        stage.setScene(new Scene(bp, 1280, 720));
        stage.show();
    }

    public Pane loginPage(Stage stage) {
        Label l1 = new Label("Username:  ");
        l1.setFont(new Font(30));
        TextField text = new TextField();
        text.setPrefSize(200,40);
        Label l2 = new Label("Color:  ");
        l2.setFont(new Font(30));
        ChoiceBox<String> cb = new ChoiceBox<>();
        //aggiungere solo i colori che non sono già stati scelti
        cb.getItems().add("red");
        cb.getItems().add("green");
        cb.getItems().add("blue");
        cb.getItems().add("yellow");
        cb.getSelectionModel().selectFirst();
        cb.setPrefSize(200,40);
        Button b1 = new Button("Next");
        b1.setFont(new Font(20));
        b1.setPrefSize(200,40);
        b1.setOnAction(event -> {
            //controllo per username già scelti
            if(text.getText().isEmpty() || text.getText().equals("hydro")){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Username error");
                a.setHeaderText("Please write a valid username");
                a.setContentText("Your username is either empty or already taken.\nPlease choose another username");
                a.show();
            }
            else{
                //username
                String username = text.getText();
                //color
                String color = cb.getSelectionModel().getSelectedItem();
                //stage.getScene().setRoot();
            }
        });
        Button b2 = new Button("Back");
        b2.setFont(new Font(20));
        b2.setPrefSize(200,40);
        b2.setOnAction(event -> {
            stage.getScene().setRoot(this.lobbySelectionPage(stage));
        });

        //back button
        HBox h1 = new HBox(b2);
        h1.setAlignment(Pos.TOP_LEFT);
        h1.setPadding(new Insets(20,0,0,20));

        //username and color
        HBox h2 = new HBox(l1,text);
        h2.setAlignment(Pos.CENTER);
        HBox h3 = new HBox(l2,cb);
        h3.setAlignment(Pos.CENTER);
        VBox v1 = new VBox(h2 ,h3);
        v1.setAlignment(Pos.CENTER);
        v1.setSpacing(30);

        //next button
        HBox h4 = new HBox(b1);
        h4.setAlignment(Pos.BOTTOM_RIGHT);
        h4.setPadding(new Insets(0,20,20,0));

        BorderPane bp = new BorderPane();
        bp.setTop(h1);
        bp.setCenter(v1);
        bp.setBottom(h4);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/manuscript_wallpaper.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane lobbySelectionPage(Stage stage) {
        Button b1 = new Button("Next");
        b1.setFont(new Font(20));
        b1.setPrefSize(200,40);
        b1.setOnAction(event -> {
            stage.getScene().setRoot(this.loginPage(stage));
        });
        HBox h1 = new HBox(b1);
        h1.setAlignment(Pos.TOP_LEFT);
        h1.setPadding(new Insets(20,0,0,20));

        Label l1 = new Label("Lobbies: ");
        l1.setFont(new Font(30));
        ArrayList<Integer> lobbies = new ArrayList<>(List.of(0, 1));
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.add(l1, 0  ,0 );
        for(int i = 0; i < lobbies.size(); i++){
            Label l2 = new Label("Lobby " + String.valueOf(i + 1));
            l2.setFont(new Font(30));
            gp.add(l2, 1, i);
            Label l3  = new Label("Players: " + "numero di players della lobby " + String.valueOf(i + 1));
            l3.setFont(new Font(30));
            gp.add(l3, 2, i);
            Button b = new Button("Join Lobby " + String.valueOf(i + 1));
            b.setFont(new Font(20));
            b.setPrefSize(150, 30);
            gp.add(b, 3, i);
        }
        gp.setHgap(30);
        gp.setVgap(30);

        Button b2 = new Button("Create a new lobby");
        b2.setFont(new Font(30));
        b2.setPrefSize(350, 50);
        HBox h2 = new HBox(b2);
        h2.setAlignment(Pos.BOTTOM_CENTER);
        h2.setPadding(new Insets(0,0,100,0));

        BorderPane bp = new BorderPane();
        bp.setTop(h1);
        bp.setCenter(gp);
        bp.setBottom(h2);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/manuscript_wallpaper.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

}
