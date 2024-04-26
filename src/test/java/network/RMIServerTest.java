package network;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import Controller.Controller;
import model.card.*;
import model.enums.Color;
import model.enums.CornerEnum;
import model.enums.PlayerState;
import model.enums.Symbols;
import model.exceptions.AlreadyUsedPositionException;
import model.exceptions.CostNotSatisfiedException;
import model.exceptions.NotInTurnException;
import model.exceptions.OccupiedCornerException;
import model.player.Player;
import network.server.Action;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
public class RMIServerTest {
    //TODO: non è un test vero
    @Test
    @DisplayName("Running test")
    public void provaRun(){
        Action fun = () -> System.out.println("Hello Andrea");
        fun.execute();
    }
}
