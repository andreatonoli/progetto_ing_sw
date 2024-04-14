package network.messages;

import network.server.Server;

public class LobbyIndexMessage extends Message{
    public LobbyIndexMessage(String username){
        super(MessageType.LOBBY_INDEX, username);
    }
}
