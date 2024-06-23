package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

/**
 * Message used to send the updated score of a player to the client.
 */
public class ScoreUpdateMessage extends Message{
    private final int point;
    private final String name;
    public ScoreUpdateMessage(int point, String name){
        super(MessageType.SCORE_UPDATE, Server.serverName);
        this.point = point;
        this.name = name;
    }
    
    public int getPoint(){
        return point;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "ScoreUpdateMessage{" +
                "point: " + point +
                "name: " + name +
                '}';
    }
}
