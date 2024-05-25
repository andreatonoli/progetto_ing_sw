package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardBack;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Symbols;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Gui extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {


        StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 1, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));

        stage.setTitle("Codex Naturalis");
        Button b1 = new Button("Play!");
        //b1.getStylesheets().add(getClass().getResource("/ButtonStyle.css").toString());
        b1.setId("play-button");
        //b1.getStyleClass().add("play-button");
        //b1.setFont(new Font(40));
        //b1.setPrefSize(150, 70);
        b1.setOnAction(event -> {
            //stage.getScene().setRoot(this.lobbySelectionPage(stage));
            stage.getScene().setRoot(this.starterFlip(stage, s, true));
        });
        HBox h1 = new HBox(b1);
        h1.setAlignment(Pos.CENTER);
        h1.setPadding(new Insets(500, 0, 0, 0));
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/background.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        BorderPane bp = new BorderPane();
        bp.setCenter(h1);
        bp.setBackground(new Background(i));
        Scene scene = new Scene(bp, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/ButtonStyle.css").toString());
        stage.setScene(scene);
        stage.show();
    }

    public Pane lobbySelectionPage(Stage stage) {

        Label l1 = new Label("Lobbies: ");
        l1.setFont(new Font(30));
        ArrayList<Integer> lobbies = new ArrayList<>(List.of(0, 1));
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.add(l1, 0  ,0 );
        ArrayList<Button> buttonList = new ArrayList<>();
        for(int i = 0; i < lobbies.size(); i++){
            buttonList.add(new Button("Join Lobby " + String.valueOf(i + 1)));
        }
        for(int i = 0; i < lobbies.size(); i++){
            Label l2 = new Label("Lobby " + String.valueOf(i + 1));
            l2.setFont(new Font(30));
            gp.add(l2, 1, i);
            Label l3  = new Label("Players: " + "numero di players della lobby " + String.valueOf(i + 1));
            l3.setFont(new Font(30));
            gp.add(l3, 2, i);
            buttonList.get(i).setFont(new Font(20));
            buttonList.get(i).setPrefSize(150, 30);
            int finalI = i;
            buttonList.get(i).setOnAction(event -> {
                //aggiungere il player alla lobby i-esima
                stage.getScene().setRoot(this.loginPage(stage, finalI));
            });
            gp.add(buttonList.get(i), 3, i);
        }
        gp.setHgap(30);
        gp.setVgap(30);

        Button b2 = new Button("Create a new lobby");
        b2.setFont(new Font(30));
        b2.setPrefSize(350, 50);
        b2.setOnAction(event -> {
            stage.getScene().setRoot(this.loginPage(stage, lobbies.size()));
        });
        HBox h2 = new HBox(b2);
        h2.setAlignment(Pos.BOTTOM_CENTER);
        h2.setPadding(new Insets(0,0,100,0));

        BorderPane bp = new BorderPane();
        bp.setCenter(gp);
        bp.setBottom(h2);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/manuscript_wallpaper.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane loginPage(Stage stage, int selectedLobby) {

        Text t1 = new Text("Username :  ");
        t1.setFont(new Font(45));
        t1.setFill(javafx.scene.paint.Color.WHITE);
        t1.setStrokeWidth(1.5);
        t1.setStroke(javafx.scene.paint.Color.BLACK);
        TextField text = new TextField();
        text.setPrefSize(200,50);
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

        Label l1 = new Label("Lobby " + String.valueOf(lobby + 1));
        l1.setFont(new Font(45));
        Label l2 = new Label("Players in lobby: " + "numero di player della lobby " + String.valueOf(lobby + 1));
        l2.setFont(new Font(30));
        Label l3 = new Label("Waiting for other players...");
        l3.setFont(new Font(30));

        VBox v1 = new VBox(l1, l2, l3);
        v1.setAlignment(Pos.CENTER);
        v1.setSpacing(30);

        BorderPane bp = new BorderPane();
        bp.setTop(h1);
        bp.setCenter(v1);
        BackgroundImage i = new BackgroundImage(new Image(getClass().getResourceAsStream("/manuscript_wallpaper.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        bp.setBackground(new Background(i));
        return bp;
    }

    public Pane starterFlip(Stage stage, Card starter, boolean side){

        BorderPane bp = new BorderPane();

        Label l1 = new Label("Your starter card:");
        l1.setFont(new Font(45));
        Label l2 = new Label("Click the card to flip");
        l2.setFont(new Font(35));
        VBox v1 = new VBox(l1, l2);
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
            a.getButtonTypes().clear();
            a.getButtonTypes().addAll(no, yes);
            Optional<ButtonType> result = a.showAndWait();
            if(result.isEmpty()){
                stage.getScene().setRoot(this.starterFlip(stage, starter,true));
            }
            else if(result.get().getText().equals("Confirm")){
                stage.getScene().setRoot(this.loginPage(stage, 3));
            }
            else if(result.get().getText().equals("Cancel")){
                stage.getScene().setRoot(this.starterFlip(stage, starter,true));
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

}
