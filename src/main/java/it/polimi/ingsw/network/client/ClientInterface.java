package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;

import java.util.List;

/**
 * Interface containing the methods the client can call.
 */
public interface ClientInterface {

    /**
     * Method used to login.
     */
    void login();

    /**
     * Method used to set the nickname.
     * @param nickname is the nickname to be set.
     */
    void setNickname(String nickname);

    /**
     * Method used to perform the action.
     * @param response is the action to perform.
     * @param startingGamesId is the list of the starting games' id.
     * @param gamesWithDisconnectionsId is the list of the games with disconnections id.
     */
    void setOnConnectionAction(int response, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId);

    /**
     * Method used to set the lobby size.
     * @param size is the size of the lobby.
     */
    void setLobbySize(int size);

    /**
     * Method used to place the starter card.
     * @param side is the side where the starter card is placed.
     * @param starterCard is the starter card.
     */
    void placeStarterCard(boolean side, Card starterCard);

    /**
     * Method used to choose the achievement.
     * @param achievement is the chosen achievement.
     */
    void chooseAchievement(Achievement achievement);

    /**
     * Method used to choose the color.
     * @param chosenColor is the chosen color.
     */
    void chooseColor(Color chosenColor);

    /**
     * Method used to send a global message.
     * @param message is the message to be sent.
     */
    void sendChatMessage(String message);

    /**
     * Method used to send a private message.
     * @param receiver is the receiver of the message.
     * @param message is the message to be sent.
     */
    void sendChatMessage(String receiver, String message);

    /**
     * Method used to place the card.
     * @param card is the card to be placed.
     * @param placingCoordinates are the coordinates where the card is placed.
     */
    void placeCard(Card card, int[] placingCoordinates);

    /**
     * Method used to draw a card.
     * @param chosenDeck is the deck from which the card is drawn.
     */
    void drawCard(String chosenDeck);

    /**
     * Method used to draw a card from the board.
     * @param index is the index of the card to be drawn.
     */
    void drawCardFromBoard(int index);
}
