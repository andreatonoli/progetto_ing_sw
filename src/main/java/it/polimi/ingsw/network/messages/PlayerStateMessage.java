package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.network.server.Server;

public class PlayerStateMessage extends Message{
    private final PlayerState playerState;
    public PlayerStateMessage(PlayerState playerState){
        super(MessageType.PLAYER_STATE, Server.serverName);
        this.playerState=playerState;
    }

    public PlayerState getState(){
        return playerState;
    }

    public String toString(){
        return "PlayerStateMessage{"+
                "state: " + this.playerState +
                '}';
    }

}
