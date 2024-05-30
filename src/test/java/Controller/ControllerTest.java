package Controller;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.enums.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.SocketConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ControllerTest {

    @Mock
    private SocketConnection p1;

    //@Test
    //public void testChooseObj() {
    //    Controller c = new Controller(4);
    //    // Create one fake player
    //    c.joinGame(p1);
    //    // Assigning the player linked to p1
    //    Player pp1 = c.getPlayerByClient(p1);
    //    // Give p1's achievement
    //    Achievement a = new AchievementDiagonal(Color.PURPLE, 1);
    //    Achievement b = new AchievementDiagonal(Color.RED, 2);
    //    // Setting those achievements as pp1's personal objectives
    //    pp1.getPersonalObj()[0] = a;
    //    pp1.getPersonalObj()[1] = b;
    //    // p1 chooses a
    //    c.chooseObj(p1, a);
    //    assertEquals(a, pp1.getChosenObj());
    //}
}
//StarterCard s = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
//ResourceCard a = new ResourceCard(new Corner[]{new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.FUNGI) }, 1, 1);
//GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.NOTHING, 17, new Integer[]{1, 0, 0, 0}, null);
//ResourceCard d = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.PLANT), new Corner(Symbols.NOCORNER) }, 19, 1);
//pp1.addInHand(a);
//pp1.addInHand(d);
//pp1.addInHand(b);
