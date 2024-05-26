package arimaa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;
import javafx.scene.Node;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        boolean isGameStarted = false;
        boolean isGameRunning = false;
    
        Pair<Node, Object> content;
        try {
            content = loadFXML(isGameStarted, isGameRunning);
        } catch (IOException e) {
            logger.severe("Error loading FXML file: " + e.getMessage());
            return;
        }
    
        Scene scene = new Scene((Parent) content.getKey());
        Object controller = content.getValue();
        // Do something with the controller...
    
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private <T> Pair<Node, T> loadFXML(boolean isGameStarted, boolean isGameRunning) throws IOException {
        FXMLLoader loader;
        if (!isGameStarted) {
            loader = new FXMLLoader(getClass().getResource("./views/StartView.fxml"));
        } else if (isGameRunning) {
            loader = new FXMLLoader(getClass().getResource("./views/GameView.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("./views/EndView.fxml"));
        }
        Node root = (Node) loader.load();
        T controller = loader.getController();
        return new Pair<>(root, controller);
    }

    public static void main(String[] args) {
        // Set the log level based on a startup parameter
        if (args.length > 0 && args[0].equals("debug")) {
            logger.setLevel(Level.ALL);
        } else {
            logger.setLevel(Level.OFF);
        }

        // Launch the application
        launch(args);
    }





    // private static final Logger logger = Logger.getLogger(Main.class.getName());

    // @Override
    // public void start(Stage primaryStage) {
    //     try {
    //         logger.info("Starting application.");

    //         // Create the root pane for the application
    //         BorderPane root = new BorderPane();
            
    //         // Create the game board and the game instance
    //         logger.info("Creating board.");
    //         Board board = new Board();
    //         Arimaa arimaa = new Arimaa();
            
    //         // Load the board view, set the board, and initialize the controller
    //         logger.info("Loading BoardView.fxml.");
    //         FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("./views/BoardView.fxml"));
    //         Parent boardView = boardLoader.load();
    //         root.setCenter(boardView);
    //         BoardController boardController = boardLoader.getController();
    //         logger.info("Setting board in BoardController.");
    //         boardController.setBoard(board);
    //         logger.info("Initializing BoardController.");
    //         boardController.initialize();
            
    //         // Load the game view, set the board, game, and other necessary properties, and initialize the controller
    //         logger.info("Loading ArimaaView.fxml.");
    //         FXMLLoader arimaaLoader = new FXMLLoader(getClass().getResource("./views/ArimaaView.fxml"));
    //         Parent arimaaView = arimaaLoader.load();
    //         root.setBottom(arimaaView);
    //         ArimaaController arimaaController = arimaaLoader.getController();
    //         logger.info("Setting properties in ArimaaController.");
    //         arimaaController.setBoard(board);
    //         arimaaController.setBoardController(boardController);
    //         arimaaController.setArimaa(arimaa);
    //         arimaaController.setRoot(root); // Pass the root to the controller
    //         arimaaController.setStage(primaryStage); // Pass the stage to the controller
    //         logger.info("Initializing ArimaaController.");
    //         arimaaController.initialize();

    //         // Set the ArimaaController in the BoardController
    //         logger.info("Setting ArimaaController in BoardController.");
    //         boardController.setArimaaController(arimaaController);

    //         // Set the board in the Arimaa game instance
    //         logger.info("Setting board to the Arimaa game instance.");
    //         arimaa.setBoard(board);

    //         // Set up the scene and stage and show the stage
    //         logger.info("Setting up scene and stage.");
    //         Scene scene = new Scene(root, 960, 960);
    //         primaryStage.setScene(scene);
    //         primaryStage.setTitle("Arimaa Game");
    //         primaryStage.show();
    //     } catch (Exception e) {
    //         logger.severe("(!) ERROR: " + e.getMessage());
    //     }
    // }

    // public static void main(String[] args) {
    //     // Set the log level based on a startup parameter
    //     if (args.length > 0 && args[0].equals("debug")) {
    //         logger.setLevel(Level.ALL);
    //     } else {
    //         logger.setLevel(Level.OFF);
    //     }

    //     // Launch the application
    //     launch(args);
    // }
}