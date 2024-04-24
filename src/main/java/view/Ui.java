package view;

import Controller.Controller;
import model.*;
import model.Achievement;

import java.util.List;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(int freeLobbies);
    void showText(String text);
    int setLobbySize();
    boolean askToFlip();
    void printView(PlayerBoard playerBoard, Card[] hand, String username, GameBoard gameBoard);
    void printCardFromPlayerBoard(PlayerBoard playerBoard, int[] coord);
    void printCard(PlayerBoard playerBoard, Card card);
    void printCard(Card card);
    void printAchievement(Achievement achievement);
}
