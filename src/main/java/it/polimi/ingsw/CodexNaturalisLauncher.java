package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.Server;

import java.util.Scanner;

//public class CodexNaturalisLauncher {
//    public static void main(String[] args){
//        if (args.length != 0){
//            if (args[0].equals("0")){
//                new Server();
//            }
//            else {
//                new Client();
//            }
//        }
//        else{
//            System.out.println("Premi [0] per creare un server.\nPremi un qualsiasi altro numero per creare un Client");
//            Scanner scan = new Scanner(System.in);
//            int choice = scan.nextInt();
//            if (choice == 0){
//                new Server();
//            }
//            else{
//                new Client();
//            }
//        }
//    }
//}
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CodexNaturalisLauncher extends Application {
    public void start(Stage primaryStage) {
        // Create a button with the text "Click Me"
        Button button = new Button("Click Me");

        // Set an action for the button
        button.setOnAction(e -> System.out.println("Hello, World!"));

        // Create a layout pane and add the button to it
        StackPane root = new StackPane();
        root.getChildren().add(button);

        // Create a scene with the layout pane as the root node
        Scene scene = new Scene(root, 300, 200);

        // Set the scene for the primary stage (window) and show it
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
