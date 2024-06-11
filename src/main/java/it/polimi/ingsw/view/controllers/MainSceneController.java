package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Optional;

public class MainSceneController {

    @FXML
    private Button hand1;
    @FXML
    private Button hand2;
    @FXML
    private Button hand3;
    @FXML
    private ImageView personalAch;
    @FXML
    private Button resourceDeck;
    @FXML
    private Button resource1;
    @FXML
    private Button resource2;
    @FXML
    private Button goldDeck;
    @FXML
    private Button gold1;
    @FXML
    private Button gold2;
    @FXML
    private ImageView achievement1;
    @FXML
    private ImageView achievement2;
    @FXML
    private AnchorPane board;
    @FXML
    private GridPane buttonBoard;
    @FXML
    private AnchorPane scoreBoard;

    //this matrix has the x and y position of the zero point for alle the 4 pions
    private int[][] scoretrackZero = {new int[] {65, 115, 65, 115}, new int[] {670, 670, 715, 715}};
    //this matrix has the x and y values of the first pion based on points
    private int[][] scoretrackFirstPion = {new int[]{65, 155, 245, 290, 200, 110, 20, 20, 110, 200, 290, 290, 200, 110, 20, 20, 110, 200, 290, 290, 135, 40, 40, 40, 95, 175, 260, 310, 310, 175}, new int[]{670, 670, 670, 590, 590, 590, 590, 510, 510, 510, 505, 425, 425, 425, 425, 345, 345, 345, 345, 260, 220, 265, 180, 100, 20, 5, 20, 90, 170, 105}};

    //{new int[]{0, 90, 90, 45, -90, -90, -90, 0, 90, 90, 90, 0, -90, -90, -90, 0, 90, 90, 90, 0, -155, -95, 0, 0, 55, 80, 85, 50, 0, -135}, new int[]{0, 0, 0, -80, 0, 0, 0, -80, 0, 0, -5, -80, 0, 0, 0, -80, 0, 0, 0, -85, -40, 45, -85, -80, -80, -15, 15, 70, 80, -65}};

    private int cardToPlace;

