package it.polimi.ingsw.view;

public enum GuiScenes {
    START_SCENE("/fxml/startScene.fxml"),
    CONNECTION_SCENE("/fxml/connectionScene.fxml"),
    SERVER_ADDRESS_SCENE("/fxml/serverAddressScene.fxml"),
    SERVER_PORT_SCENE("/fxml/serverPortScene.fxml"),
    LOBBIES_SCENE("/fxml/lobbiesScene.fxml"),
    LOBBY_SIZE_SCENE("/fxml/lobbySizeScene.fxml"),
    RECONNECT_SCENE("/fxml/reconnectScene.fxml"),
    WAITING_SCENE("/fxml/waitingScene.fxml"),
    LOGIN_SCENE("/fxml/loginScene.fxml"),
    STARTER_FLIP_SCENE("/fxml/starterFlipScene.fxml"),
    ACHIEVEMENT_CHOICE_SCENE("/fxml/achievementChoiceScene.fxml"),
    WAITING_SETUP_SCENE("/fxml/waitingSetupScene.fxml"),
    COLOR_CHOICE_SCENE("/fxml/colorChoiceScene.fxml"),
    WAITING_COLOR_SCENE("/fxml/waitingColorScene.fxml"),
    MAIN_SCENE("/fxml/mainScene.fxml"),
    OTHER_PLAYER_BOARDS_SCENE("/fxml/otherPlayerBoardsScene.fxml");

    private final String fxml;

    GuiScenes(String fxml){
        this.fxml = fxml;
    }

    public static String getFxml(GuiScenes s){ return s.fxml; }
    
}