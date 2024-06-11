package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TurnHandler;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.server.Connection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
    @Test
    @DisplayName("ChooseObj test")
    public void testValidChooseObj() {
        // Create local mocks
        Connection user = mock(Connection.class);
        Achievement chosenAchievement = new AchievementDiagonal(Color.RED, 1);
        Player player = mock(Player.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 7));

        // Stub the getPlayerByClient method to return the mocked player
        doReturn(player).when(controller).getPlayerByClient(user);

        // Creating array of achievement from which choose from //TODO: commentare bene
        Achievement[] personalObj = new Achievement[2];
        personalObj[0] = new AchievementDiagonal(Color.RED, 1);
        personalObj[1] = new AchievementDiagonal(Color.PURPLE, 4);
        when(player.getPersonalObj()).thenReturn(personalObj);

        // Ensure that the achievement is one of the player's personal objectives
        assertTrue(chosenAchievement.equals(personalObj[0]) || chosenAchievement.equals(personalObj[1]));

        // Call the method to be tested
        controller.chooseObj(user, chosenAchievement);

        // Verify that setChosenObj was called on the player with the achievement
        verify(player).setChosenObj(chosenAchievement);
    }

    @Test
    @DisplayName("User chooses an invalid achievement")
    public void testInvalidChooseObj(){
        // Create local mocks
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        //Creating achievement with same id but different color
        Achievement chosenAchievement = new AchievementDiagonal(Color.BLUE, 1);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 7));

        // Stub the getPlayerByClient method to return the mocked player
        doReturn(player).when(controller).getPlayerByClient(user);

        // Creating array of achievement from which choose from //TODO: commentare bene
        Achievement[] personalObj = new Achievement[2];
        personalObj[0] = new AchievementDiagonal(Color.RED, 1);
        personalObj[1] = new AchievementDiagonal(Color.PURPLE, 4);
        when(player.getPersonalObj()).thenReturn(personalObj);

        // Ensure that the achievement is not one of the player's personal objectives
        assertFalse(chosenAchievement.equals(personalObj[0]) && chosenAchievement.equals(personalObj[1]));

        // Call the method to be tested
        controller.chooseObj(user, chosenAchievement);

        // Verify that setChosenObj was not called
        verify(player, never()).setChosenObj(chosenAchievement);
    }

    /*
    @Test
    public void testDrawCardFromResourceDeck() throws Exception {
        // Create local mocks
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> resourceDeck = new LinkedList<>();
        Card drawedCard = new Card(
                new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}, 1),
                new CardBack(List.of(Symbols.FUNGI)),
                "resource",
                30,
                Color.PURPLE
        );

        // Ensure the resourceDeck is mocked correctly
        resourceDeck.add(drawedCard);

        // Create an instance of the class under test
        Controller controller = spy(new Controller(4, 0));
        when(controller.getGame()).thenReturn(game);

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Set up the game board and decks
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getResourceDeck()).thenReturn(resourceDeck);

        // Stub the getPlayerByClient method to return the mocked player
        when(controller.getPlayerByClient(user)).thenReturn(player);

        // Stub the player's drawCard method to return the drawn card
        // Ensure it uses the exact same resourceDeck instance
        when(player.drawCard(same(resourceDeck))).thenReturn(drawedCard); // Use same() to match the exact reference

        // Stub player state
        when(player.getPlayerState()).thenReturn(PlayerState.DRAW_CARD);

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify interactions
        verify(player).drawCard(resourceDeck);
        verify(turnHandler).changePlayerState(player);
        verify(user).sendMessage(any(UpdateCardMessage.class));
        verify(controller).notifyAll(any(UpdateDeckMessage.class));
    }

    @Test
    public void testDrawCardFromGoldDeck() throws Exception {
        // Create local mocks
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> goldDeck = new LinkedList<>();
        Card drawedCard = mock(Card.class);

        // Create an instance of the class under test
        Controller controller = spy(new Controller(4, 0)); // Replace 'Controller' with the actual class name
        when(controller.getGame()).thenReturn(game);

        // Set up the game board and decks
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getGoldDeck()).thenReturn(goldDeck);
        goldDeck.add(drawedCard); // Add a card to the deck

        // Stub the getPlayerByClient method to return the mocked player
        doReturn(player).when(controller).getPlayerByClient(user);

        // Stub the player's drawCard method to return the drawn card
        when(player.drawCard(goldDeck)).thenReturn(drawedCard);

        // Call the method to be tested
        controller.drawCard(user, "gold");

        // Verify interactions
        verify(player).drawCard(goldDeck);
        verify(turnHandler).changePlayerState(player);
        verify(user).sendMessage(any(UpdateCardMessage.class));
    }

    @Test
    public void testDrawCardHandlesEmptyDeck() throws Exception {
        // Create local mocks
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> resourceDeck = new LinkedList<>();

        // Create an instance of the class under test
        Controller controller = spy(new Controller(4, 0)); // Replace 'Controller' with the actual class name
        when(controller.getGame()).thenReturn(game);

        // Set up the game board and decks
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getResourceDeck()).thenReturn(resourceDeck);

        // Stub the getPlayerByClient method to return the mocked player
        doReturn(player).when(controller).getPlayerByClient(user);

        // Stub the player's drawCard method to throw EmptyException
        doThrow(new EmptyException()).when(player).drawCard(resourceDeck);

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify that an error message was sent
        verify(user).sendMessage(any(ErrorMessage.class));
    }

    @Test
    public void testDrawCardHandlesNotInTurnException() throws Exception {
        // Create local mocks
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> resourceDeck = new LinkedList<>();

        // Create an instance of the class under test
        Controller controller = spy(new Controller(4, 0)); // Replace 'Controller' with the actual class name
        when(controller.getGame()).thenReturn(game);

        // Set up the game board and decks
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getResourceDeck()).thenReturn(resourceDeck);

        // Stub the getPlayerByClient method to return the mocked player
        doReturn(player).when(controller).getPlayerByClient(user);

        // Stub the player's drawCard method to throw NotInTurnException
        doThrow(new NotInTurnException()).when(player).drawCard(resourceDeck);

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify that an error message was sent
        verify(user).sendMessage(any(ErrorMessage.class));
    }

    @Test
    public void testDrawCardHandlesFullHandException() throws Exception {
        // Create local mocks
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> resourceDeck = new LinkedList<>();

        // Create an instance of the class under test
        Controller controller = spy(new Controller(4, 0)); // Replace 'Controller' with the actual class name
        when(controller.getGame()).thenReturn(game);

        // Set up the game board and decks
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getResourceDeck()).thenReturn(resourceDeck);

        // Stub the getPlayerByClient method to return the mocked player
        doReturn(player).when(controller).getPlayerByClient(user);

        // Stub the player's drawCard method to throw FullHandException
        doThrow(new FullHandException()).when(player).drawCard(resourceDeck);

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify that an error message was sent
        verify(user).sendMessage(any(ErrorMessage.class));
    }
    */
}
//StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
//ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 1);
//GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.NOTHING, 17, new Integer[]{1, 0, 0, 0}, null);
//ResourceCard d = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER) }, 19, 1);
//pp1.addInHand(a);
//pp1.addInHand(d);
//pp1.addInHand(b);
