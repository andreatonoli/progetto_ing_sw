package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.network.server.Server;

public class GameStateMessage extends Message{
    private final GameState state;

    public GameStateMessage(GameState state){
        super(MessageType.GAME_STATE, Server.serverName);
        this.state = state;
    }

    public GameState getState(){
        return state;
    }

    @Override
    public String toString() {
        return "GameStateMessage{" +
                "state: " + state +
                '}';
    }
}
