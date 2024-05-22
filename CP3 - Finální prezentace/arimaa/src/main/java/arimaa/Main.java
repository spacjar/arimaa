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
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Starting application.");

            BorderPane root = new BorderPane();

            // Create the board
            logger.info("Creating board.");
            Board board = new Board();

            Arimaa arimaa = new Arimaa();
            arimaa.setBoard(board);

            // Load BoardView.fxml and set the board
            logger.info("Loading BoardView.fxml.");
            FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("./views/BoardView.fxml"));
            Parent boardView = boardLoader.load();
            root.setCenter(boardView);

            BoardController boardController = boardLoader.getController();
            boardController.setBoard(board);
            boardController.initialize();

            // Load ArimaaView.fxml and set the board
            logger.info("Loading ArimaaView.fxml.");
            FXMLLoader arimaaLoader = new FXMLLoader(getClass().getResource("./views/ArimaaView.fxml"));
            Parent arimaaView = arimaaLoader.load();
            root.setBottom(arimaaView);

            ArimaaController arimaaController = arimaaLoader.getController();
            arimaaController.setBoard(board);
            arimaaController.setBoardController(boardController);
            arimaaController.setArimaa(arimaa);
            arimaaController.setRoot(root); // Pass the root to the controller
            arimaaController.setStage(primaryStage); // Pass the stage to the controller
            arimaaController.initialize();

            // Set up the scene and stage
            logger.info("Setting up scene and stage.");
            Scene scene = new Scene(root, 640, 640);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Arimaa Game");
            primaryStage.show();
        } catch (Exception e) {
            logger.severe("(!) ERROR: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Set the log level based on a startup parameter
        if (args.length > 0 && args[0].equals("debug")) {
            logger.setLevel(Level.ALL);
        } else {
            logger.setLevel(Level.OFF);
        }

        launch(args);
    }
}