package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Gui extends Application {

    /**
     * The stage of the GUI.
     */
    private static Stage stage;

    /**
     * The loader of the FXML file.
     */
    private static FXMLLoader loader;

    /**
     * First scene of the game.
     */
    private static Parent root;

    /**
     * List of all the scenes.
     */
    private static final ArrayList<Parent> scenes = new ArrayList<>();

    /**
     * List of all the controllers of the scenes.
     */
    private static final ArrayList<Object> controllers = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Load all the scenes of the game.
     */
    public static void loadAllScenes() {
        for (GuiScenes s : GuiScenes.values()) {
            loader = new FXMLLoader(Gui.class.getResource(GuiScenes.getFxml(s)));
            try {
                root = loader.load();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            scenes.add(root);
            s.setController(loader.getController());
        }
    }

    /**
     * Get the scenes.
     *
     * @return the scenes of the GUI.
     */
    public static ArrayList<Parent> getScenes() {
        return scenes;
    }

    /**
     * Get the controllers.
     *
     * @return the controllers of the GUI.
     */
    public static ArrayList<Object> getControllers() {
        return controllers;
    }

    /**
     * Set the scene to show.
     */
    public static void setScene(Parent r) {
        stage.getScene().setRoot(r);
    }

    /**
     * Sets the first scene and shows it.
     */
    @Override
    public void start(Stage s) throws Exception {

        loadAllScenes();
        stage = s;

        stage.setScene(new Scene(scenes.get(GuiScenes.START_SCENE.ordinal())));
        stage.setFullScreen(true);
        stage.show();

    }

    /**
     * Get the stage.
     *
     * @return the stage of the GUI.
     */
    public static Stage getStage() {
        return stage;
    }
}