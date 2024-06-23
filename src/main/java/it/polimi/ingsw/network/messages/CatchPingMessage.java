package it.polimi.ingsw.network.messages;

/**
 * Message used to notify the server that the client is still alive.
 */
public class CatchPingMessage extends Message{
    public CatchPingMessage(String username) {
        super(MessageType.CATCH_PING, username);
    }

    @Override
    public String toString(){
        return "CatchPingMessage{" +
                "sender = " + getSender() +
                '}';
    }
}
