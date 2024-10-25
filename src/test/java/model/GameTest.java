
package model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    /**
     * Checks if the game class constructor correctly initializes all the variables
     */

    @Test
    @DisplayName("Constructor test")
    public void gameConstructorTest(){
        Game game = new Game(3);
        assertEquals(3, game.getLobbySize());
        assertEquals(GameState.WAIT_PLAYERS, game.getGameState());
        assertNotNull(game.getChat());
        assertNotNull(game.getGameBoard());
    }


/**
     * checks if players are correctly added to the lobby
     */

    @Test
    @DisplayName("Add Player")
    public void addPlayerTest(){
        Game game1 = new Game(3);
        Player player1 = new Player("mario", game1);
        Player player2 = new Player("luigi", game1);
        Player player3 = new Player("peach", game1);
        //initially the game is empty
        assertEquals(0, game1.getPlayers().size());
        assertFalse(game1.isFull());
        //adding players
        game1.addPlayer(player1);
        assertEquals(1, game1.getPlayers().size());
        assertFalse(game1.isFull());
        game1.addPlayer(player2);
        assertEquals(2, game1.getPlayers().size());
        assertFalse(game1.isFull());
        //the lobby is now full
        game1.addPlayer(player3);
        assertTrue(game1.isFull());
    }

/**
     * This test check that a game with three players assigns correctly the common cards
     * and the cards of all the players.
     */

    @Test
    @DisplayName("Test game start")
    public void testGameStart() {
        Game game1 = new Game(3);
        Player player1 = new Player("mario", game1);
        Player player2 = new Player("luigi", game1);
        Player player3 = new Player("peach", game1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //Before starting the game is in WAIT_PLAYER state
        assertEquals(GameState.WAIT_PLAYERS, game1.getGameState());
        //start the game
        assertDoesNotThrow(game1::startGame);
        //common achievement are drew and placed on the game board
        assertNotNull(game1.getGameBoard().getCommonAchievement());
        assertEquals(game1.getGameBoard().getCommonAchievement().length, 2);
        //every card in the commonAchievement array is an Achievement that is no more contained in the achievement deck
        for (int i = 0; i < game1.getGameBoard().getCommonAchievement().length; i++){
            assertInstanceOf(Achievement.class, game1.getGameBoard().getCommonAchievement()[i]);
            assertFalse(game1.getGameBoard().getAchievementDeck().contains(game1.getGameBoard().getCommonAchievement()[i]));
        }
        //common resources are drew and placed on the game board
        assertNotNull(game1.getGameBoard().getCommonResource());
        assertEquals(game1.getGameBoard().getCommonResource().length, 2);
        //every card in the commonResources array is a ResourceCard that is no more contained in the resource deck
        for (int i = 0; i < game1.getGameBoard().getCommonResource().length; i++){
            assertEquals("resource", game1.getGameBoard().getCommonResource()[i].getType());
            assertFalse(game1.getGameBoard().getResourceDeck().contains(game1.getGameBoard().getCommonResource()[i]));
        }
        //common gold are drew and placed on the game board
        assertNotNull(game1.getGameBoard().getCommonGold());
        assertEquals(game1.getGameBoard().getCommonGold().length, 2);
        //every card in the commonGold array is a GoldCard that is no more contained in the gold deck
        for (int i = 0; i < game1.getGameBoard().getCommonGold().length; i++){
            assertEquals("gold", game1.getGameBoard().getCommonGold()[i].getType());
            assertFalse(game1.getGameBoard().getGoldDeck().contains(game1.getGameBoard().getCommonGold()[i]));
        }

        ArrayList<Player> playersWithoutP; //contains all players but p
        for (Player p : game1.getPlayers()){
            //every player is given a starterCard that is no more contained in the starterDeck
            assertNotNull(p.getPlayerBoard().getStarterCard());
            assertEquals("starter", p.getPlayerBoard().getStarterCard().getType());
            assertFalse(game1.getGameBoard().getStarterDeck().contains(p.getPlayerBoard().getStarterCard()));
            //two players cannot have the same starterCard
            playersWithoutP = new ArrayList<>(game1.getPlayers());
            playersWithoutP.remove(p);
            for (Player pp : playersWithoutP){
                assertNotEquals(p.getPlayerBoard().getStarterCard(), pp.getPlayerBoard().getStarterCard());
            }
            //every player has 3 cards in hand
            assertNotNull(p.getCardInHand());
            assertEquals(p.getCardInHand().length, 3);
            //the first two cards are resource cards
            for (int i = 0; i < p.getCardInHand().length - 1; i++){
                assertEquals("resource", p.getCardInHand()[i].getType());
                assertFalse(game1.getGameBoard().getResourceDeck().contains(p.getCardInHand()[i]));
                //two players cannot have the same card in hand
                playersWithoutP = new ArrayList<>(game1.getPlayers());
                playersWithoutP.remove(p);
                for (Player pp : playersWithoutP){
                    for(Card c : pp.getCardInHand()){
                        assertNotEquals(p.getCardInHand()[i], c);
                    }
                }
            }
            //The third card in hand is a gold card
            assertEquals("gold", p.getCardInHand()[2].getType());
            assertFalse(game1.getGameBoard().getGoldDeck().contains(p.getCardInHand()[2]));
            //two players cannot have the same gold card in hand
            playersWithoutP = new ArrayList<>(game1.getPlayers());
            playersWithoutP.remove(p);
            for (Player pp : playersWithoutP){
                for(Card c : pp.getCardInHand()){
                    assertNotEquals(p.getCardInHand()[2], c);
                }
            }
            //every player could choose between two achievements
            assertNotNull(p.getPersonalObj());
            assertEquals(p.getPersonalObj().length, 2);
            //those achievements are not contained in the achievement deck and are different for every player
            for (int i = 0; i < p.getPersonalObj().length; i++){
                assertInstanceOf(Achievement.class, p.getPersonalObj()[i]);
                assertFalse(game1.getGameBoard().getAchievementDeck().contains(p.getPersonalObj()[i]));
                playersWithoutP = new ArrayList<>(game1.getPlayers());
                playersWithoutP.remove(p);
                for (Player pp : playersWithoutP){
                    for(Achievement c : pp.getPersonalObj()){
                        assertNotEquals(p.getCardInHand()[i], c);
                    }
                }
            }
        }

        assertNotNull(game1.getFirstPlayer());
        assertTrue(game1.getPlayers().contains(game1.getFirstPlayer()));
        //Checking that there's only one player that is the first to play
        assertTrue(game1.getFirstPlayer().isFirstToPlay());
        ArrayList<Player> otherPlayers = new ArrayList<>(game1.getPlayers());
        otherPlayers.remove(game1.getFirstPlayer());
        for (Player p : otherPlayers){
            assertFalse(p.isFirstToPlay());
        }

    }


/**
     * this test checks that a game with less player than its capacity does not start until the lobby is full
     */

    @Test
    @DisplayName("Game does not start unless it's full")
    public void fewPlayerTest(){
        Game game1 = new Game(3);
        Player player1 = new Player("mario", game1);
        Player player2 = new Player("luigi", game1);
        Player player3 = new Player("peach", game1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //lobby size = 3 but only 2 players have joined
        assertThrows(NotEnoughPlayersException.class, game1::startGame);
        //fill the lobby and start the game
        game1.addPlayer(player3);
        assertDoesNotThrow(game1::startGame);
    }


/**
     * this test checks that a game with three players that placed some cards
     * to make all types of achievements is ended with the correct number
     * of points for each player
     */

    @Test
    @DisplayName("Test game end")
    public void testGameEnd() {
        Game game1 = new Game(3);
        Player player1 = new Player("mario", game1);
        Player player2 = new Player("luigi", game1);
        Player player3 = new Player("peach", game1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        assertDoesNotThrow(game1::startGame);
        //Manually setting game's state to IN_GAME (mocking turn_handler's task)
        game1.setGameState(GameState.IN_GAME);
        //3 points
        AchievementL a1 = new AchievementL(Color.RED, 1);
        //3 points
        AchievementItem a2 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT)), 14);
        //2 points
        AchievementDiagonal a3 = new AchievementDiagonal(Color.PURPLE, 5);
        //2 points
        AchievementResources a4 = new AchievementResources(Symbols.ANIMAL, 8);
        //3 points
        AchievementL a5 = new AchievementL(Color.GREEN, 2);
        //set the common achievements
        game1.getGameBoard().setCommonAchievement(a1, 0);
        game1.getGameBoard().setCommonAchievement(a2, 1);
        //give each player his personal achievement
        player1.setChosenObj(a3);
        player2.setChosenObj(a4);
        player3.setChosenObj(a5);
        //place every player's starter card on the table
        for (Player p : game1.getPlayers()){
            p.getPlayerBoard().setStarterCard(p.getPlayerBoard().getStarterCard());
        }
        //player1 places his cards
        player1.setPlayerState(PlayerState.PLAY_CARD);
        Card r2 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 2, Color.RED);
        Card r3 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI), new Corner(Symbols.FUNGI) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 3, Color.RED);
        Card g11 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 11, Color.GREEN);
        try {
            player1.placeCard(r2, new int[]{1,1});
            player1.placeCard(r3, new int[]{1,-1});
            player1.placeCard(g11, new int[]{2,-2});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
            System.out.println(ex.getMessage());
        }
        Card p31 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 31, Color.PURPLE);
        Card p32 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT), new Corner(Symbols.INSECT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 32, Color.PURPLE);
        Card p33 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.INSECT), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY), new Corner(Symbols.INSECT) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 33, Color.PURPLE);
        try {
            player1.placeCard(p31, new int[]{-1,1});
            player1.placeCard(p33, new int[]{-2,2});
            player1.placeCard(p32, new int[]{-3,3});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
            System.err.println(ex.getMessage());
        }
        //player1 made a1 and a3 achievements (total 5 points)
        player1.setPlayerState(PlayerState.NOT_IN_TURN);
        //player2 can now place his cards
        player2.setPlayerState(PlayerState.PLAY_CARD);
        Card r6 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.INKWELL), new Corner(Symbols.FUNGI), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 6, Color.RED);
        Card g15 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 15, Color.GREEN);
        Card b26 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 26, Color.BLUE);
        try {
            player2.placeCard(r6, new int[]{1,1});
            player2.placeCard(g15, new int[]{2,0});
            player2.placeCard(b26, new int[]{3,-1});
        } catch (OccupiedCornerException | NotInTurnException | AlreadyUsedPositionException | CostNotSatisfiedException | InvalidCoordinatesException ex) {
            System.out.println(ex.getMessage());
        }
        Card b21 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 21, Color.BLUE);
        try {
            player2.placeCard(b21, new int[]{-1,1});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
            System.out.println(ex.getMessage());
        }
        //player2 made a2 and a4 achievements (total 5 points)
        player2.setPlayerState(PlayerState.NOT_IN_TURN);
        //player3 can now place his cards
        player3.setPlayerState(PlayerState.PLAY_CARD);
        Card g12 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 12, Color.GREEN);
        Card g13 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 13, Color.GREEN);
        Card p34 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.ANIMAL), new Corner(Symbols.NOCORNER), new Corner(Symbols.EMPTY) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 34, Color.PURPLE);
        try {
            player3.placeCard(g12, new int[]{1,1});
            player3.placeCard(g13, new int[]{1,-1});
            player3.placeCard(p34, new int[]{0,-2});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException ex) {
            System.out.println(ex.getMessage());
        } catch (AlreadyUsedPositionException | InvalidCoordinatesException e) {
            throw new RuntimeException(e);
        }
        Card b25 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INKWELL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 25, Color.BLUE);
        Card g17 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.MANUSCRIPT), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 27, Color.GREEN);
        Card p35 = new Card( new ResourceCard(new Corner[]{new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL), new Corner(Symbols.INSECT), new Corner(Symbols.ANIMAL) }, 0), new CardBack(List.of(Symbols.FUNGI)), "resource", 35, Color.PURPLE);
        try {
            player3.placeCard(g17, new int[]{-1,1});
            player3.placeCard(b25, new int[]{-2,0});
            player3.placeCard(p35, new int[]{-1,-1});
        } catch (OccupiedCornerException | NotInTurnException | CostNotSatisfiedException |
                 AlreadyUsedPositionException | InvalidCoordinatesException ex) {
            System.out.println(ex.getMessage());
        }
        //player3 made a2 and a5 achievements (total 6 points)
        try {
            game1.endGame();
        } catch (GameNotStartedException e) {
            throw new RuntimeException(e);
        }

        //Player1 made 5 points by completing 2 objectives
        assertEquals(5, player1.getPoints());
        assertEquals(2, player1.getObjCompleted());
        //Player2 made 5 points by completing 2 objectives
        assertEquals(5, player2.getPoints());
        assertEquals(2, player2.getObjCompleted());
        //Player3 made 6 points by completing 2 objectives
        assertEquals(6, player3.getPoints());
        assertEquals(2, player3.getObjCompleted());
    }


