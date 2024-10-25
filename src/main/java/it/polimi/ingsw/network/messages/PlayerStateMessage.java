package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.network.server.Server;

/**
 * Message used to send the state of a player to the client.
 */
public class PlayerStateMessage extends Message{
    private final PlayerState playerState;
    private final String name;
    public PlayerStateMessage(PlayerState playerState, String username){
        super(MessageType.PLAYER_STATE, Server.serverName);
        this.playerState=playerState;
        this.name=username;
    }

    public PlayerState getState(){
        return playerState;
    }
    public String getName() {
        return name;
    }

    public String toString(){
        return "PlayerStateMessage{"+
                "state: " + this.playerState +
                "name: " + this.name+
                '}';
    }

}
