package it.polimi.ingsw.network.server;

import java.util.function.Consumer;

@FunctionalInterface
public interface Action {
   void execute();
}
