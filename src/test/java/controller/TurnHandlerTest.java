package controller;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TurnHandler;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TurnHandlerTest {

    @Test
    @DisplayName("changing player state and player turn")
    void changePlayerState() {
        Controller controller = new Controller(2,1);
        Game game = controller.getGame();
        TurnHandler turn = controller.getTurnHandler();
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

    @Test
    @DisplayName("testing the changeSetupPlayer method")
    void changeSetupPlayerTest() {
        Controller controller = new Controller(3,1);
        Game game = controller.getGame();
        TurnHandler turn = controller.getTurnHandler();
        Player p1 = new Player("paolo", game);
        Player p2 = new Player("giovanna", game);
        Player p3 = new Player("peppe", game);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);

        try {
            game.startGame();
        } catch (NotEnoughPlayersException e) {
            System.err.println(e.getMessage());
        }

        game.getPlayers().get(1).setDisconnected(true);
        String name = turn.changeSetupPlayer();
        assertEquals(name, game.getPlayers().get(2).getUsername());
        assertNull(turn.changeSetupPlayer());

    }

    @Test
    @DisplayName("testing the startEnd method")
    void startEndTest() throws Exception {

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Controller controller = mock(Controller.class);
        Game game = mock(Game.class);


        TurnHandler turnHandler = spy(new TurnHandler(game, controller));
        java.lang.reflect.Field field = TurnHandler.class.getDeclaredField("endingByPlacingCard");
        field.setAccessible(true);


        when(player1.getPlayerState()).thenReturn(PlayerState.PLAY_CARD);
        when(player2.getPlayerState()).thenReturn(PlayerState.DRAW_CARD);

        turnHandler.startEnd(player2);
        assertFalse((Boolean) field.get(turnHandler));

        turnHandler.startEnd(player1);
        assertTrue((Boolean) field.get(turnHandler));

    }

    @Test
    @DisplayName("testing the disconnectedWhileInTurn method")
    void disconnectedWhileInTurnTest() throws Exception {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Controller controller = mock(Controller.class);
        Game game = mock(Game.class);

        TurnHandler turnHandler = spy(new TurnHandler(game, controller));
        java.lang.reflect.Field field = TurnHandler.class.getDeclaredField("disconnectedWhileInTurn");
        field.setAccessible(true);
        field.set(turnHandler,true);

        when(player1.getPlayerState()).thenReturn(PlayerState.DRAW_CARD);
        when(player2.getPlayerState()).thenReturn(PlayerState.PLAY_CARD);
        doNothing().when(turnHandler).changePlayerState(player1);
        doNothing().when(turnHandler).changePlayerState(player2);

        turnHandler.disconnectedWhileInTurn(player1);

        verify(player1).setPlayerState(PlayerState.PLAY_CARD);
        assertFalse((Boolean) field.get(turnHandler));

        field.set(turnHandler,true);
        turnHandler.disconnectedWhileInTurn(player2);

        verify(player2).setPlayerState(PlayerState.DRAW_CARD);
        assertFalse((Boolean) field.get(turnHandler));
    }

    @Test
    @DisplayName("testing the changePlayerState method in case the player disconnect while in turn")
    void changePlayerStateWHileDisconnectingTest() throws Exception {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Controller controller = mock(Controller.class);
        Game game = mock(Game.class);
        GameBoard board = mock(GameBoard.class);
        Card card = mock(Card.class);
        LinkedList<Card> deck = mock(LinkedList.class);
        LinkedList<Card> goldDeck = mock(LinkedList.class);

        TurnHandler turnHandler = spy(new TurnHandler(game, controller));
        java.lang.reflect.Field field = TurnHandler.class.getDeclaredField("disconnectedWhileInTurn");
        field.setAccessible(true);
        field.set(turnHandler,true);

        when(player1.getPlayerState()).thenReturn(PlayerState.PLAY_CARD);
        when(game.getDisconnections()).thenReturn(1);
        when(game.getLobbySize()).thenReturn(2);
        when(game.getPlayerInTurn()).thenReturn(player2);
        when(game.getGameBoard()).thenReturn(board);
        when(board.getResourceDeck()).thenReturn(deck);
        when(board.getGoldDeck()).thenReturn(goldDeck);
        doThrow(EmptyException.class).when(player1).drawCard(deck);
        when(player1.drawCard(game.getGameBoard().getGoldDeck())).thenReturn(card);
        when(card.getColor()).thenReturn(Color.BLUE);
        when(goldDeck.getFirst()).thenReturn(card);
        when(player1.isDisconnected()).thenReturn(true);

        turnHandler.changePlayerState(player1);

        verify(game).setPlayerInTurn();
        verify(player1).setPlayerState(PlayerState.NOT_IN_TURN);
        verify(turnHandler, times(2)).notifyAll(any(PlayerStateMessage.class));
        verify(turnHandler, times(1)).notifyAll(any(UpdateDeckMessage.class));
        verify(turnHandler).notifyAll(any(PlayerBoardUpdateMessage.class));
    }

    @Test
    @DisplayName("testing the declareWinnerByDisconnection method")
    void declareWinnerByDisconnectionTest() throws Exception {
        Player player1 = mock(Player.class);
        Controller controller = mock(Controller.class);
        Game game = mock(Game.class);

        TurnHandler turnHandler = spy(new TurnHandler(game, controller));
        java.lang.reflect.Field field = TurnHandler.class.getDeclaredField("disconnectedWhileInTurn");
        field.setAccessible(true);
        field.set(turnHandler,false);

        when(game.getDisconnections()).thenReturn(1);
        when(game.getLobbySize()).thenReturn(2);

        turnHandler.changePlayerState(player1);

        Thread.sleep(130000);

        verify(turnHandler).notifyAll(any(WaitingReconnectionMessage.class));
        verify(game).endGameByDisconnection();
        verify(game).setGameState(GameState.END);
        verify(controller).removeFromServer();
        verify(turnHandler).notifyAll(any(WinnerMessage.class));
        verify(turnHandler).notifyAll(any(GameStateMessage.class));

    }

}