package Controller;
import model.*;
import model.enums.PlayerState;
import model.player.Player;
import observer.Observable;

public class TurnHandler extends Observable {
    private Game game;
    //private transient Server server;

    public TurnHandler(Game game/*, Server server*/){
        this.game=game;
        //this.server=server;
    }

    public void changePlayerState(Player player){
        int i = (player.getPlayerState().ordinal() + 1) % 3;
        if (i == 0){
            game.setPlayerInTurn();
        }
        player.setPlayerState(PlayerState.values()[i]);
    }

}
