package network.messages;

import model.enums.PlayerState;
import network.server.Server;

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
