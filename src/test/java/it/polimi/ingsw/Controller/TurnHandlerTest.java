package it.polimi.ingsw.Controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnHandlerTest {

    @Test
    @DisplayName("normally changing player state and player turn")
    void changePlayerState() {
        Game game = new Game(2);
        TurnHandler turn = new TurnHandler(game);
        Player p1 = new Player("paolo", game);
        Player p2 = new Player("giovanna", game);
        game.addPlayer(p1);
        game.addPlayer(p2);
        try {
            game.startGame();
        } catch (NotEnoughPlayersException e) {
            System.err.println(e.getMessage());
        }

        assertEquals(PlayerState.PLAY_CARD, game.getFirstPlayer().getPlayerState());
        assertEquals(PlayerState.NOT_IN_TURN, game.getPlayers().get(1).getPlayerState());

        turn.changePlayerState(game.getFirstPlayer());
        assertEquals(PlayerState.DRAW_CARD, game.getFirstPlayer().getPlayerState());
        assertEquals(PlayerState.NOT_IN_TURN, game.getPlayers().get(1).getPlayerState());

        turn.changePlayerState(game.getFirstPlayer());
        assertEquals(PlayerState.NOT_IN_TURN, game.getFirstPlayer().getPlayerState());
        assertEquals(PlayerState.PLAY_CARD, game.getPlayers().get(1).getPlayerState());

        turn.changePlayerState(game.getPlayers().get(1));
        turn.changePlayerState(game.getPlayers().get(1));
        assertEquals(PlayerState.NOT_IN_TURN, game.getPlayers().get(1).getPlayerState());
        assertEquals(PlayerState.PLAY_CARD, game.getFirstPlayer().getPlayerState());

    }
}