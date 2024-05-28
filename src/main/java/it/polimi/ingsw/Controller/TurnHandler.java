package it.polimi.ingsw.Controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TurnHandler extends Observable {
    /**
     * game reference
     */
    private final Game game;
    private int j = 0;
    private boolean endingCycle = false;
    private boolean endingByPlacingCard = false;
    private int endCountDown;

    public TurnHandler(Game game){
        this.game = game;
    }

    public void startEnd(Player player){
        endingCycle = true;
        endCountDown = 2;
        if (player.getPlayerState() == PlayerState.PLAY_CARD){
            endingByPlacingCard = true;
        }
    }

    public void changePlayerState(Player player){
        if (game.getLobbySize()-2 >= game.getDisconnections()) {
            int i = (player.getPlayerState().ordinal() + 1) % 3;
            if (endingCycle && endCountDown==0 && PlayerState.values()[i].equals(PlayerState.DRAW_CARD)){
                i++;
            }
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
                if (endCountDown == 2){
                    if (endingByPlacingCard) {
                        notifyAll(new GenericMessage(player.getUsername() + " reached 20 points, at the end of the current round will start the last one"));
                    }
                    else{
                        notifyAll(new GenericMessage("both decks are empty, at the end of the current round will start the last one"));
                    }
                    endCountDown--;
                }
                Player playerInTurn = game.setPlayerInTurn();
                if (endingCycle && playerInTurn.isFirstToPlay()){
                    if (endCountDown > 0){
                        endCountDown--;
                    }
                    else{
                        playerInTurn.setPlayerState(PlayerState.NOT_IN_TURN);
                        notifyAll(new GameStateMessage(GameState.END));
                        try {
                            notifyAll(new WinnerMessage(game.endGame()));
                        } catch (GameNotStartedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
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
                    Player winner = game.endGameByDisconnection(player);
                    declareWinnerByDisconnection(winner);
                    ping.cancel();
                }
            }, 60000, 2000);
            while (game.getDisconnections()<2);
            ping.cancel();
            this.changePlayerState(player);
        }
    }

    private void declareWinnerByDisconnection(Player p){
        notifyAll(new WinnerMessage(List.of(p)));
    }
    public String changeSetupPlayer(){
        j++;
        if (j == game.getLobbySize()){
            return null;
        }
        return game.getPlayers().get(j).getUsername();
    }
}
