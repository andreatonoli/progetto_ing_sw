package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.Ui;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GuiInputHandler implements Ui {

    /**
     * The instance of the class.
     */
    private static GuiInputHandler instance;

    /**
     * Reference to the client.
     */
    private ClientInterface client;

    /**
     * The address of the server.
     */
    private String address;

    /**
     * Gets the instance of the GuiHandler.
     */
    public static GuiInputHandler getInstance(){
        if(instance == null){
            instance = new GuiInputHandler();
        }
        return instance;
    }

    /**
     * Called when the play button is clicked. It sets the scene to the server address scene.
     */
    public void playButtonClicked(){
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.SERVER_ADDRESS_SCENE.ordinal())));
    }

    /**
     * Called when the next button is clicked. It sets the address and changes the scene to the connection scene.
     */
    public void nextAddressButtonClicked(String s){
        address = s;
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.CONNECTION_SCENE.ordinal())));
    }

    /**
     * Called when the RMI button is clicked. It sets the connection type to RMI and changes the scene to the server port scene.
     */
    public void rmiButtonClicked() {
        try {
            client = new RMIClient(address, Server.rmiPort, this);
            client.login();
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Called when the socket button is clicked. It sets the connection type to socket and changes the scene to the server port scene.
     */
    public void socketButtonClicked() {
        client = new SocketClient(address, Server.socketPort, this);
        client.login();
    }

    /**
     * Called when the next button is clicked. It sets the nickname.
     */
    public void nextLoginButtonClicked(String username){
        client.setNickname(username);
    }

    /**
     * Called when the next button is clicked. It changes the scene to the waiting scene.
     */
    public void joinLobbyButtonClicked(int selectedLobby, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        client.setOnConnectionAction(selectedLobby, startingGamesId, gamesWithDisconnectionsId);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_SCENE.ordinal())));
    }

    /**
     * Called when the create lobby button is clicked. It changes the scene to the login scene.
     */
    public void createLobbyButtonClicked(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        client.setOnConnectionAction(-1, startingGamesId, gamesWithDisconnectionsId);
    }

    /**
     * Called when the reconnect button is clicked. It changes the scene to the reconnect scene.
     */
    public void reconnectLobbyButtonClicked(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        Platform.runLater(() -> {
            ReconnectSceneController c = (ReconnectSceneController) GuiScenes.getController(GuiScenes.LOBBIES_SCENE);
            c.setLobbies(startingGamesId, gamesWithDisconnectionsId);
            Gui.setScene(Gui.getScenes().get(GuiScenes.RECONNECT_SCENE.ordinal()));
        });
    }

    /**
     * Called when the reconnect button is clicked. It reconnects the client and changes the scene to the waiting scene.
     */
    public void reconnectButtonClicked(int selectedLobby, List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId){
        client.setOnConnectionAction(selectedLobby, startingGamesId, gamesWithDisconnectionsId);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_SCENE.ordinal())));
    }

    /**
     * Called when the next button is clicked. It sets the lobby size and changes the scene to the waiting scene.
     */
    public void nextLobbySizeButtonClicked(int lobbySize) {
        client.setLobbySize(lobbySize);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_SCENE.ordinal())));
    }

    /**
     * Called when the player confirms the side on which he wants to play the starter card.
     */
    public void nextStarterCardButtonClicked(boolean side, Card starter){
        client.placeStarterCard(side, starter);
    }

    /**
     * Called when the player chooses an achievement.
     * @param selectedAchievement the achievement chosen by the player.
     */
    public void chosenAchievement(Achievement selectedAchievement){
        client.chooseAchievement(selectedAchievement);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_SETUP_SCENE.ordinal())));
    }

    /**
     * Called when the player chooses a color.
     * @param selectedColor the color chosen by the player.
     */
    public void chosenColor(Color selectedColor){
        client.chooseColor(selectedColor);
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.WAITING_COLOR_SCENE.ordinal())));
    }

    /**
     * called when the player wants to draw from deck.
     * @param s the deck where the player wants to draw.
     */
    public void drawDeckButtonClicked(String s){
        client.drawCard(s);
    }

    /**
     * Called when the player wants to draw from board.
     * @param s the card the player wants to draw.
     */
    public void drawCardButtonClicked(String s){
        client.drawCardFromBoard(Integer.parseInt(s));
    }

    /**
     * Called when the player wants to place a card.
     * @param cardToPlace the card the player wants to place.
     * @param placingCoordinates the coordinates where the player wants to place the card.
     */
    public void placeCard(Card cardToPlace, int[] placingCoordinates){ client.placeCard(cardToPlace, placingCoordinates); }

    /**
     * Called when the player wants to send a global message.
     * @param message global message
     */
    public void sendGlobalMessageButtonClicked(String message){ client.sendChatMessage(message); }

    /**
     * Called when the player wants to send a private message.
     * @param message private message
     * @param receiver the receiver of the message
     */
    public void sendMessageButtonClicked(String message, String receiver){
        client.sendChatMessage(receiver, message);
    }

    /**
     * Called when the player wants to see the other players' boards.
     * @param other the player whose board the player wants to see.
     * @param player the player who wants to see the board.
     * @param game the game the player is playing.
     * @param players the list of players in the game.
     */
    public void otherPlayersBoardButtonClicked(PlayerBean other, PlayerBean player, GameBean game, ArrayList<PlayerBean> players){
        Platform.runLater(() -> {
            OtherPlayerBoardsSceneController c = (OtherPlayerBoardsSceneController) GuiScenes.getController(GuiScenes.OTHER_PLAYER_BOARDS_SCENE);
            c.setBoard(other, player, game, players);
            Gui.setScene(Gui.getScenes().get(GuiScenes.OTHER_PLAYER_BOARDS_SCENE.ordinal()));
        });
    }

    /**
     * Called when the player wants to come back to his board.
     * @param player the player who is playing.
     * @param game the game the player is playing.
     * @param opponents the list of players in the game.
     */
    public void backButtonClicked(PlayerBean player, GameBean game, ArrayList<PlayerBean> opponents){
        Platform.runLater(() -> {
            MainSceneController c = (MainSceneController) GuiScenes.getController(GuiScenes.MAIN_SCENE);
            c.setBoard(player, game, opponents);
            Gui.setScene(Gui.getScenes().get(GuiScenes.MAIN_SCENE.ordinal()));
        });
    }

    @Override
    public void reset() {

    }

    @Override
    public void handleReconnection() {

    }

    /**
     * Asks the player its nickname.
     */
    @Override
    public void askNickname() {
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.LOGIN_SCENE.ordinal())));
    }

    /**
     * Asks the player if he wants to join a lobby, create a new one or reconnect to a lobby.
     */
    @Override
    public void selectGame(List<Integer> startingGamesId, List<Integer> gamesWithDisconnectionsId) {
        Platform.runLater(() -> {
            LobbiesSceneController c = (LobbiesSceneController) GuiScenes.getController(GuiScenes.LOBBIES_SCENE);
            c.setLobbies(startingGamesId, gamesWithDisconnectionsId);
            Gui.setScene(Gui.getScenes().get(GuiScenes.LOBBIES_SCENE.ordinal()));
        });
    }

    /**
     * Asks the player the size of the lobby he wants to create.
     */
    @Override
    public void askLobbySize() {
        Platform.runLater(() -> Gui.setScene(Gui.getScenes().get(GuiScenes.LOBBY_SIZE_SCENE.ordinal())));
    }

    /**
     * Asks the player on which side he wants to play the starter card.
     * @param starterCard the starter card.
     */
    @Override
    public void askSide(Card starterCard) {
        Platform.runLater(() -> {
            StarterFlipSceneController c = (StarterFlipSceneController) GuiScenes.getController(GuiScenes.STARTER_FLIP_SCENE);
            c.setFace(starterCard);
            Gui.setScene(Gui.getScenes().get(GuiScenes.STARTER_FLIP_SCENE.ordinal()));
        });
    }

    /**
     * Shows the main scene, containing the scoretrack, the player's board, the common cards, the player's hand and chat.
     * @param player is the player.
     * @param game is the game.
     * @param players is the list of opponents.
     */
    @Override
    public void printViewWithCommands(PlayerBean player, GameBean game, ArrayList<PlayerBean> players) {
        Platform.runLater(() -> {
            MainSceneController c = (MainSceneController) GuiScenes.getController(GuiScenes.MAIN_SCENE);
            c.setBoard(player, game, players);
            Gui.getStage().setFullScreen(true);
            Gui.setScene(Gui.getScenes().get(GuiScenes.MAIN_SCENE.ordinal()));
        });
    }

    /**
     * Asks the player which of two achievements he wants to choose.
     * @param choices are the achievements the player can choose.
     */
    @Override
    public void askAchievement(Achievement[] choices) {
        AchievementChoiceSceneController c = (AchievementChoiceSceneController) GuiScenes.getController(GuiScenes.ACHIEVEMENT_CHOICE_SCENE);
        c.setAchievements(choices);
    }

    /**
     * Asks the player which color he wants to choose.
     * @param colors are the colors the player can choose.
     */
    @Override
    public void askColor(List<Color> colors) {
        Platform.runLater(() -> {
            ColorChoiceSceneController c = (ColorChoiceSceneController) GuiScenes.getController(GuiScenes.COLOR_CHOICE_SCENE);
            c.setColors(colors);
            Gui.setScene(Gui.getScenes().get(GuiScenes.COLOR_CHOICE_SCENE.ordinal()));
        });
    }

    /**
     * Sets the message to be shown to the player on the main scene.
     * @param message is the message to be set.
     * @param isError is true if the message is an error, false otherwise.
     */
    @Override
    public void setMessage(String message, boolean isError) {
        Platform.runLater(() -> {
            MainSceneController c = (MainSceneController) GuiScenes.getController(GuiScenes.MAIN_SCENE);
            c.setMessage(message, isError);
        });
    }

    /**
     * Shows the winners of the game.
     * @param winners are the winners of the game.
     */
    @Override
    public void declareWinners(ArrayList<String> winners) {
        Platform.runLater(() -> {
            WinnersSceneController c = (WinnersSceneController) GuiScenes.getController(GuiScenes.WINNERS_SCENE);
            c.setWinners(winners);
            Gui.setScene(Gui.getScenes().get(GuiScenes.WINNERS_SCENE.ordinal()));
        });
    }
}
