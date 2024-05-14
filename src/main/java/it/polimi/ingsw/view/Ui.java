package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;

import java.util.ArrayList;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(int freeLobbies);
    void showText(String text);
    int setLobbySize();
    boolean askSide();
    void printView(PlayerBean player, GameBean game, ArrayList<PlayerBean> players);
    void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players);
    void printCardFromPlayerBoard(PlayerBoard playerBoard, int[] coord);
    void printStarterCard(Card card);
    void printCard(PlayerBoard playerBoard, Card card);
    void printCard(Card card);
    void printAchievement(Achievement achievement);
    Achievement chooseAchievement(Achievement[] choices);
}
