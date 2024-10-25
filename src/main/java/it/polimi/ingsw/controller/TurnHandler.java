package it.polimi.ingsw.controller;

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
     * Reference to the game.
     */
    private final Game game;

    /**
     * Reference to the controller.
     */
    private final Controller controller;

    /**
     * Time in milliseconds before the game ends if all players but one are disconnected.
     */
    private int RECONNECTION_TIME = 120000;

    /**
     * Index of the player in the lobby.
     */
    private int j = 0;

    /**
     * Boolean that indicates if we are in the last turns of the game.
     */
    private boolean endingCycle = false;

    /**
     * Boolean that indicates if the game is ending by placing a card.
     */
    private boolean endingByPlacingCard = false;

    /**
     * Boolean that indicates if a player disconnected while in turn.
     */
    private boolean disconnectedWhileInTurn = false;

    /**
     * Counter used to manage the end of the game.
     */
    private int endCountDown;

    /**
     * Constructor of the class.
     * @param game is the reference to the game.
     * @param controller is the reference to the controller.
     */
    public TurnHandler(Game game, Controller controller){
        this.game = game;
        this.controller = controller;
    }

    /**
     * Method used to indicate that the player has started the end of the game.
     * @param player is the player that started the end of the game.
     */
    public void startEnd(Player player){
        endingCycle = true;
        endCountDown = 2;
        if (player.getPlayerState() == PlayerState.PLAY_CARD){
            endingByPlacingCard = true;
        }
    }

    /**
     * Method used to manage the change of the player state in case the player disconnect while in turn.
     */
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

    /**
     * Method used to manage the change of the player state.
     * If the player is disconnected and has already placed a card, he will draw a card.
     * at the end of the player's turn, a new player will be set in turn.
     * @param player is the player that has to change the state.
     */
    public void changePlayerState(Player player){
        if (game.getDisconnections()+1 < game.getLobbySize() || disconnectedWhileInTurn) {
            int i = (player.getPlayerState().ordinal() + 1) % 3;
            if (endingCycle && endCountDown==0 && i==1){
                i=2;
            }
            if (player.isDisconnected() && i==1) {
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
                            ArrayList<Player> winners = game.endGame();
                            for (Player p : game.getPlayers()) {
                                notifyAll(new ScoreUpdateMessage(p.getPoints(), p.getUsername()));
                            }
                            notifyAll(new WinnerMessage(winners));
                            controller.removeFromServer();
                        } catch (GameNotStartedException e) {
                            System.out.println(e.getMessage());
                        }
                        //Interrupts the flow and avoid sending useless data
                        return;
                    }
                }
                notifyAll(new PlayerStateMessage(playerInTurn.getPlayerState(), playerInTurn.getUsername()));
                notifyAll(new PlayerBoardUpdateMessage(playerInTurn.getPlayerBoard(), playerInTurn.getUsername()));
            }
            player.setPlayerState(PlayerState.values()[i]);
            notifyAll(new PlayerStateMessage(player.getPlayerState(), player.getUsername()));
        }
        //If the player is disconnected and is the last one, the game will end after 2 minutes
        else {
            notifyAll(new WaitingReconnectionMessage(player.getUsername()));
            Timer t = new Timer();
            Timer rec = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.endGameByDisconnection();
                    declareWinnerByDisconnection(player);
                    rec.cancel();
                    t.cancel();
                }
            }, RECONNECTION_TIME, 2000);

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

    /**
     * Method used to set the winner in case of disconnections.
     * @param p is the last player remaining in the game.
     */
    private void declareWinnerByDisconnection(Player p){
        notifyAll(new GameStateMessage(GameState.END));
        notifyAll(new WinnerMessage(List.of(p)));
        game.setGameState(GameState.END);
        controller.removeFromServer();
    }

    /**
     * Method used to change the player in turn during the setup phase.
     * @return the username of the new player in turn (null if all the player have ended the setup).
     */
    public String changeSetupPlayer(){
        j++;
        if (j != game.getLobbySize() && game.getPlayers().get(j).isDisconnected() ){
            j++;
        }
        if (j == game.getLobbySize()){
            return null;
        }
        return game.getPlayers().get(j).getUsername();
    }
}
