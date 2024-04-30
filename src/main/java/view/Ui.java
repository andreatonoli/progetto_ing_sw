package view;

import model.card.Achievement;
import model.card.Card;
import model.player.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(int freeLobbies);
    void showText(String text);
    int setLobbySize();
    boolean askToFlip();
    void printView(PlayerBoard playerBoard, Card[] hand, String username, GameBoard gameBoard, ArrayList<Player> players, ArrayList<String> messages);
    void printCardFromPlayerBoard(PlayerBoard playerBoard, int[] coord);
    void printCard(PlayerBoard playerBoard, Card card);
    void printCard(Card card);
    void printAchievement(Achievement achievement);
    Achievement chooseAchievement(Achievement[] choices);
}
