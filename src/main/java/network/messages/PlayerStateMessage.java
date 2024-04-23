package network.messages;

import model.PlayerState;
import network.server.Server;

public class PlayerStateMessage extends Message{
    private PlayerState playerState;
    public PlayerStateMessage(PlayerState playerState){
        super(MessageType.PLAYER_STATE, Server.serverName);
        this.playerState=playerState;
    }

    public String getState(){
        return playerState.toString();
    }

    public String toString(){
        return "PlayerStateMessage{"+
                "state: " + this.playerState +
                '}';
    }

}
