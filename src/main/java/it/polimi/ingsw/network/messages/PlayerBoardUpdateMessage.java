package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.server.Server;

public class PlayerBoardUpdateMessage extends Message {
    private PlayerBoard pBoard;
    private String name;
    public PlayerBoardUpdateMessage(PlayerBoard pBoard, String name){
        super(MessageType.PLAYERBOARD_UPDATE, Server.serverName);
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
