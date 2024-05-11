package it.polimi.ingsw.network.client;

import java.util.ArrayList;

public interface ClientInterface {
    void sendChatMessage(String message);
    void sendChatMessage(String receiver, String message);
    void placeACard();
    void drawACard();
    ArrayList<PlayerBean> getPlayers();
}
