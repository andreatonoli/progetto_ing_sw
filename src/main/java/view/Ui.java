package view;

import Controller.Controller;
import model.Card;

import java.util.List;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(List<Controller> activeGames);
    void showText(String text);
    int setLobbySize();
    boolean askToFlip(Card card);
}
