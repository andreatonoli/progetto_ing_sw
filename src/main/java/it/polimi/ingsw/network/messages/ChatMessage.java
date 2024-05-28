package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.network.server.Server;
import java.util.ArrayList;

public class ChatMessage extends Message{
    private final ArrayList<String> chat;
    public ChatMessage(ArrayList<String> chat) {
        super(MessageType.CHAT, Server.serverName);
        this.chat = chat;
    }
    public ArrayList<String> getChat(){
        return chat;
    }
    @Override
    public String toString(){
        return "ChatMessage{" +
                "chat: " + chat +
                '}';
    }
}
