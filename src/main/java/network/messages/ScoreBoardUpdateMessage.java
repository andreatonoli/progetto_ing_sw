package network.messages;

import network.server.Server;

public class ScoreBoardUpdateMessage extends Message{
    public ScoreBoardUpdateMessage(){
        super(MessageType.SCOREBOARD_UPDATE, Server.serverName);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
