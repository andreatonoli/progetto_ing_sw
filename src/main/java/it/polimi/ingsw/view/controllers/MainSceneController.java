package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.StarterCard;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

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
    @FXML
    private AnchorPane messages;
    @FXML
    private ChoiceBox receiver;
    @FXML
    private TextField textMessage;
    @FXML
    private ChoiceBox otherPlayers;

    //this matrix has the x and y position of the zero point for alle the 4 pions
    private int[][] scoretrackZero = {new int[] {65, 115, 65, 115}, new int[] {670, 670, 715, 715}};
    //this matrix has the x and y values of the first pion based on points
    private int[][] scoretrackFirstPion = {new int[]{65, 155, 245, 290, 200, 110, 20, 20, 110, 200, 290, 290, 200, 110, 20, 20, 110, 200, 290, 290, 135, 40, 40, 40, 95, 175, 260, 310, 310, 175}, new int[]{670, 670, 670, 590, 590, 590, 590, 510, 510, 510, 505, 425, 425, 425, 425, 345, 345, 345, 345, 260, 220, 265, 180, 100, 20, 5, 20, 90, 170, 105}};

    private int cardToPlace;

    private ArrayList<ImageView> pions = new ArrayList<>();

    int h1;
    int h2;
    int h3;

    public void setBoard(PlayerBean player, GameBean game, ArrayList<PlayerBean> opponents){
        //hand
        //sostituire con for
        if(player.getHand()[0] != null){
            h1 = player.getHand()[0].getCardNumber();
            if(player.getHand()[0].getType().equals("gold")){
                h1 = h1 + 40;
            }
            Image imageHand1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h1) + ".png"));
            ImageView viewHand1 = new ImageView(imageHand1);
            viewHand1.setFitHeight(42*3);
            viewHand1.setFitWidth(63*3);
            hand1.setGraphic(viewHand1);
        }
        if(player.getHand()[1] != null){
            h2 = player.getHand()[1].getCardNumber();
            if(player.getHand()[1].getType().equals("gold")){
                h2 = h2 + 40;
            }
            Image imageHand2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h2) + ".png"));
            ImageView viewHand2 = new ImageView(imageHand2);
            viewHand2.setFitHeight(42*3);
            viewHand2.setFitWidth(63*3);
            hand2.setGraphic(viewHand2);
        }
        if(player.getHand()[2] != null){
            h3 = player.getHand()[2].getCardNumber();
            if(player.getHand()[2].getType().equals("gold")){
                h3 = h3 + 40;
            }
            Image imageHand3 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h3) + ".png"));
            ImageView viewHand3 = new ImageView(imageHand3);
            viewHand3.setFitHeight(42*3);
            viewHand3.setFitWidth(63*3);
            hand3.setGraphic(viewHand3);
        }

        //personal achievement
        int pa = player.getAchievement().getId() + 86;
        Image imagePersonalAch = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(pa) + ".png"));
        personalAch.setImage(imagePersonalAch);

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
            case RED -> goldDeck.setGraphic(viewFungiGoldRetro);
            case BLUE -> goldDeck.setGraphic(viewAnimalGoldRetro);
            case GREEN -> goldDeck.setGraphic(viewPlantGoldRetro);
            case PURPLE -> goldDeck.setGraphic(viewInsectGoldRetro);
        }

        int g1 = game.getCommonGold()[0].getCardNumber() + 40;
        int g2 = game.getCommonGold()[1].getCardNumber() + 40;
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

        int a1 = game.getCommonAchievement()[0].getId() + 86;
        int a2 = game.getCommonAchievement()[1].getId() + 86;
        Image imageAchievement1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(a1) + ".png"));
        achievement1.setImage(imageAchievement1);
        Image imageAchievement2 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(a2) + ".png"));
        achievement2.setImage(imageAchievement2);

        //player board
        for (Integer i : player.getBoard().getPositionCardKeys()){
            int[] coord = new int[2];
            coord[0] = (i / 1024) - PlayerBoard.OFFSET;
            coord[1] = (i % 1024) - PlayerBoard.OFFSET;
            int card = player.getBoard().getCard(coord).getCardNumber();
            if(player.getBoard().getCard(coord).getType().equals("starter")){
                card = card + 80;
            }
            if(player.getBoard().getCard(coord).getType().equals("gold")){
                card = card + 40;
            }
            Image imageCard = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(card) + ".png"));
            ImageView viewCard = new ImageView(imageCard);
            viewCard.setFitHeight(42*4);
            viewCard.setFitWidth(63*4);
            viewCard.setLayoutX(1013 + coord[1]*150);
            viewCard.setLayoutY(532 + coord[0]*77);
            board.getChildren().add(viewCard);
            int[][] buttonCornerCenter = {new int[]{6, 6, 7, 7}, new int[]{6, 7, 7, 6}};
            int[][] cornerCenter = {new int[]{-1, 1, -1, 1}, new int[]{1, 1, -1, -1}};
            int j = 0;
            for (CornerEnum c : CornerEnum.values()){
                if (player.getBoard().getCard(coord).getCorner(c).getState().equals(CornerState.VISIBLE)){
                    Button b = new Button();
                    b.setPrefSize(58, 52);
                    //b.setStyle("-fx-background-color: transparent");
                    int finalJ = j;
                    b.setOnAction(event -> {
                        placeCard(cardToPlace, new int[]{cornerCenter[0][finalJ], cornerCenter[1][finalJ]});
                    });
                    buttonBoard.add(b, (buttonCornerCenter[0][j] + coord[0]), (buttonCornerCenter[1][j] + coord[1]));
                    //b.setVisible(false);
                }
                j++;
            }
        }

        //score track
        ArrayList<PlayerBean> allPlayers = new ArrayList<>(opponents);
        allPlayers.add(player);
        for(PlayerBean p : allPlayers){
            switch(p.getPionColor()){
                case RED -> {
                    Image imageRed = new Image(getClass().getResourceAsStream("/images/red.png"));
                    ImageView viewRed = new ImageView(imageRed);
                    viewRed.setFitHeight(40);
                    viewRed.setFitWidth(40);
                    scoreBoard.getChildren().add(viewRed);
                    viewRed.setLayoutX(scoretrackZero[0][0]);
                    viewRed.setLayoutY(scoretrackZero[1][0]);
                    pions.add(viewRed);
                }
                case BLUE -> {
                    Image imageBlue = new Image(getClass().getResourceAsStream("/images/blue.png"));
                    ImageView viewBlue = new ImageView(imageBlue);
                    scoreBoard.getChildren().add(viewBlue);
                    viewBlue.setFitHeight(40);
                    viewBlue.setFitWidth(40);
                    viewBlue.setLayoutX(scoretrackZero[0][2]);
                    viewBlue.setLayoutY(scoretrackZero[1][2]);
                    pions.add(viewBlue);
                }
                case GREEN -> {
                    Image imageGreen = new Image(getClass().getResourceAsStream("/images/green.png"));
                    ImageView viewGreen = new ImageView(imageGreen);
                    scoreBoard.getChildren().add(viewGreen);
                    viewGreen.setFitHeight(40);
                    viewGreen.setFitWidth(40);
                    viewGreen.setLayoutX(scoretrackZero[0][1]);
                    viewGreen.setLayoutY(scoretrackZero[1][1]);
                    pions.add(viewGreen);
                }
                case YELLOW -> {
                    Image imageYellow = new Image(getClass().getResourceAsStream("/images/yellow.png"));
                    ImageView viewYellow = new ImageView(imageYellow);
                    scoreBoard.getChildren().add(viewYellow);
                    viewYellow.setFitHeight(40);
                    viewYellow.setFitWidth(40);
                    viewYellow.setLayoutX(scoretrackZero[0][3]);
                    viewYellow.setLayoutY(scoretrackZero[1][3]);
                    pions.add(viewYellow);
                }
                case null, default -> {}
            }
        }
        for (int i = 0; i < pions.size(); i++){
            pions.get(i).setLayoutX(scoretrackFirstPion[0][allPlayers.get(i).getPoints()] + (scoretrackZero[0][0] - scoretrackZero[i][0]));
            pions.get(i).setLayoutY(scoretrackFirstPion[1][allPlayers.get(i).getPoints()] + (scoretrackZero[0][1] - scoretrackZero[i][1]));
        }

        //chat
        for(String s : player.getChat()){
            Text t = new Text(s);
            messages.getChildren().add(t);
        }
        receiver.getItems().add("global");
        for(PlayerBean p : opponents){
            if(!p.getUsername().equals(player.getUsername())){
                receiver.getItems().add(p.getUsername());
            }
        }
        receiver.getSelectionModel().selectFirst();

        //other players' playerboard
        for(PlayerBean p : opponents){
            if(!p.getUsername().equals(player.getUsername())){
                otherPlayers.getItems().add(p.getUsername());
            }
        }
        otherPlayers.getSelectionModel().selectFirst();


    }

    @FXML
    public void drawResourceDeck(ActionEvent e) {
        Gui.addReturnValue("2" + "§" + "resource");
    }

    @FXML
    public void drawResource1(ActionEvent e) {
        Gui.addReturnValue("3" + "§" + "1");
    }

    @FXML
    public void drawResource2(ActionEvent e) {
        Gui.addReturnValue("3" + "§" + "2");
    }

    @FXML
    public void drawGoldDeck(ActionEvent e) {
        Gui.addReturnValue("2" + "§" + "gold");
    }

    @FXML
    public void drawGold1(ActionEvent e) {
        Gui.addReturnValue("3" + "§" + "3");
    }

    @FXML
    public void drawGold2(ActionEvent e) {
        Gui.addReturnValue("3" + "§" + "4");
    }

    @FXML
    public void wantToPlaceHand1(ActionEvent e) {
        cardToPlace = 0;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    @FXML
    public void wantToPlaceHand2(ActionEvent e) {
        cardToPlace = 1;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    @FXML
    public void wantToPlaceHand3(ActionEvent e) {
        cardToPlace = 2;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    @FXML void placeCard(int cardToPlace, int[] newCardCoord){
        //"hand" + cardToPlace + "in" + newCardCoord
        Gui.addReturnValue("1" + "§" + String.valueOf(cardToPlace) + "§" + String.valueOf(newCardCoord[0]) + "§" + String.valueOf(newCardCoord[1]));
    }

    public void flipHand1(ActionEvent e) {
        Image imageRetroHand1 = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(h1) + ".png"));
        ImageView viewRetroHand1 = new ImageView(imageRetroHand1);
        viewRetroHand1.setFitHeight(42*3);
        viewRetroHand1.setFitWidth(63*3);
        hand1.setGraphic(viewRetroHand1);
    }

    public void flipHand2(ActionEvent e) {
        Image imageRetroHand2 = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(h2) + ".png"));
        ImageView viewRetroHand2 = new ImageView(imageRetroHand2);
        viewRetroHand2.setFitHeight(42*3);
        viewRetroHand2.setFitWidth(63*3);
        hand2.setGraphic(viewRetroHand2);
    }

    public void flipHand3(ActionEvent e) {
        Image imageRetroHand3 = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(h3) + ".png"));
        ImageView viewRetroHand3 = new ImageView(imageRetroHand3);
        viewRetroHand3.setFitHeight(42*3);
        viewRetroHand3.setFitWidth(63*3);
        hand1.setGraphic(viewRetroHand3);
    }

    public void sendMessage(ActionEvent e) {
        if(!textMessage.getText().isEmpty()){
            Gui.addReturnValue("4" + receiver.getSelectionModel().getSelectedItem() + "§" + textMessage.getText());
        }
    }

    public void viewPlayerboard(ActionEvent e) {
        Gui.addReturnValue("view" + otherPlayers.getSelectionModel().getSelectedItem());
    }
}
