package Controller;
import model.*;

public class TurnHandler {
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

    public void changeGameState(){
        int i = (game.getGameState().ordinal() + 1) % 4;
        game.setGameState(GameState.values()[i]);
    }

}
