package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        //b1.setStyle(
        //        "-fx-border-radius: 4;" +
        //        "-fx-background-color: gold;" +
        //        "-fx-border-width: 3;" +
        //        "-fx-border-color: black;" +
        //        "-fx-background-radius: 4;" +
        //);
        b1.setOnMouseEntered(event -> b1.setStyle("-fx-background-color: gold"));
        b1.setOnMouseExited(event -> b1.setStyle(""));
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
        Scene scene = new Scene(bp, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    public Pane lobbySelectionPage(Stage stage) {

        Text t1 = new Text("Lobbies:");
        t1.setFont(new Font(65));
        t1.setFill(javafx.scene.paint.Color.DARKGREEN);
        t1.setStrokeWidth(1.8);
        t1.setStroke(javafx.scene.paint.Color.BLACK);
        HBox h1 = new HBox(t1);
        h1.setAlignment(Pos.CENTER);
        h1.setPadding(new Insets(100, 0, 0, 0));

        ArrayList<Integer> lobbies = new ArrayList<>(List.of(0, 1));
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        ArrayList<Button> buttonList = new ArrayList<>();
        for(int i = 0; i < lobbies.size(); i++){
            buttonList.add(new Button("Join lobby " + String.valueOf(i + 1)));
        }
        for(int i = 0; i < lobbies.size(); i++){
            Text t2 = new Text("Lobby " + String.valueOf(i + 1));
            t2.setFont(new Font(55));
            t2.setFill(javafx.scene.paint.Color.WHITE);
            t2.setStrokeWidth(1.7);
            t2.setStroke(javafx.scene.paint.Color.BLACK);
            gp.add(t2, 0, i);
            //qua vanno messi il numero di player di ogni lobby
            Text t3 = new Text("Players: 2/4");
            t3.setFont(new Font(55));
            t3.setFill(javafx.scene.paint.Color.WHITE);
            t3.setStrokeWidth(1.7);
            t3.setStroke(javafx.scene.paint.Color.BLACK);
            gp.add(t3, 1, i);
            buttonList.get(i).setFont(new Font(30));
            buttonList.get(i).setPrefSize(230, 30);
            int finalI = i;
            buttonList.get(i).setOnAction(event -> {
                //aggiungere il player alla lobby i-esima
                stage.getScene().setRoot(this.loginPage(stage, finalI));
            });
            gp.add(buttonList.get(i), 2, i);
        }
        gp.setHgap(30);
        gp.setVgap(30);

        Button b2 = new Button("Create a new lobby");
        b2.setFont(new Font(35));
        b2.setPrefSize(380, 50);
        b2.setOnAction(event -> {
            stage.getScene().setRoot(this.loginPage(stage, lobbies.size()));
        });
        HBox h2 = new HBox(b2);
        h2.setAlignment(Pos.BOTTOM_CENTER);
        h2.setPadding(new Insets(0,0,100,0));

        BorderPane bp = new BorderPane();
        bp.setTop(h1);
        bp.setCenter(gp);
        bp.setBottom(h2);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/forest_background.jpeg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane loginPage(Stage stage, int selectedLobby) {

        Text t1 = new Text("Username:  ");
        t1.setFont(new Font(60));
        t1.setFill(javafx.scene.paint.Color.WHITE);
        t1.setStrokeWidth(1.8);
        t1.setStroke(javafx.scene.paint.Color.BLACK);
        Text t2 = new Text("Color:  ");
        t2.setFont(new Font(60));
        t2.setFill(javafx.scene.paint.Color.WHITE);
        t2.setStrokeWidth(1.8);
        t2.setStroke(javafx.scene.paint.Color.BLACK);
        TextField text = new TextField();
        text.setPrefSize(350, 75);
        text.setFont(new Font(30));
        ChoiceBox<String> cb = new ChoiceBox<>();
        //aggiungere solo i colori che non sono già stati scelti
        cb.getItems().add("red");
        cb.getItems().add("green");
        cb.getItems().add("blue");
        cb.getItems().add("yellow");
        cb.setStyle("-fx-font-size: 30");
        cb.getSelectionModel().selectFirst();
        cb.setPrefSize(250,75);
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
                stage.getScene().setRoot(this.waitingPage(stage, selectedLobby));
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
        HBox h2 = new HBox(t1,text);
        h2.setAlignment(Pos.CENTER);
        HBox h3 = new HBox(t2,cb);
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
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/forest_background.jpeg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane waitingPage(Stage stage, int lobby){

        Button b2 = new Button("Back");
        b2.setFont(new Font(20));
        b2.setPrefSize(200,40);
        b2.setOnAction(event -> {
            stage.getScene().setRoot(this.loginPage(stage, lobby));
        });

        //back button
        HBox h1 = new HBox(b2);
        h1.setAlignment(Pos.TOP_LEFT);
        h1.setPadding(new Insets(20,0,0,20));

        Text t1 = new Text("Lobby " + String.valueOf(lobby + 1));
        t1.setFont(new Font(60));
        t1.setFill(javafx.scene.paint.Color.WHITE);
        t1.setStrokeWidth(1.8);
        t1.setStroke(javafx.scene.paint.Color.BLACK);
        Text t2 = new Text("Players in lobby: " + "numero di player della lobby " + String.valueOf(lobby + 1));
        t2.setFont(new Font(30));
        t2.setFill(javafx.scene.paint.Color.WHITE);
        t2.setStrokeWidth(1.5);
        t2.setStroke(javafx.scene.paint.Color.BLACK);
        Text t3 = new Text("Waiting for other players...");
        t3.setFont(new Font(30));
        t3.setFill(javafx.scene.paint.Color.WHITE);
        t3.setStrokeWidth(1.5);
        t3.setStroke(javafx.scene.paint.Color.BLACK);

        VBox v1 = new VBox(t1, t2, t3);
        v1.setAlignment(Pos.CENTER);
        v1.setSpacing(30);

        BorderPane bp = new BorderPane();
        bp.setTop(h1);
        bp.setCenter(v1);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/forest_background.jpeg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));

        Timeline t = new Timeline(new KeyFrame(Duration.seconds(5), event2 -> {
            StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
            stage.getScene().setRoot(this.starterFlip(stage, s, true));
        }));
        t.setCycleCount(1);
        t.play();

        return bp;
    }

    public Pane starterFlip(Stage stage, Card starter, boolean side){

        BorderPane bp = new BorderPane();

        Text t1 = new Text("Your starter card:");
        t1.setFont(new Font(65));
        t1.setFill(javafx.scene.paint.Color.WHITE);
        t1.setStrokeWidth(1.8);
        t1.setStroke(javafx.scene.paint.Color.BLACK);
        Text t2 = new Text("Click the card to flip");
        t2.setFont(new Font(50));
        t2.setFill(javafx.scene.paint.Color.ORANGE);
        t2.setStrokeWidth(1.8);
        t2.setStroke(javafx.scene.paint.Color.BLACK);
        VBox v1 = new VBox(t1, t2);
        v1.setAlignment(Pos.TOP_CENTER);
        v1.setPadding(new Insets(100,0,0,0));

        int number = starter.getCardNumber() + 80;
        Image front = new Image(getClass().getResourceAsStream("/cards/fronts/0" + String.valueOf(number) + ".png"));
        ImageView frontView = new ImageView(front);
        frontView.setFitHeight(42*6);
        frontView.setFitWidth(63*6);
        Image back = new Image(getClass().getResourceAsStream("/cards/backs/0" + String.valueOf(number) + ".png"));
        ImageView backView = new ImageView(back);
        backView.setFitHeight(42*6);
        backView.setFitWidth(63*6);
        ImageView miniFrontView = new ImageView(front);
        miniFrontView.setFitHeight(42*3);
        miniFrontView.setFitWidth(63*3);
        ImageView miniBackView = new ImageView(back);
        miniBackView.setFitHeight(42*3);
        miniBackView.setFitWidth(63*3);
        Button b1 = new Button();
        b1.setPrefSize((63*6), (42*6));
        b1.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent"
        );
        b1.setOnAction(event -> {
            if(side){
                stage.getScene().setRoot(this.starterFlip(stage, starter, false));
            }
            else{
                stage.getScene().setRoot(this.starterFlip(stage, starter, true));
            }
        });

        Button b2 = new Button("Place starter card");
        b2.setFont(new Font(20));
        b2.setPrefSize(200,40);
        b2.setOnAction(event -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType yes = new ButtonType("Confirm");
            ButtonType no = new ButtonType("Cancel");
            a.setTitle("Placing starter card");
            a.setHeaderText("Do you want to place the card on this side?");
            if(side){
                a.setGraphic(miniFrontView);
            }
            else{
                a.setGraphic(miniBackView);
            }
            a.getButtonTypes().clear();
            a.getButtonTypes().addAll(no, yes);
            Optional<ButtonType> result = a.showAndWait();
            if(result.isEmpty()){
                stage.getScene().setRoot(this.starterFlip(stage, starter,true));
            }
            else if(result.get().getText().equals("Confirm")){
                stage.getScene().setRoot(this.achievementChoice(stage));
            }
            else if(result.get().getText().equals("Cancel")){
                stage.getScene().setRoot(this.starterFlip(stage, starter, side));
            }
        });
        HBox h1 = new HBox(b2);
        h1.setAlignment(Pos.BOTTOM_RIGHT);
        h1.setPadding(new Insets(0,20,20,0));

        bp.setTop(v1);
        if(side){
            b1.setGraphic(frontView);
        }
        else{
            b1.setGraphic(backView);
        }
        bp.setCenter(b1);
        bp.setBottom(h1);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/wood_background.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane achievementChoice(Stage stage){

        BorderPane bp = new BorderPane();

        Text t1 = new Text("Choose your achievement:");
        t1.setFont(new Font(65));
        t1.setFill(javafx.scene.paint.Color.WHITE);
        t1.setStrokeWidth(1.8);
        t1.setStroke(javafx.scene.paint.Color.BLACK);
        Text t2 = new Text("Click the card to choose");
        t2.setFont(new Font(50));
        t2.setFill(javafx.scene.paint.Color.ORANGE);
        t2.setStrokeWidth(1.8);
        t2.setStroke(javafx.scene.paint.Color.BLACK);
        VBox v1 = new VBox(t1, t2);
        v1.setAlignment(Pos.TOP_CENTER);
        v1.setPadding(new Insets(100,0,0,0));

        //int number1 = achievements[0].getCardNumber();
        //int number2 = achievements[1].getCardNumber();
        Image front1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + "095" + ".png"));
        ImageView frontView1 = new ImageView(front1);
        frontView1.setFitHeight(42*6);
        frontView1.setFitWidth(63*6);
        ImageView miniFrontView1 = new ImageView(front1);
        miniFrontView1.setFitHeight(42*3);
        miniFrontView1.setFitWidth(63*3);
        Button b1 = new Button();
        b1.setPrefSize((63*6), (42*6));
        b1.setGraphic(frontView1);
        b1.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent"
        );
        b1.setOnAction(event -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType yes = new ButtonType("Confirm");
            ButtonType no = new ButtonType("Cancel");
            a.setTitle("Choosing achievement card");
            a.setHeaderText("Do you want to choose this achievement?");
            a.setGraphic(miniFrontView1);
            a.getButtonTypes().clear();
            a.getButtonTypes().addAll(no, yes);
            Optional<ButtonType> result = a.showAndWait();
            if(result.isEmpty()){
                stage.getScene().setRoot(this.achievementChoice(stage));
            }
            else if(result.get().getText().equals("Confirm")){
                stage.getScene().setRoot(this.loginPage(stage, 3));
            }
            else if(result.get().getText().equals("Cancel")){
                stage.getScene().setRoot(this.achievementChoice(stage));
            }
        });

        Image front2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + "100" + ".png"));
        ImageView frontView2 = new ImageView(front2);
        frontView2.setFitHeight(42*6);
        frontView2.setFitWidth(63*6);
        ImageView miniFrontView2 = new ImageView(front2);
        miniFrontView2.setFitHeight(42*3);
        miniFrontView2.setFitWidth(63*3);
        Button b2 = new Button("Place starter card");
        b2.setPrefSize((63*6), (42*6));
        b2.setGraphic(frontView2);
        b2.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent"
        );
        b2.setOnAction(event -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType yes = new ButtonType("Confirm");
            ButtonType no = new ButtonType("Cancel");
            a.setTitle("Choosing achievement card");
            a.setHeaderText("Do you want to choose this achievement?");
            a.setGraphic(miniFrontView2);
            a.getButtonTypes().clear();
            a.getButtonTypes().addAll(no, yes);
            Optional<ButtonType> result = a.showAndWait();
            if(result.isEmpty()){
                stage.getScene().setRoot(this.achievementChoice(stage));
            }
            else if(result.get().getText().equals("Confirm")){
                stage.getScene().setRoot(this.loginPage(stage, 3));
            }
            else if(result.get().getText().equals("Cancel")){
                stage.getScene().setRoot(this.achievementChoice(stage));
            }
        });
        HBox h1 = new HBox(b1, b2);
        h1.setAlignment(Pos.CENTER);
        h1.setSpacing(50);

        bp.setTop(v1);
        bp.setCenter(h1);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/wood_background.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane colorSelectionPage(Stage stage){
        int[] freeColors = {1, 1, 1, 1};
        Button[] buttons = new Button[4];
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setStyle("-fx-shape: circle");
            buttons[i].setPrefSize(40, 40);
        }

        ArrayList<Button> buttonList = new ArrayList<>();
        for(int i = 0; i < freeColors.length; i++){
            if(freeColors[i] == 1){
                buttonList.add(buttons[i]);
            }
        }

        HBox h1 = new HBox();

        BorderPane bp = new BorderPane();
        return bp;
    }


}
