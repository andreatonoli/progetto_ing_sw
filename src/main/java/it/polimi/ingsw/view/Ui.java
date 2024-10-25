package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface containing the common methods for the UI.
 */
public interface Ui{

    /**
     * Method used to reset the UI.
     */
    void reset();

    /**
     * Method used to handle the reconnection.
     */
    void handleReconnection();

    /**
     * Method used to ask the nickname.
     */
    void askNickname();

    /**
     * Method used to ask the action the player wants to do.
     * @param startingGamesId list of the starting games' id.
     * @param gamesWhitDisconnectionsId list of the games with disconnections id.
     */
    void selectGame(List<Integer> startingGamesId, List<Integer> gamesWhitDisconnectionsId);

    /**
     * Method used to ask the size of the lobby the player wants to create.
     */
    void askLobbySize();

    /**
     * Method used to ask the side the player wants to place the starter card.
     * @param starterCard is the starter card.
     */
    void askSide(Card starterCard);

    /**
     * Method to print the commands available to the player.
     * @param player is the player.
     * @param game is the game.
     * @param players is the list of opponents.
     */
    void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players);

    /**
     * Method used to ask the player the achievement he wants to take.
     * @param choices are the achievements the player can choose.
     */
    void askAchievement(Achievement[] choices);

    /**
     * Method used to ask the player the color he wants to take.
     * @param colors are the colors the player can choose.
     */
    void askColor(List<Color> colors);

    /**
     * Method used to set the message in the UI.
     * @param message is the message to be set.
     * @param isError is true if the message is an error, false otherwise.
     */
    void setMessage(String message, boolean isError);

    /**
     * Method used to declare the winners of the game.
     * @param winners are the winners of the game.
     */
    void declareWinners(ArrayList<String> winners);
}