    public void setScene(PlayerBean player, GameBean game, ArrayList<PlayerBean> players){
        //hand
        int h1 = player.getHand()[0].getCardNumber();
        int h2 = player.getHand()[1].getCardNumber();
        int h3 = player.getHand()[2].getCardNumber();

        Image imageHand1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h1) + ".png"));
        ImageView viewHand1 = new ImageView(imageHand1);
        viewHand1.setFitHeight(42*3);
        viewHand1.setFitWidth(63*3);
        Image imageHand2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h2) + ".png"));
        ImageView viewHand2 = new ImageView(imageHand2);
        viewHand2.setFitHeight(42*3);
        viewHand2.setFitWidth(63*3);
        Image imageHand3 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h3) + ".png"));
        ImageView viewHand3 = new ImageView(imageHand3);
        viewHand3.setFitHeight(42*3);
        viewHand3.setFitWidth(63*3);
        hand1.setGraphic(viewHand1);
        hand2.setGraphic(viewHand2);
        hand3.setGraphic(viewHand3);

        //personal achievement
        int pa = player.getAchievement().getId();

        Image imagePersonalAch = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(pa) + ".png"));
        personalAch = new ImageView(imagePersonalAch);
        personalAch.setFitHeight(42*3);
        personalAch.setFitWidth(63*3);

        //common board
        Color rDeck = game.getResourceDeckRetro();
        Image imageFungiRetro = new Image(getClass().getResourceAsStream("/cards/backs/1.png"));
        ImageView viewFungiRetro = new ImageView(imageFungiRetro);
        viewFungiRetro.setFitHeight(42*2.5);
        viewFungiRetro.setFitWidth(64*2.5);
        Image imageAnimalRetro = new Image(getClass().getResourceAsStream("/cards/backs/21.png"));
        ImageView viewAnimalRetro = new ImageView(imageAnimalRetro);
        viewAnimalRetro.setFitHeight(42*2.5);
        viewAnimalRetro.setFitWidth(64*2.5);
        Image imagePlantRetro = new Image(getClass().getResourceAsStream("/cards/backs/11.png"));
        ImageView viewPlantRetro = new ImageView(imagePlantRetro);
        viewPlantRetro.setFitHeight(42*2.5);
        viewPlantRetro.setFitWidth(64*2.5);
        Image imageInsectRetro = new Image(getClass().getResourceAsStream("/cards/backs/31.png"));
        ImageView viewInsectRetro = new ImageView(imageInsectRetro);
        viewInsectRetro.setFitHeight(42*2.5);
        viewInsectRetro.setFitWidth(64*2.5);
        switch (rDeck){
            case RED -> resourceDeck.setGraphic(viewFungiRetro);
            case BLUE -> resourceDeck.setGraphic(viewAnimalRetro);
            case GREEN -> resourceDeck.setGraphic(viewPlantRetro);
            case PURPLE -> resourceDeck.setGraphic(viewInsectRetro);
        }

        int r1 = game.getCommonResources()[0].getCardNumber();
        int r2 = game.getCommonResources()[1].getCardNumber();
        Image imageResource1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(r1) + ".png"));
        ImageView viewResource1 = new ImageView(imageResource1);
        viewResource1.setFitHeight(42*2.5);
        viewResource1.setFitWidth(64*2.5);
        Image imageResource2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(r2) + ".png"));
        ImageView viewResource2 = new ImageView(imageResource2);
        viewResource2.setFitHeight(42*2.5);
        viewResource2.setFitWidth(64*2.5);
        resource1.setGraphic(viewResource1);
        resource2.setGraphic(viewResource2);

        Color gDeck = game.getGoldDeckRetro();
        Image imageFungiGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/41.png"));
        ImageView viewFungiGoldRetro = new ImageView(imageFungiGoldRetro);
        viewFungiGoldRetro.setFitHeight(42*2.5);
        viewFungiGoldRetro.setFitWidth(64*2.5);
        Image imageAnimalGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/66.png"));
        ImageView viewAnimalGoldRetro = new ImageView(imageAnimalGoldRetro);
        viewAnimalGoldRetro.setFitHeight(42*2.5);
        viewAnimalGoldRetro.setFitWidth(64*2.5);
        Image imagePlantGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/51.png"));
        ImageView viewPlantGoldRetro = new ImageView(imagePlantGoldRetro);
        viewPlantGoldRetro.setFitHeight(42*2.5);
        viewPlantGoldRetro.setFitWidth(64*2.5);
        Image imageInsectGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/73.png"));
        ImageView viewInsectGoldRetro = new ImageView(imageInsectGoldRetro);
        viewInsectGoldRetro.setFitHeight(42*2.5);
        viewInsectGoldRetro.setFitWidth(64*2.5);
        switch (gDeck){
            case RED -> resourceDeck.setGraphic(viewFungiGoldRetro);
            case BLUE -> resourceDeck.setGraphic(viewAnimalGoldRetro);
            case GREEN -> resourceDeck.setGraphic(viewPlantGoldRetro);
            case PURPLE -> resourceDeck.setGraphic(viewInsectGoldRetro);
        }

        int g1 = game.getCommonGold()[0].getCardNumber();
        int g2 = game.getCommonGold()[1].getCardNumber();
        Image imageGold1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(g1) + ".png"));
        ImageView viewGold1 = new ImageView(imageGold1);
        viewGold1.setFitHeight(42*2.5);
        viewGold1.setFitWidth(64*2.5);
        Image imageGold2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(g2) + ".png"));
        ImageView viewGold2 = new ImageView(imageGold2);
        viewGold2.setFitHeight(42*2.5);
        viewGold2.setFitWidth(64*2.5);
        gold1.setGraphic(viewGold1);
        gold2.setGraphic(viewGold2);

        int a1 = game.getCommonAchievement()[0].getId();
        int a2 = game.getCommonAchievement()[1].getId();
        Image imageAchievement1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(a1) + ".png"));
        achievement1 = new ImageView(imageAchievement1);
        Image imageAchievement2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(a2) + ".png"));
        achievement2 = new ImageView(imageAchievement2);

        //player board
        for (Integer i : player.getBoard().getPositionCardKeys()){
            int[] coord = new int[2];
            coord[0] = (i / 1024) - PlayerBoard.OFFSET;
            coord[1] = (i % 1024) - PlayerBoard.OFFSET;
            int card = player.getBoard().getCard(coord).getCardNumber();
            Image imageCard = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(card) + ".png"));
            ImageView viewCard = new ImageView(imageCard);
            viewCard.setFitHeight(42*4);
            viewCard.setFitWidth(63*4);
            viewCard.setLayoutX(1013 + coord[1]*150);
            viewCard.setLayoutY(532 + coord[0]*77);
            board.getChildren().add(viewCard);
            int[][] buttonCornerCenter = {new int[]{6, 7, 7, 7}, new int[]{6, 6, 6, 7}};
            for (CornerEnum c : CornerEnum.values()){
                if (player.getBoard().getCard(coord).getCorner(c).getState().equals(CornerState.VISIBLE)){
                    Button b = new Button();
                    b.setPrefSize(58, 52);
                    b.setStyle("-fx-background-color: transparent");
                    b.setOnAction(event -> {
                        placeCard(cardToPlace, coord, new int[]{c.getX(), c.getY()});
                    });
                    buttonBoard.add(b, (buttonCornerCenter[c.getX()][0] + coord[0]), (buttonCornerCenter[c.getY()][1] + coord[1]));
                    b.setVisible(false);
                }
            }
        }

        //score track
        Image imageRed = new Image(getClass().getResourceAsStream("/images/red.png"));
        ImageView viewRed = new ImageView(imageRed);
        viewRed.setFitHeight(40);
        viewRed.setFitWidth(40);
        viewRed.setLayoutX(scoretrackZero[0][0]);
        viewRed.setLayoutY(scoretrackZero[0][1]);
        Image imageGreen = new Image(getClass().getResourceAsStream("/images/green.png"));
        ImageView viewGreen = new ImageView(imageGreen);
        viewGreen.setFitHeight(40);
        viewGreen.setFitWidth(40);
        viewGreen.setLayoutX(scoretrackZero[1][0]);
        viewGreen.setLayoutY(scoretrackZero[1][1]);
        Image imageBlue = new Image(getClass().getResourceAsStream("/images/blue.png"));
        ImageView viewBlue = new ImageView(imageBlue);
        viewBlue.setFitHeight(40);
        viewBlue.setFitWidth(40);
        viewBlue.setLayoutX(scoretrackZero[2][0]);
        viewBlue.setLayoutY(scoretrackZero[2][1]);
        Image imageYellow = new Image(getClass().getResourceAsStream("/images/yellow.png"));
        ImageView viewYellow = new ImageView(imageYellow);
        viewYellow.setFitHeight(40);
        viewYellow.setFitWidth(40);
        viewYellow.setLayoutX(scoretrackZero[3][0]);
        viewYellow.setLayoutY(scoretrackZero[3][1]);
        ImageView[] pions = {viewRed, viewGreen, viewBlue, viewYellow};
        for (int i = 0; i < players.size(); i++){
            if(players.get(i).getPoints() == 0){
                scoreBoard.getChildren().add(pions[i]);
            }
            else{
                pions[i].setLayoutX(scoretrackFirstPion[players.get(i).getPoints()][0] + (scoretrackZero[0][0] - scoretrackZero[i][0]));
                pions[i].setLayoutX(scoretrackFirstPion[players.get(i).getPoints()][1] + (scoretrackZero[0][1] - scoretrackZero[i][1]));
            }
        }

    }

    @FXML
    public void drawResourceDeck(ActionEvent e) {
        Gui.addReturnValue("RDeck");
    }

    @FXML
    public void drawResource1(ActionEvent e) {
        Gui.addReturnValue("R1");
    }

    @FXML
    public void drawResource2(ActionEvent e) {
        Gui.addReturnValue("R2");
    }

    @FXML
    public void drawGoldDeck(ActionEvent e) {
        Gui.addReturnValue("GDeck");
    }

    @FXML
    public void drawGold1(ActionEvent e) {
        Gui.addReturnValue("G1");
    }

    @FXML
    public void drawGold2(ActionEvent e) {
        Gui.addReturnValue("G2");
    }

    @FXML
    public void wantToPlaceHand1(ActionEvent e) {
        cardToPlace = 1;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    @FXML
    public void wantToPlaceHand2(ActionEvent e) {
        cardToPlace = 2;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    @FXML
    public void wantToPlaceHand3(ActionEvent e) {
        cardToPlace = 3;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }
    
    @FXML void placeCard(int cardToPlace, int[] cardToCover, int[] cornerToCover){
        Gui.addReturnValue("hand" + cardToPlace + "OnTopOf" + cardToCover[0] + cardToCover[1] + "CardOnCorner" + cornerToCover[0] + cardToCover[1]);
    }

}
