package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TurnHandler;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.server.Connection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@ExtendWith(MockitoExtension.class)
public class ControllerTest {
    @Test
    @DisplayName("Choose valid Objective")
    public void validChooseObjTest() {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Achievement chosenAchievement = new AchievementDiagonal(Color.RED, 1);
        Player player = mock(Player.class);
        Achievement[] personalObj = new Achievement[2];
        personalObj[0] = new AchievementDiagonal(Color.RED, 1);
        personalObj[1] = new AchievementDiagonal(Color.PURPLE, 4);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Stubbing methods
        doReturn(player).when(controller).getPlayerByClient(user);
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
    public void invalidChooseObjTest() {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        Achievement[] personalObj = new Achievement[2];
        personalObj[0] = new AchievementDiagonal(Color.RED, 1);
        personalObj[1] = new AchievementDiagonal(Color.PURPLE, 4);

        //Creating achievement with same id but different color
        Achievement chosenAchievement = new AchievementDiagonal(Color.BLUE, 1);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 7));

        // Stubbing methods
        doReturn(player).when(controller).getPlayerByClient(user);
        when(player.getPersonalObj()).thenReturn(personalObj);

        // Ensure that the achievement is not one of the player's personal objectives
        assertFalse(chosenAchievement.equals(personalObj[0]) && chosenAchievement.equals(personalObj[1]));

        // Call the method to be tested
        controller.chooseObj(user, chosenAchievement);

