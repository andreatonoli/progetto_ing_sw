package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MainSceneController extends GenericController{

    @FXML
    private Button hand1;
    @FXML
    private Button hand2;
    @FXML
    private Button hand3;
    @FXML
    private Button flip1;
    @FXML
    private Button flip2;
    @FXML
    private Button flip3;
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
    @FXML
    private Text playerState;
    @FXML
    private Button chat;
    @FXML
    private Button otherPlayersBoard;

    //this matrix has the x and y position of the zero point for alle the 4 pions
    private final int[][] scoretrackZero = {new int[] {65, 115, 65, 115}, new int[] {670, 670, 715, 715}};
    //this matrix has the x and y values of the first pion based on points
    private final int[][] scoretrackFirstPion = {new int[]{65, 155, 245, 290, 200, 110, 20, 20, 110, 200, 290, 290, 200, 110, 20, 20, 110, 200, 290, 290, 135, 40, 40, 40, 95, 175, 260, 310, 310, 175}, new int[]{670, 670, 670, 590, 590, 590, 590, 510, 510, 510, 505, 425, 425, 425, 425, 345, 345, 345, 345, 260, 220, 265, 180, 100, 20, 5, 20, 90, 170, 105}};

    private int cardToPlace;

    private final ArrayList<ImageView> pions = new ArrayList<>();

    private PlayerBean player;
    private GameBean game;
    private ArrayList<PlayerBean> opponents;

    private int h1;
    private int h2;
    private int h3;

    private ImageView viewHand1;
    private ImageView viewHand2;
    private ImageView viewHand3;

    private ImageView viewFungiRetro;
    private ImageView viewAnimalRetro;
    private ImageView viewPlantRetro;
    private ImageView viewInsectRetro;
    private ImageView viewFungiGoldRetro;
    private ImageView viewAnimalGoldRetro;
    private ImageView viewPlantGoldRetro;
    private ImageView viewInsectGoldRetro;

    private GuiInputHandler guiHandler;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        resourceDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.drawDeckButtonClicked("resource");
        });
        resource1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.drawCardButtonClicked("1");
        });
        resource2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.drawCardButtonClicked("2");
        });
        goldDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.drawDeckButtonClicked("gold");
        });
        gold1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.drawCardButtonClicked("3");
        });
        gold2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.drawCardButtonClicked("4");
        });
        flip1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(hand1.getGraphic().equals(viewHand1)){
                if(h1 > 0 && h1 < 11){
                    hand1.setGraphic(viewFungiRetro);
                }
                else if(h1 > 10 && h1 < 21){
                    hand1.setGraphic(viewPlantRetro);
                }
                else if(h1 > 20 && h1 < 31){
                    hand1.setGraphic(viewAnimalRetro);
                }
                else if(h1 > 30 && h1 < 41){
                    hand1.setGraphic(viewInsectRetro);
                }
                else if(h1 > 40 && h1 < 51){
                    hand1.setGraphic(viewFungiGoldRetro);
                }
                else if(h1 > 50 && h1 < 61){
                    hand1.setGraphic(viewPlantGoldRetro);
                }
                else if(h1 > 60 && h1 < 71){
                    hand1.setGraphic(viewAnimalGoldRetro);
                }
                else{
                    hand1.setGraphic(viewInsectGoldRetro);
                }
            }
            else{
                hand1.setGraphic(viewHand1);
            }
        });
        flip2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(hand2.getGraphic().equals(viewHand2)){
                if(h2 > 0 && h2 < 11){
                    hand2.setGraphic(viewFungiRetro);
                }
                else if(h2 > 10 && h2 < 21){
                    hand2.setGraphic(viewPlantRetro);
                }
                else if(h2 > 20 && h2 < 31){
                    hand2.setGraphic(viewAnimalRetro);
                }
                else if(h2 > 30 && h2 < 41){
                    hand2.setGraphic(viewInsectRetro);
                }
                else if(h2 > 40 && h2 < 51){
                    hand2.setGraphic(viewFungiGoldRetro);
                }
                else if(h2 > 50 && h2 < 61){
                    hand2.setGraphic(viewPlantGoldRetro);
                }
                else if(h2 > 60 && h2 < 71){
                    hand2.setGraphic(viewAnimalGoldRetro);
                }
                else{
                    hand2.setGraphic(viewInsectGoldRetro);
                }
            }
            else{
                hand2.setGraphic(viewHand2);
            }
        });
        flip3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(hand3.getGraphic().equals(viewHand3)){
                if(h3 > 0 && h3 < 11){
                    hand3.setGraphic(viewFungiRetro);
                }
                else if(h3 > 10 && h3 < 21){
                    hand3.setGraphic(viewPlantRetro);
                }
                else if(h3 > 20 && h3 < 31){
                    hand3.setGraphic(viewAnimalRetro);
                }
                else if(h3 > 30 && h3 < 41){
                    hand3.setGraphic(viewInsectRetro);
                }
                else if(h3 > 40 && h3 < 51){
                    hand3.setGraphic(viewFungiGoldRetro);
                }
                else if(h3 > 50 && h3 < 61){
                    hand3.setGraphic(viewPlantGoldRetro);
                }
                else if(h3 > 60 && h3 < 71){
                    hand3.setGraphic(viewAnimalGoldRetro);
                }
                else{
                    hand3.setGraphic(viewInsectGoldRetro);
                }
            }
            else{
                hand3.setGraphic(viewHand3);
            }
        });
        chat.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!textMessage.getText().isEmpty()){
                if(receiver.getSelectionModel().getSelectedItem().equals("global")){
                    guiHandler.sendGlobalMessageButtonClicked(textMessage.getText());
                }
                else{
                    guiHandler.sendMessageButtonClicked(textMessage.getText(), (String) receiver.getSelectionModel().getSelectedItem());
                }
            }
        });
        otherPlayersBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            for(PlayerBean p : opponents){
                if(p.getUsername().equals(otherPlayers.getSelectionModel().getSelectedItem())){
                    guiHandler.otherPlayersBoardButtonClicked(p, player, game, opponents);
                }
            }
        });

    }

    public void setBoard(PlayerBean player, GameBean game, ArrayList<PlayerBean> opponents){
        this.player = player;
        this.game = game;
        this.opponents = opponents;

        if(player.getState().equals(PlayerState.DRAW_CARD)){
            playerState.setText("Your turn, draw a card");
        }
        else if(player.getState().equals(PlayerState.PLAY_CARD)){
            playerState.setText("Your turn, place a card!");
        }
        else{
            playerState.setText("Wait for your turn!");
        }

        //hand
        if(player.getHand()[0] != null){
            h1 = player.getHand()[0].getCardNumber();
            if(player.getHand()[0].getType().equals("gold")){
                h1 = h1 + 40;
            }
            Image imageHand1 = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(h1) + ".png"));
            viewHand1 = new ImageView(imageHand1);
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
            viewHand2 = new ImageView(imageHand2);
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
            viewHand3 = new ImageView(imageHand3);
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
        viewFungiRetro = new ImageView(imageFungiRetro);
        viewFungiRetro.setFitHeight(42*3);
        viewFungiRetro.setFitWidth(63*3);
        ImageView viewFungiRetroBoard = new ImageView(imageFungiRetro);
        viewFungiRetroBoard.setFitHeight(42*2.5);
        viewFungiRetroBoard.setFitWidth(64*2.5);
        Image imageAnimalRetro = new Image(getClass().getResourceAsStream("/cards/backs/21.png"));
        viewAnimalRetro = new ImageView(imageAnimalRetro);
        viewAnimalRetro.setFitHeight(42*3);
        viewAnimalRetro.setFitWidth(63*3);
        ImageView viewAnimalRetroBoard = new ImageView(imageAnimalRetro);
        viewAnimalRetroBoard.setFitHeight(42*2.5);
        viewAnimalRetroBoard.setFitWidth(64*2.5);
        Image imagePlantRetro = new Image(getClass().getResourceAsStream("/cards/backs/11.png"));
        viewPlantRetro = new ImageView(imagePlantRetro);
        viewPlantRetro.setFitHeight(42*3);
        viewPlantRetro.setFitWidth(63*3);
        ImageView viewPlantRetroBoard = new ImageView(imagePlantRetro);
        viewPlantRetroBoard.setFitHeight(42*2.5);
        viewPlantRetroBoard.setFitWidth(64*2.5);
        Image imageInsectRetro = new Image(getClass().getResourceAsStream("/cards/backs/31.png"));
        viewInsectRetro = new ImageView(imageInsectRetro);
        viewInsectRetro.setFitHeight(42*3);
        viewInsectRetro.setFitWidth(63*3);
        ImageView viewInsectRetroBoard = new ImageView(imageInsectRetro);
        viewInsectRetroBoard.setFitHeight(42*2.5);
        viewInsectRetroBoard.setFitWidth(64*2.5);
        switch (rDeck){
            case RED -> resourceDeck.setGraphic(viewFungiRetroBoard);
            case BLUE -> resourceDeck.setGraphic(viewAnimalRetroBoard);
            case GREEN -> resourceDeck.setGraphic(viewPlantRetroBoard);
            case PURPLE -> resourceDeck.setGraphic(viewInsectRetroBoard);
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
        viewFungiGoldRetro = new ImageView(imageFungiGoldRetro);
        viewFungiGoldRetro.setFitHeight(42*3);
        viewFungiGoldRetro.setFitWidth(63*3);
        ImageView viewFungiGoldRetroBoard = new ImageView(imageFungiGoldRetro);
        viewFungiGoldRetroBoard.setFitHeight(42*2.5);
        viewFungiGoldRetroBoard.setFitWidth(64*2.5);
        Image imageAnimalGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/66.png"));
        viewAnimalGoldRetro = new ImageView(imageAnimalGoldRetro);
        viewAnimalGoldRetro.setFitHeight(42*3);
        viewAnimalGoldRetro.setFitWidth(63*3);
        ImageView viewAnimalGoldRetroBoard = new ImageView(imageAnimalGoldRetro);
        viewAnimalGoldRetroBoard.setFitHeight(42*2.5);
        viewAnimalGoldRetroBoard.setFitWidth(64*2.5);
        Image imagePlantGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/51.png"));
        viewPlantGoldRetro = new ImageView(imagePlantGoldRetro);
        viewPlantGoldRetro.setFitHeight(42*3);
        viewPlantGoldRetro.setFitWidth(63*3);
        ImageView viewPlantGoldRetroBoard = new ImageView(imagePlantGoldRetro);
        viewPlantGoldRetroBoard.setFitHeight(42*2.5);
        viewPlantGoldRetroBoard.setFitWidth(64*2.5);
        Image imageInsectGoldRetro = new Image(getClass().getResourceAsStream("/cards/backs/73.png"));
        viewInsectGoldRetro = new ImageView(imageInsectGoldRetro);
        viewInsectGoldRetro.setFitHeight(42*3);
        viewInsectGoldRetro.setFitWidth(63*3);
        ImageView viewInsectGoldRetroBoard = new ImageView(imageInsectGoldRetro);
        viewInsectGoldRetroBoard.setFitHeight(42*2.5);
        viewInsectGoldRetroBoard.setFitWidth(64*2.5);
        switch (gDeck){
            case RED -> goldDeck.setGraphic(viewFungiGoldRetroBoard);
            case BLUE -> goldDeck.setGraphic(viewAnimalGoldRetroBoard);
            case GREEN -> goldDeck.setGraphic(viewPlantGoldRetroBoard);
            case PURPLE -> goldDeck.setGraphic(viewInsectGoldRetroBoard);
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
            int[] coord = player.getBoard().getCardCoordinates(player.getBoard().getCardPosition().get(i));
            int card = player.getBoard().getCard(coord).getCardNumber();
            if(player.getBoard().getCard(coord).getType().equals("starter")){
                card = card + 80;
            }
            if(player.getBoard().getCard(coord).getType().equals("gold")){
                card = card + 40;
            }
            Image imageCard = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(card) + ".png"));
            if(!player.getBoard().getCard(coord).isNotBack()){
                imageCard = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(card) + ".png"));
            }
            ImageView viewCard = new ImageView(imageCard);
            viewCard.setFitHeight(42*3);
            viewCard.setFitWidth(64*3);
            viewCard.setLayoutX(996 + coord[0]*150);
            viewCard.setLayoutY(524 - coord[1]*77);
            board.getChildren().addFirst(viewCard);
            board.setLayoutX(1000);
            board.setLayoutY(530);
            int[][] buttonCornerCenter = {new int[]{6, 6, 7, 7}, new int[]{6, 7, 7, 6}};
            int[][] cornerCenter = {new int[]{-1, 1, 1, -1}, new int[]{1, 1, -1, -1}};
            int j = 0;
            for (CornerEnum c : CornerEnum.values()){
                if (player.getBoard().getCard(coord).getCorner(c).getState().equals(CornerState.VISIBLE)){
                    Button b = new Button();
                    b.setPrefSize(58, 52);
                    b.setStyle("-fx-background-color: transparent");
                    b.setOnAction(event -> {
                        if(GridPane.getRowIndex(b) < GridPane.getColumnIndex(b)){
                            placeCard(cardToPlace, new int[]{cornerCenter[0][1] + coord[0], cornerCenter[1][1] + coord[1]});
                            buttonBoard.getChildren().remove((buttonCornerCenter[1][1] + coord[0]), (buttonCornerCenter[0][1] - coord[1]));
                        }
                        else if(GridPane.getRowIndex(b) > GridPane.getColumnIndex(b)){
                            placeCard(cardToPlace, new int[]{cornerCenter[0][3] + coord[0], cornerCenter[1][3] + coord[1]});
                            buttonBoard.getChildren().remove((buttonCornerCenter[1][3] + coord[0]), (buttonCornerCenter[0][3] - coord[1]));
                        }
                        else{
                            if(GridPane.getRowIndex(b) % 2 == 0){
                                placeCard(cardToPlace, new int[]{cornerCenter[0][0] + coord[0], cornerCenter[1][0] + coord[1]});
                                buttonBoard.getChildren().remove((buttonCornerCenter[1][0] + coord[0]), (buttonCornerCenter[0][0] - coord[1]));
                            }
                            else{
                                placeCard(cardToPlace, new int[]{cornerCenter[0][2] + coord[0], cornerCenter[1][2] + coord[1]});
                                buttonBoard.getChildren().remove((buttonCornerCenter[1][2] + coord[0]), (buttonCornerCenter[0][2] - coord[1]));
                            }
                        }
                    });
                    buttonBoard.add(b, (buttonCornerCenter[1][j] + coord[0]), (buttonCornerCenter[0][j] - coord[1]));
                    b.setVisible(false);
                }
                j = j + 1;
            }
        }

        //score track
        ArrayList<PlayerBean> allPlayers = new ArrayList<>(opponents);
        allPlayers.add(player);
        for(PlayerBean p : allPlayers){
            if(pions.size() < allPlayers.size()){
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
        }
        for (int i = 0; i < pions.size(); i++){
            pions.get(i).setLayoutX(scoretrackFirstPion[0][allPlayers.get(i).getPoints()] + (scoretrackZero[0][i] - scoretrackZero[0][0]));
            pions.get(i).setLayoutY(scoretrackFirstPion[1][allPlayers.get(i).getPoints()] + (scoretrackZero[1][i] - scoretrackZero[1][0]));
        }

        //chat
        VBox v = new VBox();
        v.setSpacing(5);
        v.setAlignment(Pos.TOP_CENTER);
        for(String s : player.getChat()){
            Text t = new Text(s);
            t.setFont(new Font(10));
            v.getChildren().add(t);
            messages.getChildren().add(v);
        }
        if(receiver.getItems().size() < opponents.size()){
            receiver.getItems().add("global");
            for(PlayerBean p : opponents){
                receiver.getItems().add(p.getUsername());
            }
            receiver.setStyle("-fx-font-size:15");
            receiver.getSelectionModel().selectFirst();
        }

        //other players' playerboard
        if(otherPlayers.getItems().size() < opponents.size()){
            for(PlayerBean p : opponents){
                otherPlayers.getItems().add(p.getUsername());
            }
            otherPlayers.setStyle("-fx-font-size:20");
            otherPlayers.getSelectionModel().selectFirst();
        }

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
        //take the card in hand based on the number saved in cardToPlace (0 means the first card in hand) and place it
        //in the coordinates saved in newCardCoord
        guiHandler.placeCard(player.getHand()[cardToPlace], newCardCoord);
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(false);
        }
    }

}