/**
     * this test checks that a game with three players that placed some cards
     * to make all types of achievements but without calling startGame(4) method
     * is ended with the GameNotStartedException and with no points added
     * to each player
     */

    @Test
    @DisplayName("Test game end without starting before")
    public void testGameEndWithoutStarting() {
        Game game1 = new Game(3);
        Player player1 = new Player("mario", game1);
        Player player2 = new Player("luigi", game1);
        Player player3 = new Player("peach", game1);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //trying to finish the game without starting it. Players aren't given any points
        assertThrows(GameNotStartedException.class, game1::endGame);

        assertEquals(0, player1.getPoints());
        assertEquals(0, player2.getPoints());
        assertEquals(0, player3.getPoints());
    }

    @Test
    @DisplayName("Disconnection Test")
    public void disconnectionTest() {
        Game game = new Game(4);
        Player a = new Player("Paolo", game);
        Player b = new Player("Andrea", game);
        Player c = new Player("Carlo", game);
        Player d = new Player("Matteo", game);

        //Add player to game
        game.addPlayer(a);
        game.addPlayer(b);
        game.addPlayer(c);
        game.addPlayer(d);

        //Check if the lobby is full
        assertEquals(4, game.getPlayers().size());
        assertTrue(game.isFull());

        //Remove the first player
        game.removePlayer("Paolo");
        game.addDisconnections(1);
        assertEquals(3, game.getPlayers().size());
        assertEquals(1, game.getDisconnections());
        //Check if the player was removed
        for (Player p : game.getPlayers()){
            assertNotEquals(a.getUsername(), p.getUsername());
        }

        //Remove other 2 players
        game.removePlayer("Andrea");
        game.removePlayer("Carlo");
        game.addDisconnections(2);
        assertEquals(1, game.getPlayers().size());
        assertEquals(3, game.getDisconnections());

        //One player remained -> end game by disconnection
        game.endGameByDisconnection();

        assertEquals(GameState.END, game.getGameState());
    }
}