        // Verify that setChosenObj was not called
        verify(player, never()).setChosenObj(chosenAchievement);
    }

    @Test
    @DisplayName("Draw from resource deck")
    public void drawFromResourceTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> resourceDeck = new LinkedList<>();
        Card drawedCard = mock(Card.class);

        // Ensure the resourceDeck is mocked correctly
        resourceDeck.add(drawedCard);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.drawCard(any())).thenReturn(drawedCard);

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify interactions
        verify(player).drawCard(any(LinkedList.class));
        verify(turnHandler).changePlayerState(player);
        verify(user).sendMessage(any(UpdateCardMessage.class));
        verify(controller).notifyAll(any(UpdateDeckMessage.class));
    }

    @Test
    @DisplayName("Draw from gold deck")
    public void drawFromGoldDeckTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        LinkedList<Card> goldDeck = new LinkedList<>();
        Card drawedCard = mock(Card.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Add a card to the deck
        goldDeck.add(drawedCard);

        // Stubbing methods
        doReturn(player).when(controller).getPlayerByClient(user);
        when(player.drawCard(any())).thenReturn(drawedCard);

        // Call the method to be tested
        controller.drawCard(user, "gold");

        // Verify interactions
        verify(player).drawCard(any());
        verify(turnHandler).changePlayerState(player);
        verify(user).sendMessage(any(UpdateCardMessage.class));
    }

    @Test
    @DisplayName("Draw from empty deck")
    public void drawCardFromEmptyDeck() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        doReturn(player).when(controller).getPlayerByClient(user);
        doThrow(new EmptyException()).when(player).drawCard(any());

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify that an error message was sent
        verify(user).sendMessage(any(ErrorMessage.class));
    }

    @Test
    @DisplayName("Draw card when not in turn")
    public void drawCardWhenNotInTurnTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        doReturn(player).when(controller).getPlayerByClient(user);
        doThrow(new NotInTurnException()).when(player).drawCard(any());

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify that an error message was sent
        verify(user).sendMessage(any(ErrorMessage.class));
    }

    @Test
    @DisplayName("Draw card when hand is full")
    public void drawCardWhenFullHandTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        doReturn(player).when(controller).getPlayerByClient(user);
        doThrow(new FullHandException()).when(player).drawCard(any());

        // Call the method to be tested
        controller.drawCard(user, "resource");

        // Verify that an error message was sent
        verify(user).sendMessage(any(ErrorMessage.class));
    }

    @Test
    @DisplayName("Place a card that does not belong to player")
    public void placeCardCardNotInHandTest() {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        Card card = mock(Card.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.getCardInHand()).thenReturn(new Card[]{mock(Card.class), mock(Card.class), mock(Card.class)});

        // Invoke the method to be tested
        controller.placeCard(user, card, new int[]{0, 0});

        // Capture and verify messages sent to user
        ArgumentCaptor<GenericMessage> genericMessageCaptor = ArgumentCaptor.forClass(GenericMessage.class);
        verify(user).sendMessage(any(CardInHandMessage.class));
        verify(user).sendMessage(genericMessageCaptor.capture());

        // Assert the captured message
        assertEquals("\nThis card does not belong to your hand.\n", genericMessageCaptor.getValue().toString());
    }

    @Test
    @DisplayName("Place a card")
    public void placeCardTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        Card card = mock(Card.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        Card[] hand = new Card[]{mock(Card.class), mock(Card.class), mock(Card.class)};

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        when(hand[0].equals(card)).thenReturn(true);
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.getCardInHand()).thenReturn(hand);

        // Invoke the method to be tested
        controller.placeCard(user, card, new int[]{0, 0});

        // Verify interactions
        verify(player).placeCard(card, new int[]{0, 0});
        verify(turnHandler).changePlayerState(player);
        verify(controller, times(2)).notifyAll(any());
    }

    @Test
    @DisplayName("Invalid placement")
    public void invalidPlacementTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        Card card = mock(Card.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        Card[] hand = new Card[]{mock(Card.class), mock(Card.class), mock(Card.class)};

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        when(hand[0].equals(card)).thenReturn(true);
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.getCardInHand()).thenReturn(hand);
        doThrow(new InvalidCoordinatesException()).when(player).placeCard(card, new int[]{0, 0});

        // Invoke the method to be tested
        controller.placeCard(user, card, new int[]{0, 0});

        // Verify interactions
        verify(user).sendMessage(any(UpdateCardMessage.class));
        verify(user).sendMessage(any(ErrorMessage.class));
    }


    @Test
    @DisplayName("Draw from common resources")
    public void drawCardFromCommonResourceTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Card card = mock(Card.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.drawCardFromBoard(any())).thenReturn(card);

        // Call the method with index 0 (common resource)
        controller.drawCardFromBoard(user, 0);

        // Verify interactions and state changes
        verify(player).drawCardFromBoard(any());
        verify(user).sendMessage(any(UpdateCardMessage.class));
        verify(controller).notifyAll(any(CommonCardUpdateMessage.class));
        verify(controller).notifyAll(any(UpdateDeckMessage.class));
        verify(turnHandler).changePlayerState(player);
    }

    @Test
    @DisplayName("Draw from common gold")
    public void drawCardFromCommonGoldTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Card card = mock(Card.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.drawCardFromBoard(any())).thenReturn(card);

        // Call the method with index 2 (common gold)
        controller.drawCardFromBoard(user, 2);

        // Verify interactions and state changes
        verify(player).drawCardFromBoard(any());
        verify(user).sendMessage(any(UpdateCardMessage.class));
        verify(controller).notifyAll(any(CommonCardUpdateMessage.class));
        verify(controller).notifyAll(any(UpdateDeckMessage.class));
        verify(turnHandler).changePlayerState(player);
    }

    @Test
    @DisplayName("Trying to draw from invalid index")
    public void drawCardFromBoardInvalidIndexTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Call the method with an invalid index (greater than 3)
        controller.drawCardFromBoard(user, 4);

        // Verify the user is notified with an error message
        verify(user).sendMessage(any(GenericMessage.class));
        verify(player, never()).drawCardFromBoard(any(Card.class));
        verify(turnHandler, never()).changePlayerState(any(Player.class));
    }

    @Test
    @DisplayName("Exception in drawCardFromBoard")
    public void drawCardFromBoardExceptionTest() throws Exception {
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.drawCardFromBoard(any())).thenThrow(new CardNotFoundException());

        // Call the method with index 0 (common resource)
        controller.drawCardFromBoard(user, 0);

        // Verify the user is notified with an error message
        verify(user).sendMessage(any(ErrorMessage.class));
    }

    @Test
    @DisplayName("Join a not full game")
    public void joinGameNotFullTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field turnHandelerField = Controller.class.getDeclaredField("turnHandler");
        turnHandelerField.setAccessible(true);
        turnHandelerField.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Stubbing method
        when(user.getUsername()).thenReturn("mario");

        // Call the method
        boolean result = controller.joinGame(user);

        // Verify interactions and state changes
        assertFalse(result);
        verify(user).setLobby(controller);
        verify(game).addPlayer(any(Player.class));
        verify(controller).notifyAll(any(GenericMessage.class));
        verify(controller, never()).startGame();
        verify(controller, never()).notifyAll(any(GameStateMessage.class));
    }

    @Test
    @DisplayName("Join a full game")
    public void joinGameFullTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Game game = mock(Game.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Stubbing methods
        when(user.getUsername()).thenReturn("luigi");
        when(game.isFull()).thenReturn(false).thenReturn(true);
        doNothing().when(controller).startGame();

        // Call the method
        boolean result = controller.joinGame(user);

        // Verify interactions and state changes
        assertTrue(result);
        verify(user).setLobby(controller);
        verify(game).addPlayer(any(Player.class));
        verify(controller).notifyAll(any(GenericMessage.class));
        verify(controller).startGame();
        verify(controller).notifyAll(any(GameStateMessage.class));
        verify(game).setGameState(GameState.START);
    }

    @Test
    @DisplayName("Start game")
    public void startGameTest() throws Exception {
        // Mocking dependencies
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        Card resourceCard1 = mock(Card.class);
        Card resourceCard2 = mock(Card.class);
        Card goldCard1 = mock(Card.class);
        Card goldCard2 = mock(Card.class);
        Achievement achievement1 = mock(Achievement.class);
        Achievement achievement2 = mock(Achievement.class);
        Card starterCard1 = mock(Card.class);
        Card starterCard2 = mock(Card.class);
        PlayerBoard playerBoard1 = mock(PlayerBoard.class);
        PlayerBoard playerBoard2 = mock(PlayerBoard.class);
        LinkedList<Card> resourceDeck = mock(LinkedList.class);
        LinkedList<Card> goldDeck = mock(LinkedList.class);
        Player firstPlayer = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        Connection user1 = mock(Connection.class);
        Connection user2 = mock(Connection.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        ConcurrentHashMap<Connection, Player> connectedPlayers = new ConcurrentHashMap<>();
        connectedPlayers.put(user1, player1);
        connectedPlayers.put(user2, player2);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4,0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Using reflection to set the internal connectedPlayers map since it's created in the constructor
        java.lang.reflect.Field connectedPlayersField = Controller.class.getDeclaredField("connectedPlayers");
        connectedPlayersField.setAccessible(true);
        connectedPlayersField.set(controller, connectedPlayers);

        // Stubbing methods
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getCommonResource()).thenReturn(new Card[]{resourceCard1, resourceCard2});
        when(gameBoard.getCommonGold()).thenReturn(new Card[]{goldCard1, goldCard2});
        when(gameBoard.getCommonAchievement()).thenReturn(new Achievement[]{achievement1, achievement2});
        when(gameBoard.getResourceDeck()).thenReturn(resourceDeck);
        when(gameBoard.getGoldDeck()).thenReturn(goldDeck);
        when(resourceDeck.getFirst()).thenReturn(resourceCard1);
        when(goldDeck.getFirst()).thenReturn(goldCard1);
        when(resourceCard1.getColor()).thenReturn(Color.BLUE);
        when(goldCard1.getColor()).thenReturn(Color.PURPLE);
        when(game.getFirstPlayer()).thenReturn(firstPlayer);
        when(firstPlayer.getUsername()).thenReturn("Player1");
        when(user1.getUsername()).thenReturn("Player1");
        when(user2.getUsername()).thenReturn("Player2");
        when(player1.getPlayerBoard()).thenReturn(playerBoard1);
        when(player2.getPlayerBoard()).thenReturn(playerBoard2);
        when(playerBoard1.getStarterCard()).thenReturn(starterCard1);
        when(playerBoard2.getStarterCard()).thenReturn(starterCard2);
        when(player1.getCardInHand()).thenReturn(new Card[]{mock(Card.class), mock(Card.class), mock(Card.class)});
        when(player2.getCardInHand()).thenReturn(new Card[]{mock(Card.class), mock(Card.class), mock(Card.class)});
        when(player1.getPersonalObj()).thenReturn(new Achievement[]{mock(Achievement.class), mock(Achievement.class)});
        when(player2.getPersonalObj()).thenReturn(new Achievement[]{mock(Achievement.class), mock(Achievement.class)});

        // Mocking player methods
        when(controller.getPlayerByClient(user1)).thenReturn(player1);
        when(controller.getPlayerByClient(user2)).thenReturn(player2);

        // Call the method
        controller.startGame();

        // Verify that game.startGame() was called
        verify(game).startGame();

        // Verify that the notifyAll methods were called with correct messages
        verify(controller).notifyAll(any(OpponentsMessage.class));
        verify(controller, times(4)).notifyAll(any(CommonCardUpdateMessage.class));
        verify(controller).notifyAll(any(AchievementMessage.class));
        verify(controller, times(2)).notifyAll(any(UpdateDeckMessage.class));

        // Verify player-specific messages
        verify(user1).sendMessage(any(StarterCardMessage.class));
        verify(user1).sendMessage(any(CardInHandMessage.class));
        verify(user1).sendMessage(any(AchievementMessage.class));
        verify(user2).sendMessage(any(StarterCardMessage.class));
        verify(user2).sendMessage(any(CardInHandMessage.class));
        verify(user2).sendMessage(any(AchievementMessage.class));

        // Verify player state messages
        verify(controller, times(2)).notifyAll(any(PlayerStateMessage.class));

        // Verify pickQueue is called
        verify(controller).pickQueue();
    }

    @Test
    @DisplayName("Trying to start a game without enough players")
    public void startGameNotEnoughPlayersTest() throws Exception {
        // Mocking dependencies
        Game game = mock(Game.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4,0));

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Stubbing method to throw NotEnoughPlayersException
        doThrow(new NotEnoughPlayersException()).when(game).startGame();

        // Call the method
        controller.startGame();

        // Verify that game.startGame() was called and exception was caught
        verify(game).startGame();
    }

    @Test
    @DisplayName("Place starter card")
    public void placeStarterCardTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Card starterCard = mock(Card.class);
        Player player = mock(Player.class);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        Game game = mock(Game.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4,0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.getPlayerBoard()).thenReturn(playerBoard);
        when(playerBoard.getStarterCard()).thenReturn(starterCard);
        when(starterCard.equals(any())).thenReturn(true);

        // Call the method
        controller.placeStarterCard(user, starterCard);

        // Verify that the starter card was set correctly
        verify(playerBoard).setStarterCard(any(Card.class));
        // Verify that no error messages were sent
        verify(user, never()).sendMessage(any(GenericMessage.class));
        verify(user, never()).sendMessage(any(StarterCardMessage.class));
    }

    @Test
    @DisplayName("Trying to place a starter card that does not belong to player")
    public void placeStarterCardExceptionTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Card starterCard = mock(Card.class);
        Card incorrectCard = mock(Card.class);
        Player player = mock(Player.class);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        Game game = mock(Game.class);
        TurnHandler turnHandler = mock(TurnHandler.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Stubbing methods
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.getPlayerBoard()).thenReturn(playerBoard);
        when(playerBoard.getStarterCard()).thenReturn(incorrectCard);

        // Call the method with an incorrect starter card
        controller.placeStarterCard(user, starterCard);

        // Verify that the starter card was not set
        verify(playerBoard, never()).setStarterCard(starterCard);
        // Verify that error messages were sent
        verify(user).sendMessage(any(StarterCardMessage.class));
        verify(user).sendMessage(any(GenericMessage.class));
    }


    @Test
    @DisplayName("Set color")
    public void setColorTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        Game game = mock(Game.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Stubbing methods
        when(game.getAvailableColors()).thenReturn(new ArrayList<>(List.of(Color.RED, Color.BLUE, Color.GREEN)));
        when(controller.getPlayerByClient(user)).thenReturn(player);
        when(player.getPlayerBoard()).thenReturn(playerBoard);
        when(turnHandler.changeSetupPlayer()).thenReturn("NextPlayer"); // Simulate changing setup player

        // Call the method with a valid color
        controller.setColor(user, Color.RED);

        // Verify interactions and state changes
        verify(player).setPionColor(any(Color.class));
        verify(controller).notifyAll(any(ColorResponseMessage.class));
        verify(controller).notifyAll(any(PlayerBoardUpdateMessage.class));
        verify(turnHandler).changeSetupPlayer(); // Ensure turn handler was called

        // Verify game state changes when setup is finished
        verify(game, never()).setGameState(GameState.IN_GAME);
        verify(controller, never()).notifyAll(new GameStateMessage(GameState.IN_GAME));
    }

    @Test
    @DisplayName("Set color - invalid color")
    public void setColorExceptionTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Player player = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        Game game = mock(Game.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Call the method with an invalid color
        controller.setColor(user, Color.YELLOW);

        // Verify interactions and state changes
        verify(player, never()).setPionColor(any(Color.class)); // Player color not set
        verify(controller).chooseColor(user); // Choose color method called
        verify(user).sendMessage(any(ErrorMessage.class));
        verify(controller, never()).notifyAll(any(ColorResponseMessage.class)); // No color response sent
        verify(controller, never()).notifyAll(any(PlayerBoardUpdateMessage.class)); // No player board update sent
        verify(turnHandler, never()).changeSetupPlayer(); // Turn handler not called
    }

    @Test
    @DisplayName("Reconnect")
    public void reconnectTest() throws Exception{
        // Mocking dependencies
        Connection user = mock(Connection.class);
        Connection opponentConnection1 = mock(Connection.class);
        Connection opponentConnection2 = mock(Connection.class);
        Player reconnectedPlayer = mock(Player.class);
        Player opponent1 = mock(Player.class);
        Player opponent2 = mock(Player.class);
        TurnHandler turnHandler = mock(TurnHandler.class);
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        PlayerBoard playerBoard1 = mock(PlayerBoard.class);
        LinkedList<Card> resourceDeck = mock(LinkedList.class);
        LinkedList<Card> goldDeck = mock(LinkedList.class);
        Card resourceCard1 = mock(Card.class);
        Card goldCard1 = mock(Card.class);

        // Create an instance of Controller
        Controller controller = spy(new Controller(4, 0));

        // Using reflection to set the internal turnHandler since it's created in the constructor
        java.lang.reflect.Field field = Controller.class.getDeclaredField("turnHandler");
        field.setAccessible(true);
        field.set(controller, turnHandler);

        // Using reflection to set the internal game since it's created in the constructor
        java.lang.reflect.Field gameField = Controller.class.getDeclaredField("game");
        gameField.setAccessible(true);
        gameField.set(controller, game);

        // Mocking connectedPlayers map
        ConcurrentHashMap<Connection, Player> connectedPlayers = new ConcurrentHashMap<>();
        connectedPlayers.put(opponentConnection1, opponent1);
        connectedPlayers.put(user, reconnectedPlayer);
        connectedPlayers.put(opponentConnection2, opponent2);

        // Using reflection to set the internal connectedPlayers map since it's created in the constructor
        java.lang.reflect.Field connectedPlayersField = Controller.class.getDeclaredField("connectedPlayers");
        connectedPlayersField.setAccessible(true);
        connectedPlayersField.set(controller, connectedPlayers);

        // Stubbing methods
        when(user.getUsername()).thenReturn("ReconnectedPlayer");
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(controller.getPlayerByClient(user)).thenReturn(reconnectedPlayer);
        when(controller.getPlayerByClient(opponentConnection1)).thenReturn(opponent1);
        when(controller.getPlayerByClient(opponentConnection2)).thenReturn(opponent2);
        when(opponentConnection1.getUsername()).thenReturn("mario");
        when(opponentConnection2.getUsername()).thenReturn("luigi");
        //Stubbing model to allow controller to send the message
        when(gameBoard.getCommonResource()).thenReturn(new Card[]{mock(Card.class), mock(Card.class)});
        when(gameBoard.getCommonGold()).thenReturn(new Card[]{mock(Card.class), mock(Card.class)});
        when(gameBoard.getCommonAchievement()).thenReturn(new Achievement[]{mock(Achievement.class), mock(Achievement.class)});
        when(gameBoard.getResourceDeck()).thenReturn(resourceDeck);
        when(gameBoard.getGoldDeck()).thenReturn(goldDeck);
        when(resourceDeck.getFirst()).thenReturn(resourceCard1);
        when(goldDeck.getFirst()).thenReturn(goldCard1);
        when(resourceCard1.getColor()).thenReturn(Color.BLUE);
        when(goldCard1.getColor()).thenReturn(Color.PURPLE);
        when(user.getUsername()).thenReturn("Player1");
        when(reconnectedPlayer.getPlayerBoard()).thenReturn(playerBoard1);
        when(reconnectedPlayer.getCardInHand()).thenReturn(new Card[]{mock(Card.class), mock(Card.class), mock(Card.class)});

        // Call the method
        controller.reconnectBackup(user);

        // Verify interactions and state changes
        verify(controller, atLeastOnce()).removeObserver(any(Connection.class)); // Verify removeObserver called for old connection
        verify(controller, atLeastOnce()).addObserver(user); // Verify addObserver called for new connection
        verify(turnHandler, atLeastOnce()).removeObserver(any(Connection.class)); // Verify turnHandler removeObserver called for old connection
        verify(turnHandler, atLeastOnce()).addObserver(user); // Verify turnHandler addObserver called for new connection
        verify(reconnectedPlayer, atLeastOnce()).setDisconnected(false); // Verify player's disconnected status set to false

        // Verify reconnection message sent to user
        verify(user, atLeastOnce()).sendMessage(any(ReconnectionMessage.class));
    }
}
