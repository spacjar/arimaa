package arimaa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();

            Parent boardView = FXMLLoader.load(getClass().getResource("./views/BoardView.fxml"));
            root.setCenter(boardView);
    
            Parent arimaaView = FXMLLoader.load(getClass().getResource("./views/ArimaaView.fxml"));
            root.setBottom(arimaaView);

            Scene scene = new Scene(root, 400, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Arimaa Game");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Arimaa arimaa = new Arimaa();
        // arimaa.startGame();

        launch(args);
    }
}