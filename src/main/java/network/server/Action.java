package network.server;

import java.util.function.Consumer;

@FunctionalInterface
public interface Action {
   void execute();
}
