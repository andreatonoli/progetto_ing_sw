package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class OtherPlayerBoardsSceneController extends GenericController{

    @FXML
    private AnchorPane board;
    @FXML
    private Text t;
    @FXML
    private Button back;

    GuiInputHandler guiHandler;
    private PlayerBean player;
    private GameBean game;
    private ArrayList<PlayerBean> opponents;

    @FXML
    public void initialize(){
        guiHandler = GuiInputHandler.getInstance();
        bindEvents();
    }

    public void bindEvents(){
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            guiHandler.backButtonClicked(player, game, opponents);
        });
    }

    public void setBoard(PlayerBean other, PlayerBean player, GameBean game, ArrayList<PlayerBean> opponents){
        this.player = player;
        this.game = game;
        this.opponents = opponents;

        t.setText(other.getUsername() + "'s board");
        for (Integer i : other.getBoard().getPositionCardKeys()){
            int[] coord = other.getBoard().getCardCoordinates(other.getBoard().getCardPosition().get(i));
            int card = other.getBoard().getCard(coord).getCardNumber();
            if(other.getBoard().getCard(coord).getType().equals("starter")){
                card = card + 80;
            }
            if(other.getBoard().getCard(coord).getType().equals("gold")){
                card = card + 40;
            }
            Image imageCard = new Image(getClass().getResourceAsStream("/cards/fronts/" + String.valueOf(card) + ".png"));
            if(!other.getBoard().getCard(coord).isNotBack()){
                imageCard = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(card) + ".png"));
            }
            ImageView viewCard = new ImageView(imageCard);
            viewCard.setFitHeight(42*3);
            viewCard.setFitWidth(64*3);
            viewCard.setLayoutX(996 + coord[0]*150);
            viewCard.setLayoutY(524 - coord[1]*77);
            board.getChildren().addFirst(viewCard);
        }
    }
}
