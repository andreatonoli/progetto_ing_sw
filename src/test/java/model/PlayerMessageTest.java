package model;

import junit.framework.TestCase;
import model.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PlayerMessageTest extends TestCase {

    private Game game1;
    private Chat chat1;
    private GameBoard board1;
    private Player player1, player2;
    @Test
    public void globalMessage() throws IOException, PlayerNotFoundException {
        String message = "messaggio globale";
        game1 = new Game();
        chat1 = new Chat(game1);
        board1 = new GameBoard(game1);
        player1 = new Player("mario", board1);
        player2 = new Player("luigi", board1);
        //aggiunta tempornaea dei giocatori alla partita finchè non gestita diversamente
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        //temporanei finchè non viene gestito login + creazione game da parte del player
        player1.setGame(game1);
        player2.setGame(game1);
        player1.sendMessage(true, message);
    }
}
