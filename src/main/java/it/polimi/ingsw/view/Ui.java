package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.PlayerBean;

import java.util.ArrayList;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(int freeLobbies);
    void showText(String text);
    int setLobbySize();
    boolean askToFlip();
    void printViewWithCommands(PlayerBoard playerBoard, Card[] hand, String username, Card[] commonResources, Card[] commonGold, Achievement[] commonAchievement, ArrayList<PlayerBean> players, ArrayList<String> messages);
    void printView(PlayerBoard playerBoard, Card[] hand, String username, Card[] commonResources, Card[] commonGold, Achievement[] commonAchievement, ArrayList<PlayerBean> players, ArrayList<String> messages);
    void printCardFromPlayerBoard(PlayerBoard playerBoard, int[] coord);
    void printCard(PlayerBoard playerBoard, Card card);
    void printCard(Card card);
    void printAchievement(Achievement achievement);
    Achievement chooseAchievement(Achievement[] choices);
}
