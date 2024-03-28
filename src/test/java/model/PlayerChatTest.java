package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

public class PlayerChatTest {

    private final Game game1;
    private final GameBoard board1;
    private Player player1, player2, player3;

    /**
     *
     * @throws IOException
     * Exception for not existing file
     */
    public PlayerChatTest() throws IOException {
        this.game1 = new Game();
        this.board1 = new GameBoard(game1);
    }

    /**
     * This test check that a global message sent as first, is saved for all players
     * in the first cell of chat array.
     */
    @Test
    @DisplayName("Test global first message")
    public void testGlobalFirstMessage() {
        String message = "global message1";
        player1 = new Player("mario", board1, game1);
        player2 = new Player("luigi", board1, game1);
        //player temporary added until fix
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //game set for players temporary until fix
        player1.setGame(game1);
        player2.setGame(game1);
        player1.sendMessage(true, message);

        assertEquals(message, player1.getChat().get(0));
        assertEquals(message, player2.getChat().get(0));
        assertEquals(1, player1.getChat().size());
        assertEquals(1, player2.getChat().size());
    }

    /**
     * This test check that global messages sent to full chat array are saved correctly and
     * another global message sent is saved as newest message in first cell.
     */
    @Test
    @DisplayName ("Test global message with full array chat")
    public void testGlobalMessageFullChat() {
        String message1 = "global message1";
        String message2 = "global message2";
        player1 = new Player("mario", board1, game1);
        player2 = new Player("luigi", board1, game1);
        //player temporary added until fix
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //game set for players temporary until fix
        player1.setGame(game1);
        player2.setGame(game1);
        //fulling array chat for all players
        for (int i = 0; i < Chat.CHATDIM; i++){
            player1.sendMessage(true, message1);
        }
        player1.sendMessage(true, message2);

        assertEquals(message2, player1.getChat().get(0));
        assertEquals(message2, player2.getChat().get(0));
        for (int i = 1; i < Chat.CHATDIM; i++){
            assertEquals(message1, player1.getChat().get(i));
            assertEquals(message1, player2.getChat().get(i));
        }
        assertEquals(Chat.CHATDIM, player1.getChat().size());
        assertEquals(Chat.CHATDIM, player2.getChat().size());
    }

    /**
     * This test check that a private message sent to a player is saved both in the first cell
     * for sender and receiver player, and not saved in another player's array chat.
     */
    @Test
    @DisplayName ("Test private first message")
    public void testPrivateFirstMessage() {
        String message = "private message for player2";
        player1 = new Player("mario", board1, game1);
        player2 = new Player("luigi", board1, game1);
        player3 = new Player("peach", board1, game1);
        //player temporary added until fix
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //game set for players temporary until fix
        player1.setGame(game1);
        player2.setGame(game1);
        player3.setGame(game1);
        player1.sendMessage(player2, message);

        assertEquals(message, player1.getChat().get(0));
        assertEquals(message, player2.getChat().get(0));
        assertEquals(1, player1.getChat().size());
        assertEquals(1, player2.getChat().size());
        assertTrue(player3.getChat().isEmpty());
    }

    /**
     * This test check that global messages and private messages from player1 to player2
     * sent to fill array chat of player1 and player2 are saved correctly after another message
     * sent from player1 to player 2.
     * Array chat of player3 has to contains only global messages.
     */
    @Test
    @DisplayName ("Test private message with full array chat")
    public void privateMessageFullChat() {
        String message1 = "global message1";
        String message2= "private message1 for player2";
        String message3 = "private message2 for player2";
        player1 = new Player("mario", board1, game1);
        player2 = new Player("luigi", board1, game1);
        player3 = new Player("peach", board1, game1);
        //player temporary added until fix
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //game set for players temporary until fix
        player1.setGame(game1);
        player2.setGame(game1);
        player3.setGame(game1);
        //partially fulling array chat for all players
        for (int i = 0; i < Chat.CHATDIM - 2; i++){
            player2.sendMessage(true, message1);
        }
        //fulling array chat for player2
        for (int i = 0; i < Chat.CHATDIM - 5; i++){
            player1.sendMessage(player2, message2);
        }
        player1.sendMessage(player2, message3);

        assertEquals(message3, player1.getChat().get(0));
        assertEquals(message2, player1.getChat().get(1));
        assertEquals(message2, player1.getChat().get(2));
        assertEquals(message3, player2.getChat().get(0));
        assertEquals(message2, player2.getChat().get(1));
        assertEquals(message2, player2.getChat().get(2));
        for (int i = 3; i < Chat.CHATDIM; i++){
            assertEquals(message1, player1.getChat().get(i));
            assertEquals(message1, player2.getChat().get(i));
        }
        for (int i = 0; i < Chat.CHATDIM - 2; i++){
            assertEquals(message1, player3.getChat().get(i));
        }
        assertEquals(Chat.CHATDIM, player1.getChat().size());
        assertEquals(Chat.CHATDIM, player2.getChat().size());
        assertEquals(Chat.CHATDIM - 2, player3.getChat().size());
    }

    /**
     * This test check that a private message sent to a player that is not in game is
     * not saved in any player's array chat.
     */
    @Test
    @DisplayName ("Test private first message with not in game receiver")
    public void privateFirstMessageReceiverNotInGame() {
        String message = "private message1 for not in game player3";
        player1 = new Player("mario", board1, game1);
        player2 = new Player("luigi", board1, game1);
        player3 = new Player("peach", board1, game1);
        //player temporary added until fix
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //game set for players temporary until fix
        player1.setGame(game1);
        player2.setGame(game1);
        player1.sendMessage(player3, message);

        assertTrue(player1.getChat().isEmpty());
        assertTrue(player2.getChat().isEmpty());
        assertTrue(player3.getChat().isEmpty());
    }

    /**
     * This test check that a private message sent to a player that is not in game
     * after private messages sent from player1 to player2, is not saved in any player's
     * array chat.
     * Array chat of player1 and player2 has to contains their private messages.
     */
    @Test
    @DisplayName ("Test private message receiver with full array chat and not in game receiver")
    public void privateMessageReceiverNotInGameFullChat() {
        String message1 = "private message1 for player2";
        String message2 = "private message1 for not in game player3";
        player1 = new Player("mario", board1, game1);
        player2 = new Player("luigi", board1, game1);
        player3 = new Player("peach", board1, game1);
        //player temporary added until fix
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //game set for players temporary until fix
        player1.setGame(game1);
        player2.setGame(game1);
        //fulling array chat for player1 and player2
        for (int i = 0; i < Chat.CHATDIM - 2; i++){
            player1.sendMessage(player2, message1);
        }
        player1.sendMessage(player3, message2);

        for (int i = 0; i < Chat.CHATDIM - 2; i++){
            assertEquals(message1, player1.getChat().get(0));
            assertEquals(message1, player2.getChat().get(0));
        }
        assertEquals(Chat.CHATDIM - 2, player1.getChat().size());
        assertEquals(Chat.CHATDIM - 2, player1.getChat().size());
        assertTrue(player3.getChat().isEmpty());
    }
}
