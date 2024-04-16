package Controller;
import model.*;

import java.io.Serializable;

public class TurnHandler implements Serializable {
    Game game;

    public TurnHandler(Game game){
        this.game=game;
    }

    public void changePlayerState(Player player){
        int i = (player.getPlayerState().ordinal() + 1) % 3;
        if (i == 0){
            game.setPlayerInTurn();
        }
        player.setPlayerState(PlayerState.values()[i]);
    }

}
