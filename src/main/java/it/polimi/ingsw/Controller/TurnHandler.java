package it.polimi.ingsw.Controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.messages.*;

import java.util.Timer;
import java.util.TimerTask;

public class TurnHandler extends Observable {
    /**
     * game reference
     */
    private final Game game;
    private int i = 0;

    public TurnHandler(Game game){
        this.game = game;
    }

    public void changePlayerState(Player player){
        if (game.getLobbySize()-2 >= game.getDisconnections()) {
            int i = (player.getPlayerState().ordinal() + 1) % 3;

            if (player.isDisconnected() && PlayerState.values()[i].equals(PlayerState.DRAW_CARD)) {
                try {
                    player.drawCard(game.getGameBoard().getResourceDeck());
                } catch (NotInTurnException | FullHandException | EmptyException e1) {
                    try {
                        player.drawCard(game.getGameBoard().getGoldDeck());
                    } catch (NotInTurnException | FullHandException | EmptyException ignored){}
                }
                i=2;
            }
            if (i == 2) {
                Player playerInTurn = game.setPlayerInTurn();
                notifyAll(new PlayerStateMessage(playerInTurn.getPlayerState(), playerInTurn.getUsername()));
                notifyAll(new PlayerBoardUpdateMessage(playerInTurn.getPlayerBoard(), playerInTurn.getUsername()));
            }
            player.setPlayerState(PlayerState.values()[i]);
        }
        else {
            notifyAll(new GenericMessage("only one player connected, the other players have one minute to reconnect."));
            Timer ping = new Timer();
            ping.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.endGameByDisconnection(player);
                    ping.cancel();
                }
            }, 60000, 2000);
            while (game.getDisconnections()<2){
                //TODO fermare tutti i thread
            }
            ping.cancel();
            this.changePlayerState(player);
        }
    }

    public String changeSetupPlayer(){
        i++;
        if (i == game.getLobbySize()){
            return null;
        }
        return game.getPlayers().get(i).getUsername();
    }
}
