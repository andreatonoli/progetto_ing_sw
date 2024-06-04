package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;

import java.util.ArrayList;
import java.util.List;

public interface Ui{
    String askNickname();
    String askServerAddress();
    int askServerPort(String connectionType);
    int selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId);
    int setLobbySize();
    boolean askSide(Card starterCard);
    void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players);
    Achievement chooseAchievement(Achievement[] choices);
    Color chooseColor(List<Color> colors);
    void setMessage(String message, boolean isError);
    void declareWinners(ArrayList<String> winners);
}
