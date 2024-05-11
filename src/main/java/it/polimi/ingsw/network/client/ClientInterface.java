package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Card;

import java.util.ArrayList;

public interface ClientInterface {
    void sendChatMessage(String message);
    void sendChatMessage(String receiver, String message);
    void placeCard(Card card, int[] placingCoordinates);
    void drawCard(String chosenDeck);
    void drawCardFromBoard(String ChosenDeck, int index);
    ArrayList<PlayerBean> getPlayers();
}
