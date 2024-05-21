package arimaa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the chessboard view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardView.fxml"));
        // Load the scene from the FXML file
        Scene scene = loader.load();

        // Set the scene and title for the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Game");

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}