package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.server.Server;

public class ScoreUpdateMessage extends Message{
    private int point;
    private String name;
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
