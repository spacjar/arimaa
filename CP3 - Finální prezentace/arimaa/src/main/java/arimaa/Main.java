package arimaa;

import arimaa.controllers.ArimaaController;
import arimaa.controllers.BoardController;
import arimaa.models.Arimaa;
import arimaa.models.Board;
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

            // Create the board
            Board board = new Board();

            Arimaa arimaa = new Arimaa();
            arimaa.setBoard(board);

            // Load BoardView.fxml and set the board
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("./views/BoardView.fxml"));
            Parent boardView = boardLoader.load();
            root.setCenter(boardView);

            BoardController boardController = boardLoader.getController();
            boardController.setBoard(board);
            boardController.initialize();

            // Load ArimaaView.fxml and set the board
            FXMLLoader arimaaLoader = new FXMLLoader(getClass().getResource("./views/ArimaaView.fxml"));
            Parent arimaaView = arimaaLoader.load();
            root.setBottom(arimaaView);

            ArimaaController arimaaController = arimaaLoader.getController();
            arimaaController.setBoard(board);
            arimaaController.setBoardController(boardController);
            arimaaController.setArimaa(arimaa);
            arimaaController.initialize();

            // Set up the scene and stage
            Scene scene = new Scene(root, 640, 640);
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