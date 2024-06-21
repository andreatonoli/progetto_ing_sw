package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;

import java.util.ArrayList;
import java.util.List;

public interface Ui{
    void reset();
    void handleReconnection();
    void askNickname();
    void askNickname(int lobby);
    void selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId);
    void askLobbySize();
    void askSide(Card starterCard);
    void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players);
    void askAchievement(Achievement[] choices);
    void askColor(List<Color> colors);
    void setMessage(String message, boolean isError);
    void declareWinners(ArrayList<String> winners);
}
