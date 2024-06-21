package it.polimi.ingsw.view;

import it.polimi.ingsw.view.controllers.*;

public enum GuiScenes {
    START_SCENE("/fxml/startScene.fxml", new StartSceneController()),
    CONNECTION_SCENE("/fxml/connectionScene.fxml", new ConnectionSceneController()),
    SERVER_ADDRESS_SCENE("/fxml/serverAddressScene.fxml", new ServerAddressSceneController()),
    SERVER_PORT_SCENE("/fxml/serverPortScene.fxml", new ServerPortSceneController()),
    LOBBIES_SCENE("/fxml/lobbiesScene.fxml", new LobbiesSceneController()),
    LOBBY_SIZE_SCENE("/fxml/lobbySizeScene.fxml", new LobbySizeSceneController()),
    RECONNECT_SCENE("/fxml/reconnectScene.fxml", new ReconnectSceneController()),
    WAITING_SCENE("/fxml/waitingScene.fxml", null),
    LOGIN_SCENE("/fxml/loginScene.fxml", new LoginSceneController()),
    STARTER_FLIP_SCENE("/fxml/starterFlipScene.fxml", new StarterFlipSceneController()),
    ACHIEVEMENT_CHOICE_SCENE("/fxml/achievementChoiceScene.fxml", new AchievementChoiceSceneController()),
    WAITING_SETUP_SCENE("/fxml/waitingSetupScene.fxml", null),
    COLOR_CHOICE_SCENE("/fxml/colorChoiceScene.fxml", new ColorChoiceSceneController()),
    WAITING_COLOR_SCENE("/fxml/waitingColorScene.fxml", null),
    MAIN_SCENE("/fxml/mainScene.fxml", new MainSceneController()),
    OTHER_PLAYER_BOARDS_SCENE("/fxml/otherPlayerBoardsScene.fxml", new OtherPlayerBoardsSceneController());

    private final String fxml;
    private final GenericController controller;

    GuiScenes(String fxml, GenericController controller){
        this.fxml = fxml;
        this.controller = controller;
    }

    public static String getFxml(GuiScenes s){ return s.fxml; }

    public static GenericController getController(GuiScenes s) { return s.controller; }
    
}