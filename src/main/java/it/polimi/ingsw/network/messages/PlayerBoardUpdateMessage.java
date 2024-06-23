package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.server.Server;

/**
 * Message sento to the client to update his board or one of his opponents.
 */
public class PlayerBoardUpdateMessage extends Message {
    private final PlayerBoard pBoard;
    private final String name;
    public PlayerBoardUpdateMessage(PlayerBoard pBoard, String name){
        super(MessageType.PLAYERBOARD_UPDATE, Server.serverName);
        this.pBoard = pBoard;
        this.name = name;
    }
    public PlayerBoard getpBoard(){
        return pBoard;
    }
    public String getName(){
        return name;
    }
    @Override
    public String toString() {
        return "PlayerBoardUpdateMessage{" +
                "board: " + pBoard +
                "name: " + name +
                '}';
    }
}
