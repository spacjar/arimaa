package arimaa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.logging.Logger;

import arimaa.controllers.ArimaaController;
import arimaa.controllers.ArimaaStartController;
import arimaa.controllers.BoardController;
import arimaa.controllers.DummyController;
import arimaa.models.Arimaa;
import arimaa.models.Board;

import java.io.IOException;
import java.util.logging.Level;
import javafx.scene.Node;



public class Main extends Application {
    ArimaaStartController arimaaStartController;
    ArimaaController arimaaController;
    BoardController boardController;
    DummyController dummyController;

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Models
        Board board = new Board();
        Arimaa arimaa = new Arimaa(board);

        // Controllers
        arimaaStartController = new ArimaaStartController(arimaa);
        arimaaController = new ArimaaController(arimaa, board);
        boardController = new BoardController(board);
        dummyController = new DummyController();

        arimaaController.setBoardController(boardController);
        boardController.setArimaaController(arimaaController);

        // Game state
        boolean isGameRunning = arimaa.getIsGameStart();
        boolean isGameSetup = arimaa.getIsGameSetup();
        boolean isGameEnd = arimaa.getIsGameEnd();
    
        VBox rootContainer;
        try {
            if (!isGameRunning && !isGameSetup && !isGameEnd) {
                Pair<Node, Object> content = loadFXML("./views/StartView.fxml", arimaaStartController);
                rootContainer = new VBox(content.getKey());
            } else if (isGameRunning && !isGameSetup && !isGameEnd) {
                Pair<Node, Object> content1 = loadFXML("./views/EndView.fxml", dummyController);
                Pair<Node, Object> content2 = loadFXML("./views/EndView.fxml", dummyController);
                // Pair<Node, Object> content1 = loadFXML("./views/BoardView.fxml", boardController);
                // Pair<Node, Object> content2 = loadFXML("./views/ArimaaSetupView.fxml", arimaaController);
                rootContainer = new VBox(content1.getKey(), content2.getKey());  
            } else if (isGameRunning && isGameSetup && !isGameEnd) {
                Pair<Node, Object> content1 = loadFXML("./views/EndView.fxml", dummyController);
                Pair<Node, Object> content2 = loadFXML("./views/EndView.fxml", dummyController);
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
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    private Pair<Node, Object> loadFXML(String fxmlPath, Object controller) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(c -> {
            System.out.println("FXMLLoader requested controller of type: " + c.getName());
            return controller;
        });
        loader.setController(controller);
    
        if (controller == null) {
            System.out.println("Controller is null before loading.");
        }
    
        Node root = (Node) loader.load();
    
        Object usedController = loader.getController();
        if (usedController == controller) {
            System.out.println("The controller was set correctly.");
        } else {
            System.out.println("The controller was not set correctly.");
        }
    
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