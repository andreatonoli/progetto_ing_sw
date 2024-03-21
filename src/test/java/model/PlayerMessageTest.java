package model;

import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class PlayerMessageTest extends TestCase {

    private Game game1;
    private Chat chat1;
    private GameBoard board1;
    private Player player1, player2, player3;
    @Test
    @DisplayName ("Test global message first message")
    public void globalMessageFirst() throws IOException {
        String message = "messaggio globale1";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard();
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        player1.sendMessage(true, message);
        //provare con equals altrimenti same
        assertSame(message, player1.getChat().get(0));
        assertSame(message, player2.getChat().get(0));
        assertTrue(player1.getChat().size() == 1);
        assertTrue(player2.getChat().size() == 1);
    }

    @Test
    @DisplayName ("Test global message with full chatlist")
    public void globalMessageFull() throws IOException {
        String message1 = "messaggio globale2";
        String message2 = "messaggio globale2.1";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard();
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        //riempimento della chatlist
        for (int i = 0; i < Chat.CHATDIM; i++){
            player1.sendMessage(true, message1);
        }
        player1.sendMessage(true, message2);
        //provare con equals altrimenti same
        assertSame(message2, player1.getChat().get(0));
        assertSame(message2, player2.getChat().get(0));
        for (int i = 1; i < Chat.CHATDIM; i++){
            assertSame(message1, player1.getChat().get(i));
            assertSame(message1, player2.getChat().get(i));
        }
        assertTrue(player1.getChat().size() == Chat.CHATDIM);
        assertTrue(player2.getChat().size() == Chat.CHATDIM);
    }

    @Test
    @DisplayName ("Test private message first message")
    public void privateMessageFirst() throws IOException {
        String message = "messaggio privato1 per player2";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard();
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        player3 = new Player("peach", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        player3.setGame(game1);
        player1.sendMessage(player2, message);
        assertSame(message, player1.getChat().get(0));
        assertSame(message, player2.getChat().get(0));
        assertTrue(player1.getChat().size() == 1);
        assertTrue(player2.getChat().size() == 1);
        assertNull(player3.getChat());
    }

    @Test
    @DisplayName ("Test private message full chat list")
    public void privateMessageFull() throws IOException {
        String message1 = "messaggio privato2.1 per player2";
        String message2= "messaggio privato2.2 per player2";
        String message3 = "messaggio privato2.3 per player2";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard();
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        player3 = new Player("peach", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        player3.setGame(game1);
        //riempimento parziale della chatlist
        for (int i = 0; i < Chat.CHATDIM - 3; i++){
            player2.sendMessage(true, message1);
        }
        //riempimento totale della chatlist di player 2
        for (int i = 0; i < Chat.CHATDIM - 6; i++){
            player1.sendMessage(player2, message2);
        }
        player1.sendMessage(player2, message3);
        assertSame(message3, player1.getChat().get(0));
        assertSame(message2, player1.getChat().get(1));
        assertSame(message3, player2.getChat().get(0));
        assertSame(message2, player2.getChat().get(1));
        for (int i = 2; i < Chat.CHATDIM; i++){
            assertSame(message1, player1.getChat().get(i));
            assertSame(message1, player2.getChat().get(i));
        }
        for (int i = 0; i < Chat.CHATDIM - 3; i++){
            assertSame(message1, player3.getChat().get(i));
        }
        assertTrue(player1.getChat().size() == Chat.CHATDIM);
        assertTrue(player2.getChat().size() == Chat.CHATDIM);
        assertTrue(player3.getChat().size() == Chat.CHATDIM - 3);
    }

    @Test
    @DisplayName ("Test private message receiver not existing first message")
    public void privateMessageReceiverNotExistingFirst() throws IOException {
        String message = "messaggio privato3 per player3";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard();
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        player3 = new Player("peach", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //player 3 non aggiunto alla partita
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        player1.sendMessage(player3, message);
        assertNull(player1.getChat());
        assertNull(player2.getChat());
        assertNull(player3.getChat());
    }

    @Test
    @DisplayName ("Test private message receiver not existing full chatlist")
    public void privateMessageReceiverNotExistingFull() throws IOException {
        String message1 = "messaggio privato4.1 per player2";
        String message2 = "messaggio privato4.2 per player3";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard();
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        player3 = new Player("peach", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //player 3 non aggiunto alla partita
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        //riempimento della chatlist
        for (int i = 0; i < Chat.CHATDIM - 2; i++){
            player1.sendMessage(player2, message1);
        }
        player1.sendMessage(player3, message2);
        for (int i = 0; i < Chat.CHATDIM - 2; i++){
            assertSame(message1, player1.getChat().get(0));
            assertSame(message1, player2.getChat().get(0));
        }
        assertTrue(player1.getChat().size() == Chat.CHATDIM - 2);
        assertTrue(player1.getChat().size() == Chat.CHATDIM - 2);
        assertNull(player3.getChat());
    }
}
