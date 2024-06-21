package it.polimi.ingsw.view.controllers;

import it.polimi.ingsw.model.enums.CornerEnum;
import it.polimi.ingsw.model.enums.CornerState;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.view.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class OtherPlayerBoardsSceneController extends GenericController{

    @FXML
    private AnchorPane board;
    @FXML
    private Text t;

    public void setBoard(PlayerBean player){
        t.setText(player.getUsername() + "'s board");
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
            if(!player.getBoard().getCard(coord).isNotBack()){
                imageCard = new Image(getClass().getResourceAsStream("/cards/backs/" + String.valueOf(card) + ".png"));
            }
            ImageView viewCard = new ImageView(imageCard);
            viewCard.setFitHeight(42*3);
            viewCard.setFitWidth(64*3);
            viewCard.setLayoutX(1013 + coord[1]*150);
            viewCard.setLayoutY(532 + coord[0]*77);
            board.getChildren().add(viewCard);
        }
    }

    public void backPressed(ActionEvent e){ Gui.addReturnValue("backToMain"); }

}
