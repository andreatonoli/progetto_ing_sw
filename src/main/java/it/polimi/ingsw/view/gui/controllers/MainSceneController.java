package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

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
    private VBox messagesV;
    @FXML
    private ChoiceBox<String> receiver;
    @FXML
    private TextField textMessage;
    @FXML
    private ChoiceBox<String> otherPlayers;
    @FXML
    private Text playerState;
    @FXML
    private Button chat;
    @FXML
    private Button otherPlayersBoard;
    @FXML
    private TextArea errorBox;

    /**This matrix has the x and y position of the zero point for alle the 4 pions */
    private final int[][] scoretrackZero = {new int[] {65, 115, 65, 115}, new int[] {670, 670, 715, 715}};
    /** this matrix has the x and y values of the first pion based on points */
    private final int[][] scoretrackFirstPion = {new int[]{65, 155, 245, 290, 200, 110, 20, 20, 110, 200, 290, 290, 200, 110, 20, 20, 110, 200, 290, 290, 135, 40, 40, 40, 95, 175, 260, 310, 310, 175}, new int[]{670, 670, 670, 590, 590, 590, 590, 510, 510, 510, 505, 425, 425, 425, 425, 345, 345, 345, 345, 260, 220, 265, 180, 100, 20, 5, 20, 90, 170, 105}};

    /**
     * coordinates of the buttons with respect to the center of the card.
     */
    int[][] buttonCornerCenter = { new int[]{6, 6, 7, 7}, new int[]{6, 7, 7, 6} };
    private int cardToPlace;

    private final ArrayList<ImageView> pions = new ArrayList<>();

    private PlayerBean player;
    private GameBean game;
    private ArrayList<PlayerBean> opponents;

    private ImageView viewHand1;
    private ImageView viewHand2;
    private ImageView viewHand3;

    private GuiInputHandler guiHandler;
    private boolean firstTime = true;
    ArrayList<Color> pionsColor = new ArrayList<>();

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    /**
     * Method that binds the events to the buttons.
     */
    public void bindEvents(){
        resourceDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.drawDeckButtonClicked("resource"));
        resource1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.drawCardButtonClicked("1"));
        resource2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.drawCardButtonClicked("2"));
        goldDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.drawDeckButtonClicked("gold"));
        gold1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.drawCardButtonClicked("3"));
        gold2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guiHandler.drawCardButtonClicked("4"));
        flip1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> flipCard(0));
        flip2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> flipCard(1));
        flip3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> flipCard(2));
        chat.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!textMessage.getText().isEmpty()){
                if(receiver.getSelectionModel().getSelectedItem().equals("global")){
                    guiHandler.sendGlobalMessageButtonClicked(textMessage.getText());
                }
                else{
                    guiHandler.sendMessageButtonClicked(textMessage.getText(), receiver.getSelectionModel().getSelectedItem());
                }
                textMessage.clear();
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

    public void setColors(){
        if (game.getState().equals(GameState.IN_GAME)){

            firstTime = false;
            ArrayList<PlayerBean> allPlayers = new ArrayList<>(opponents);
            allPlayers.add(player);
            pions.clear();
            pionsColor = new ArrayList<>();

            for (PlayerBean allPlayer : allPlayers) {
                if (allPlayer.getPionColor() != null) {

                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + allPlayer.getPionColor().toString().toLowerCase() + ".png")));
                    ImageView iView = new ImageView(image);

                    iView.setFitHeight(40);
                    iView.setFitWidth(40);
                    scoreBoard.getChildren().add(iView);

                    pions.add(iView);
                    pionsColor.add(allPlayer.getPionColor());
                }
            }
        }
    }

    /**
     * Method that sets the board of the player, his chat and the common board.
     * @param player the player
     * @param game the game
     * @param opponents the opponents
     */
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

        int cardIndex;
        Image imageHand;

        //hand
        if(player.getHand()[0] != null){
            cardIndex = player.getHand()[0].getCardNumber();
            if(player.getHand()[0].getType().equals("gold")){
                cardIndex += 40;
            }
            if (player.getHand()[0].isNotBack()){
                imageHand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + cardIndex + ".png")));
                viewHand1 = new ImageView(imageHand);
                viewHand1.setFitHeight(42*3);
                viewHand1.setFitWidth(63*3);
                flip1.setVisible(true);
                hand1.setVisible(true);
                hand1.setGraphic(viewHand1);
            }
            else{
                flipCard(0);
            }
        }
        else{
            flip1.setVisible(false);
            hand1.setVisible(false);
        }
        if(player.getHand()[1] != null){
            cardIndex = player.getHand()[1].getCardNumber();
            if(player.getHand()[1].getType().equals("gold")){
                cardIndex += 40;
            }
            if (player.getHand()[1].isNotBack()){
                imageHand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + cardIndex + ".png")));
                viewHand2 = new ImageView(imageHand);
                viewHand2.setFitHeight(42*3);
                viewHand2.setFitWidth(63*3);
                flip2.setVisible(true);
                hand2.setVisible(true);
                hand2.setGraphic(viewHand2);
            }
            else{
                flipCard(1);
            }
        }
        else{
            flip2.setVisible(false);
            hand2.setVisible(false);
        }
        if(player.getHand()[2] != null){
            cardIndex = player.getHand()[2].getCardNumber();
            if(player.getHand()[2].getType().equals("gold")){
                cardIndex += 40;
            }
            if (player.getHand()[2].isNotBack()){
                imageHand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + cardIndex + ".png")));
                viewHand3 = new ImageView(imageHand);
                viewHand3.setFitHeight(42*3);
                viewHand3.setFitWidth(63*3);
                flip3.setVisible(true);
                hand3.setVisible(true);
                hand3.setGraphic(viewHand3);
            }
            else {
                flipCard(2);
            }
        }
        else{
            flip3.setVisible(false);
            hand3.setVisible(false);
        }
        
        //personal achievement
        int pa = player.getAchievement().getId() + 86;
        Image imagePersonalAch = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + pa + ".png")));
        personalAch.setImage(imagePersonalAch);

        //common board
        int retroNumber = 0;
        ImageView viewRetro;
        Image imageRetro = null;
        if (!game.getResourceDeckRetro().equals(Color.EMPTY)) {
            retroNumber = (game.getResourceDeckRetro().ordinal() * 10) + 1;
            imageRetro = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/backs/" + retroNumber + ".png")));
            viewRetro = new ImageView(imageRetro);
            viewRetro.setFitHeight(42*2.5);
            viewRetro.setFitWidth(64*2.5);
            resourceDeck.setGraphic(viewRetro);
        }
        else{
            resourceDeck.setVisible(false);
            resourceDeck.setGraphic(null);
        }

        if (!game.getGoldDeckRetro().equals(Color.EMPTY)) {
            retroNumber = (game.getGoldDeckRetro().ordinal() * 10) + 41;
            imageRetro = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/backs/" + retroNumber + ".png")));
            viewRetro = new ImageView(imageRetro);
            viewRetro.setFitHeight(42*2.5);
            viewRetro.setFitWidth(64*2.5);
            goldDeck.setGraphic(viewRetro);
        }
        else{
            goldDeck.setVisible(false);
            goldDeck.setGraphic(null);
        }

        int r1 = game.getCommonResources()[0].getCardNumber();
        int r2 = game.getCommonResources()[1].getCardNumber();
        Image imageResource1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + r1 + ".png")));
        ImageView viewResource1 = new ImageView(imageResource1);
        viewResource1.setFitHeight(42*2.5);
        viewResource1.setFitWidth(64*2.5);
        Image imageResource2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + r2 + ".png")));
        ImageView viewResource2 = new ImageView(imageResource2);
        viewResource2.setFitHeight(42*2.5);
        viewResource2.setFitWidth(64*2.5);
        resource1.setGraphic(viewResource1);
        resource2.setGraphic(viewResource2);

        int g1 = game.getCommonGold()[0].getCardNumber() + 40;
        int g2 = game.getCommonGold()[1].getCardNumber() + 40;
        Image imageGold1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + g1 + ".png")));
        ImageView viewGold1 = new ImageView(imageGold1);
        viewGold1.setFitHeight(42*2.5);
        viewGold1.setFitWidth(64*2.5);
        Image imageGold2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + g2 + ".png")));
        ImageView viewGold2 = new ImageView(imageGold2);
        viewGold2.setFitHeight(42*2.5);
        viewGold2.setFitWidth(64*2.5);
        gold1.setGraphic(viewGold1);
        gold2.setGraphic(viewGold2);

        int a1 = game.getCommonAchievement()[0].getId() + 86;
        int a2 = game.getCommonAchievement()[1].getId() + 86;
        Image imageAchievement1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + a1 + ".png")));
        achievement1.setImage(imageAchievement1);
        Image imageAchievement2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + a2 + ".png")));
        achievement2.setImage(imageAchievement2);

        //player board
        buttonBoard.getChildren().clear();
        Set<Integer> keys = player.getBoard().getPositionCardKeys();
        Integer lastElement = 0;
        for (Integer key : keys) {
            lastElement = key;
            int[] coord = player.getBoard().getCardCoordinates(player.getBoard().getCardPosition().get(key));
            addButton(coord);
        }
        printCard(lastElement);

        //score track
        if(firstTime){
            setColors();
        }
        ArrayList<PlayerBean> allPlayers = new ArrayList<>(opponents);
        allPlayers.add(player);
        for (int i = 0; i < pions.size(); i++){
            for(PlayerBean p : allPlayers){
                if(p.getPionColor() != null && p.getPionColor().equals(pionsColor.get(i))){
                    pions.get(i).setLayoutX(scoretrackFirstPion[0][p.getPoints()] + (scoretrackZero[0][i] - scoretrackZero[0][0]));
                    pions.get(i).setLayoutY(scoretrackFirstPion[1][p.getPoints()] + (scoretrackZero[1][i] - scoretrackZero[1][0]));
                }
            }
        }

        //chat
        messagesV.getChildren().clear();
        messagesV.setSpacing(5);
        messagesV.setAlignment(Pos.TOP_CENTER);
        for(String s : player.getChat()){
            Text t = new Text(s);
            t.setStyle("-fx-font-size: 20;");
            messagesV.getChildren().add(t);
        }

        //Select handler
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

    /**
     * Method called if the player wants to place the first card in his hand.
     */
    @FXML
    public void wantToPlaceHand1() {
        cardToPlace = 0;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    /**
     * Method called if the player wants to place the second card in his hand.
     */
    @FXML
    public void wantToPlaceHand2() {
        cardToPlace = 1;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    /**
     * Method called if the player wants to place the third card in his hand.
     */
    @FXML
    public void wantToPlaceHand3() {
        cardToPlace = 2;
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(true);
        }
    }

    /**
     * Method that places the card in the coordinates given.
     * @param cardToPlace the card to place
     * @param newCardCoord the coordinates where to place the card
     */
    @FXML
    public void placeCard(int cardToPlace, int[] newCardCoord){
        //take the card in hand based on the number saved in cardToPlace (0 means the first card in hand) and place it
        //in the coordinates saved in newCardCoord
        guiHandler.placeCard(player.getHand()[cardToPlace], newCardCoord);
        for (Node b : buttonBoard.getChildren()){
            b.setVisible(false);
        }
    }

    /**
     * Method that sets the message in the error box.
     * @param message the message to set
     * @param isError if the message is an error
     */
    public void setMessage(String message, boolean isError){
        errorBox.clear();
        errorBox.setText(message);
        if(isError){
            errorBox.setStyle("-fx-font-size: 30; -fx-text-fill: red; -fx-stroke: black; -fx-stroke-width: 1.5;");
        }
        else{
            errorBox.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-stroke: black; -fx-stroke-width: 1.5;");
        }
    }

    /**
     * Method that flips a card in the hand of the player.
     * @param index the index of the card to flip.
     */
    public void flipCard(int index){
        int number;
        Image imageHand;

        if (!player.getHand()[index].isNotBack()){
            number = player.getHand()[index].getCardNumber();
            player.getHand()[index].setCurrentSide();
            if (player.getHand()[index].getType().equals("gold")){
                number += 40;
            }
            imageHand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + number + ".png")));
        }
        else{
            number = player.getHand()[index].getCardNumber();
            if(player.getHand()[index].getType().equals("gold")){
                number += 40;
            }
            number = getNumber(number);
            player.getHand()[index].setCurrentSide();
            imageHand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/backs/" + number + ".png")));
        }

        if (index == 0){
            viewHand1 = new ImageView(imageHand);
            viewHand1.setFitHeight(42*3);
            viewHand1.setFitWidth(63*3);
            hand1.setVisible(true);
            hand1.setGraphic(viewHand1);
        }
        else if (index == 1){
            viewHand2 = new ImageView(imageHand);
            viewHand2.setFitHeight(42*3);
            viewHand2.setFitWidth(63*3);
            hand2.setVisible(true);
            hand2.setGraphic(viewHand2);
        }
        else{
            viewHand3 = new ImageView(imageHand);
            viewHand3.setFitHeight(42*3);
            viewHand3.setFitWidth(63*3);
            hand3.setVisible(true);
            hand3.setGraphic(viewHand3);
        }
    }

    /**
     * Method that prints the last added card on the board.
     * @param lastElement the last added card on the board.
     */
    public void printCard(Integer lastElement){
        int[] coord = player.getBoard().getCardCoordinates(player.getBoard().getCardPosition().get(lastElement));
        int card = player.getBoard().getCard(coord).getCardNumber();
        if(player.getBoard().getCard(coord).getType().equals("starter")){
            card = card + 80;
        }
        if(player.getBoard().getCard(coord).getType().equals("gold")){
            card = card + 40;
        }
        Image imageCard = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/fronts/" + card + ".png")));
        if(!player.getBoard().getCard(coord).isNotBack()){
            card = getNumber(card);
            imageCard = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/backs/" + card + ".png")));
        }
        ImageView viewCard = new ImageView(imageCard);
        viewCard.setFitHeight(42*3);
        viewCard.setFitWidth(64*3);
        viewCard.setLayoutX(996 + coord[0]*150);
        viewCard.setLayoutY(524 - coord[1]*77);
        board.getChildren().addLast(viewCard);
        board.setLayoutX(1000);
        board.setLayoutY(530);
        addButton(coord);
    }

    /**
     * Method that adds buttons on the card.
     * @param coord the coordinates of the card.
     */
    public void addButton(int[] coord) {
        int j = 0;
        buttonBoard.toFront();
        for (CornerEnum c : CornerEnum.values()){
            if (player.getBoard().getCard(coord).getCorner(c).getState().equals(CornerState.VISIBLE)){
                Button b = new Button();
                b.setPrefSize(58, 52);
                b.setStyle("-fx-background-color: transparent");
                b.setOnAction(event -> {
                    placeCard(cardToPlace, new int[]{coord[0] + c.getX(), coord[1] + c.getY()});
                    buttonBoard.getChildren().remove(b);
                });
                buttonBoard.add(b, (buttonCornerCenter[1][j] + coord[0]), (buttonCornerCenter[0][j] - coord[1]));
                b.setVisible(false);
            }
            j += 1;
        }
    }

    public void drawBoardOnRecon(){
        buttonBoard.getChildren().clear();
        Set<Integer> keys = player.getBoard().getPositionCardKeys();
        for (Integer key : keys) {
            printCard(key);
        }
    }

    /**
     * Method that returns the number of the back of the card to take the correct image.
     * @param number the number of the card
     * @return the number of the back of the card
     */
    private int getNumber(int number) {
        if (number <= 80){
            return ((number - 1) / 10) * 10 + 1;
        }
        return number;
    }
}
