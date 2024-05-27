package arimaa;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.Node;

import arimaa.controllers.ArimaaGameController;
import arimaa.controllers.ArimaaSetupController;
import arimaa.controllers.ArimaaEndController;
import arimaa.controllers.ArimaaStartController;
import arimaa.controllers.BoardController;

import arimaa.models.Arimaa;
import arimaa.models.Board;


public class Main extends Application {
    ArimaaStartController arimaaStartController;
    ArimaaSetupController arimaaSetupController;
    ArimaaGameController arimaaGameController;
    ArimaaEndController arimaaEndController;
    BoardController boardController;

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private BooleanProperty isGameStart;
    private BooleanProperty isGameSetup;
    private BooleanProperty isGameEnd;

    private VBox rootContainer = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Models
        Board board = new Board();
        Arimaa arimaa = new Arimaa(board);

        // Controllers
        arimaaStartController = new ArimaaStartController(arimaa, board);
        arimaaSetupController = new ArimaaSetupController(arimaa, board);
        arimaaGameController = new ArimaaGameController(arimaa, board);
        arimaaEndController = new ArimaaEndController(arimaa);
        boardController = new BoardController(arimaa, board);

        arimaaSetupController.setBoardController(boardController);
        arimaaGameController.setBoardController(boardController);
        boardController.setArimaaSetupController(arimaaSetupController);
        boardController.setArimaaGameController(arimaaGameController);

        // Game state
        isGameStart = new SimpleBooleanProperty();
        isGameSetup = new SimpleBooleanProperty();
        isGameEnd = new SimpleBooleanProperty();

        // Bind game state to arimaa state
        isGameStart.bind(arimaa.isGameStartProperty());
        isGameSetup.bind(arimaa.isGameSetupProperty());
        isGameEnd.bind(arimaa.isGameEndProperty());

        // Add listeners to game state
        isGameStart.addListener((observable, oldValue, newValue) -> {
            logger.info("isGameStart changed from " + oldValue + " to " + newValue);
            try {
                loadView();
            } catch (Exception e) {
                logger.severe("Error while updating the UI (isGameStart): " + e.getMessage());
            }
        });

        isGameSetup.addListener((observable, oldValue, newValue) -> {
            logger.info("isGameSetup changed from " + oldValue + " to " + newValue);
            try {
                loadView();
            } catch (Exception e) {
                logger.severe("Error while updating the UI (isGameSetup): " + e.getMessage());
            }
        });

        isGameEnd.addListener((observable, oldValue, newValue) -> {
            logger.info("isGameEnd changed from " + oldValue + " to " + newValue);
            try {
                loadView();
            } catch (Exception e) {
                logger.severe("Error while updating the UI (isGameSetup): " + e.getMessage());
            }

        });
            
        // Genera a placeholder VBox
        rootContainer = new VBox();

        try {
            loadView();
        } catch (Exception e) {
            logger.severe("Error loading FXML file: " + e.getMessage());
            return;
        }
    
        Scene scene = new Scene(rootContainer);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(880);
        primaryStage.show();
    }


    public void loadView() throws Exception {
        rootContainer.getChildren().clear();

        if (!isGameStart.get() && !isGameSetup.get() && !isGameEnd.get()) {
            Pair<Node, Object> content = loadFXML("./views/ArimaaStartView.fxml", arimaaStartController);
            rootContainer.getChildren().add(content.getKey());
        } else if (isGameStart.get() && !isGameSetup.get() && !isGameEnd.get()) {
            Pair<Node, Object> content1 = loadFXML("./views/BoardView.fxml", boardController);
            Pair<Node, Object> content2 = loadFXML("./views/ArimaaSetupView.fxml", arimaaSetupController);
            rootContainer.getChildren().addAll(content1.getKey(), content2.getKey());  
        } else if (isGameStart.get() && isGameSetup.get() && !isGameEnd.get()) {
            Pair<Node, Object> content1 = loadFXML("./views/BoardView.fxml", boardController);
            Pair<Node, Object> content2 = loadFXML("./views/ArimaaGameView.fxml", arimaaGameController);
            rootContainer.getChildren().addAll(content1.getKey(), content2.getKey());
        } else {
            Pair<Node, Object> content = loadFXML("./views/ArimaaEndView.fxml", arimaaEndController);
            rootContainer.getChildren().add(content.getKey());
        }
    }


    private Pair<Node, Object> loadFXML(String fxmlPath, Object controller) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(c -> {
            logger.info("FXMLLoader requested controller of type: " + c.getName());
            return controller;
        });
        loader.setController(controller);
    
        if (controller == null) {
            logger.warning("Controller is null before loading.");
        }
    
        Node root = (Node) loader.load();
    
        Object usedController = loader.getController();
        if (usedController == controller) {
            logger.info("The controller was set correctly.");
        } else {
            logger.warning("The controller was not set correctly.");
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