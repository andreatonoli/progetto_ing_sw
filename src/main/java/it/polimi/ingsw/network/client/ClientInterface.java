package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;
import java.util.List;

public interface ClientInterface {
    void login();
    void setNickname(String nickname);
    void setNickname(String nickname, int lobby);
    void setOnConnectionAction(int response, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId);
    void setLobbySize(int size);
    void placeStarterCard(boolean side, Card starterCard);
    void chooseAchievement(Achievement achievement);
    void chooseColor(Color chosenColor);
    void sendChatMessage(String message);
    void sendChatMessage(String receiver, String message);
    void placeCard(Card card, int[] placingCoordinates);
    void drawCard(String chosenDeck);
    void drawCardFromBoard(int index);
}
