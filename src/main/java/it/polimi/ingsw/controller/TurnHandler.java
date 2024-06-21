package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.messages.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TurnHandler extends Observable {
    /**
     * game reference
     */
    private final Game game;
    private Controller controller;
    private int j = 0;
    private boolean endingCycle = false;
    private boolean endingByPlacingCard = false;
    private boolean disconnectedWhileInTurn = false;
    private int endCountDown;

    public TurnHandler(Game game, Controller controller){
        this.game = game;
        this.controller = controller;
    }

    public void startEnd(Player player){
        endingCycle = true;
        endCountDown = 2;
        if (player.getPlayerState() == PlayerState.PLAY_CARD){
            endingByPlacingCard = true;
        }
    }

    public void disconnectedWhileInTurn(Player player){
        if (player.getPlayerState().equals(PlayerState.PLAY_CARD)){
            player.setPlayerState(PlayerState.DRAW_CARD);
        }
        else if (player.getPlayerState().equals(PlayerState.DRAW_CARD)) {
            player.setPlayerState(PlayerState.PLAY_CARD);
        }
        disconnectedWhileInTurn = true;
        this.changePlayerState(player);
        disconnectedWhileInTurn = false;
    }

    public void changePlayerState(Player player){
        if (game.getDisconnections()+1 < game.getLobbySize() || disconnectedWhileInTurn) {
            int i = (player.getPlayerState().ordinal() + 1) % 3;
            if (endingCycle && endCountDown==0 && PlayerState.values()[i].equals(PlayerState.DRAW_CARD)){
                i=2;
            }
            if (player.isDisconnected() && PlayerState.values()[i].equals(PlayerState.DRAW_CARD)) {
                player.setPlayerState(PlayerState.values()[i]);
                try {
                    player.drawCard(game.getGameBoard().getResourceDeck());
                    notifyAll(new UpdateDeckMessage(game.getGameBoard().getResourceDeck().getFirst().getColor(), true));
                } catch (NotInTurnException | FullHandException | EmptyException e1) {
                    try {
                        player.drawCard(game.getGameBoard().getGoldDeck());
                        notifyAll(new UpdateDeckMessage(game.getGameBoard().getGoldDeck().getFirst().getColor(), false));
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
                game.setPlayerInTurn();
                Player playerInTurn = game.getPlayerInTurn();
                if (endingCycle && playerInTurn.isFirstToPlay()){
                    if (endCountDown > 0){
                        endCountDown--;
                    }
                    else{
                        playerInTurn.setPlayerState(PlayerState.NOT_IN_TURN);
                        notifyAll(new GameStateMessage(GameState.END));
                        try {
                            notifyAll(new WinnerMessage(game.endGame()));
                            controller.removeFromServer();
                        } catch (GameNotStartedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                notifyAll(new PlayerStateMessage(playerInTurn.getPlayerState(), playerInTurn.getUsername()));
                notifyAll(new PlayerBoardUpdateMessage(playerInTurn.getPlayerBoard(), playerInTurn.getUsername()));
            }
            player.setPlayerState(PlayerState.values()[i]);
            notifyAll(new PlayerStateMessage(player.getPlayerState(), player.getUsername()));
        }
        else {
            notifyAll(new WaitingReconnectionMessage(player.getUsername()));
            Timer t = new Timer();
            Timer rec = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Player winner = game.endGameByDisconnection(player);
                    declareWinnerByDisconnection(winner);
                    rec.cancel();
                    t.cancel();
                }
            }, 120000, 2000);

            rec.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (game.getDisconnections()+1 < game.getLobbySize()){
                        TurnHandler th = TurnHandler.this;
                        t.cancel();
                        th.changePlayerState(player);
                        th.notifyAll(new PlayerBoardUpdateMessage(player.getPlayerBoard(), player.getUsername()));
                        rec.cancel();
                    }
                }
            }, 1000, 2000);
        }
    }

    private void declareWinnerByDisconnection(Player p){
        notifyAll(new WinnerMessage(List.of(p)));
        notifyAll(new GameStateMessage(GameState.END));
        game.setGameState(GameState.END);
        controller.removeFromServer();
    }
    public String changeSetupPlayer(){
        j++;
        if (j == game.getLobbySize()){
            return null;
        }
        return game.getPlayers().get(j).getUsername();
    }
}
