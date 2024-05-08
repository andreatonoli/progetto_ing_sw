package network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RMIServerTest {
    //TODO: non è un test vero
    @Test
    @DisplayName("Running test")
    public void provaRun(){
        //spy(), mock(), verify()
        //Server server = mock(new Server());
        //Controller co = spy(new Controller(4));
        //verify(server, )
        new GameBoard(new Game(4));
    }
}
