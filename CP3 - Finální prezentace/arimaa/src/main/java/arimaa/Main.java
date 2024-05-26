package arimaa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.logging.Logger;

import arimaa.controllers.ArimaaController;
import arimaa.controllers.BoardController;
import arimaa.controllers.DummyController;
import arimaa.models.Arimaa;
import arimaa.models.Board;

import java.io.IOException;
import java.util.logging.Level;
import javafx.scene.Node;



public class Main extends Application {
    ArimaaController arimaaController;
    BoardController boardController;
    DummyController dummyController;

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Models
        Arimaa arimaa = new Arimaa();
        Board board = new Board();

        // Controllers
        arimaaController = new ArimaaController(arimaa, board);
        boardController = new BoardController(board);
        dummyController = new DummyController();

        arimaaController.setBoardController(boardController);
        boardController.setArimaaController(arimaaController);

        // Game state
        boolean isGameRunning = true;
        boolean isGameSetup = false;
        boolean isGameEnd = false;
    
        VBox rootContainer;
        try {
            if (!isGameRunning && !isGameSetup && !isGameEnd) {
                Pair<Node, Object> content = loadFXML("./views/StartView.fxml", dummyController);
                rootContainer = new VBox(content.getKey());
            } else if (isGameRunning && !isGameSetup && !isGameEnd) {
                Pair<Node, Object> content1 = loadFXML("./views/StartView.fxml", dummyController);
                Pair<Node, Object> content2 = loadFXML("./views/StartView.fxml", dummyController);
                // Pair<Node, Object> content1 = loadFXML("./views/BoardView.fxml", boardController);
                // Pair<Node, Object> content2 = loadFXML("./views/ArimaaSetupView.fxml", arimaaController);
                rootContainer = new VBox(content1.getKey(), content2.getKey());  
            } else if (isGameRunning && isGameSetup && !isGameEnd) {
                Pair<Node, Object> content1 = loadFXML("./views/StartView.fxml", dummyController);
                Pair<Node, Object> content2 = loadFXML("./views/StartView.fxml", dummyController);
                // Pair<Node, Object> content1 = loadFXML("./views/BoardView.fxml", boardController);
                // Pair<Node, Object> content2 = loadFXML("./views/ArimaaView.fxml", arimaaController);
                rootContainer = new VBox(content1.getKey(), content2.getKey());
            } else {
                Pair<Node, Object> content = loadFXML("./views/EndView.fxml", dummyController);
                rootContainer = new VBox(content.getKey());
            }
        } catch (IOException e) {
            logger.severe("Error loading FXML file: " + e.getMessage());
            return;
        }
    
        Scene scene = new Scene(rootContainer);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pair<Node, Object> loadFXML(String fxmlPath, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(c -> controller);
        Node root = (Node) loader.load();
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
}