package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Gui extends Application {

    public static Stage stage;
    static FXMLLoader loader;
    static Parent root;
    private static final ArrayList<Parent> scenes = new ArrayList<>();
    private static final ArrayList<Object> controllers = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

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

    public static ArrayList<Parent> getScenes() {
        return scenes;
    }

    public static ArrayList<Object> getControllers() {
        return controllers;
    }

    public static void setScene(Parent r) {
        stage.getScene().setRoot(r);
    }

    @Override
    public void start(Stage s) throws Exception {

        loadAllScenes();
        stage = s;

        stage.setScene(new Scene(scenes.get(GuiScenes.START_SCENE.ordinal())));
        stage.setFullScreen(true);
        stage.show();

    }
}