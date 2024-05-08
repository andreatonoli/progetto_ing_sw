package it.polimi.ingsw.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;

public class TurnHandler extends Observable {
    /**
     * game reference
     */
    private Game game;

    public TurnHandler(Game game){
        this.game = game;
    }
    //TODO: se il giocatore resta da solo??
    //TODO: (risolto in game metodo setplayerinturn) mettere controllo del client se disconnesso per fargli saltare il turno
    public void changePlayerState(Player player){
        if (game.getLobbySize()-2 >= game.getDisconnections()) {
            int i = (player.getPlayerState().ordinal() + 1) % 3;
            if (i == 0) {
                game.setPlayerInTurn();
            }
            player.setPlayerState(PlayerState.values()[i]);
        }
        else {
            // mettere il timer per finire la partita e trovare un modo per bloccare il gioco
        }
    }

}
