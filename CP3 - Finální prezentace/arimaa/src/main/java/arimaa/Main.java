package arimaa;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.Node;

import arimaa.controllers.ArimaaGameController;
import arimaa.controllers.ArimaaGameRecorderController;
import arimaa.controllers.ArimaaSetupController;
import arimaa.controllers.ArimaaEndController;
import arimaa.controllers.ArimaaStartController;
import arimaa.controllers.BoardController;
import arimaa.controllers.PlayerTimerController;

import arimaa.models.Arimaa;
import arimaa.models.ArimaaGameRecorder;
import arimaa.models.Board;
import arimaa.models.PlayerTimer;


public class Main extends Application {
    ArimaaStartController arimaaStartController;
    ArimaaSetupController arimaaSetupController;
    ArimaaGameController arimaaGameController;
    ArimaaEndController arimaaEndController;
    BoardController boardController;
    ArimaaGameRecorderController arimaaGameRecorderController;
    PlayerTimerController playerTimerController;

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private BooleanProperty isGameStart;
    private BooleanProperty isGameSetup;
    private BooleanProperty isGameEnd;

    private HBox rootContainer = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Models
        ArimaaGameRecorder arimaaGameRecorder = new ArimaaGameRecorder();
        Board board = new Board(arimaaGameRecorder);
        Arimaa arimaa = new Arimaa(board);
        PlayerTimer goldenPlayerTimer = new PlayerTimer();
        PlayerTimer silverPlayerTimer = new PlayerTimer();

        // Controllers
        arimaaStartController = new ArimaaStartController(arimaa, board);
        arimaaSetupController = new ArimaaSetupController(arimaa, board, arimaaGameRecorder);
        arimaaGameController = new ArimaaGameController(arimaa, board, arimaaGameRecorder);
        arimaaEndController = new ArimaaEndController(arimaa);
        boardController = new BoardController(arimaa, board);
        playerTimerController = new PlayerTimerController(goldenPlayerTimer, silverPlayerTimer);
        arimaaGameRecorderController = new ArimaaGameRecorderController(arimaaGameRecorder);

        arimaaSetupController.setBoardController(boardController);
        arimaaGameController.setBoardController(boardController);
        arimaaGameController.setPlayerTimerController(playerTimerController);
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
        rootContainer = new HBox();
        rootContainer.setAlignment(Pos.CENTER);
        rootContainer.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");

        try {
            loadView();
        } catch (Exception e) {
            logger.severe("Error loading FXML file: " + e.getMessage());
            return;
        }
    
        Scene scene = new Scene(rootContainer);
        primaryStage.setScene(scene);
        // primaryStage.setMinWidth(1200);
        // primaryStage.setMinHeight(960);
        primaryStage.show();
    }


    public void loadView() throws Exception {
        rootContainer.getChildren().clear();

        if (!isGameStart.get() && !isGameSetup.get() && !isGameEnd.get()) {
            Pair<Node, Object> startView = loadFXML("./views/ArimaaStartView.fxml", arimaaStartController);
            VBox layoutBox = new VBox();
            layoutBox.getChildren().addAll(startView.getKey());
            rootContainer.getChildren().addAll(layoutBox);
        } else if (isGameStart.get() && !isGameSetup.get() && !isGameEnd.get()) {    
            Pair<Node, Object> boardView = loadFXML("./views/BoardView.fxml", boardController);
            Pair<Node, Object> setupView = loadFXML("./views/ArimaaSetupView.fxml", arimaaSetupController);
            VBox layoutBox = new VBox();
            layoutBox.getChildren().addAll(boardView.getKey(), setupView.getKey());
            rootContainer.getChildren().addAll(layoutBox);  
        } else if (isGameStart.get() && isGameSetup.get() && !isGameEnd.get()) {
            Pair<Node, Object> boardView = loadFXML("./views/BoardView.fxml", boardController);
            Pair<Node, Object> gameRecorder = loadFXML("./views/ArimaaGameRecorderView.fxml", arimaaGameRecorderController);
            
            HBox boardViewAndGameRecorder = new HBox();
            boardViewAndGameRecorder.getChildren().addAll(boardView.getKey(), gameRecorder.getKey());
            
            Pair<Node, Object> timerView = loadFXML("./views/PlayerTimerView.fxml", playerTimerController);
            Pair<Node, Object> gameView = loadFXML("./views/ArimaaGameView.fxml", arimaaGameController);

            VBox layoutBox = new VBox();
            layoutBox.getChildren().addAll(boardViewAndGameRecorder, timerView.getKey(), gameView.getKey());
            rootContainer.getChildren().addAll(layoutBox);
        } else {
            Pair<Node, Object> endView = loadFXML("./views/ArimaaEndView.fxml", arimaaEndController);
            VBox layoutBox = new VBox();
            layoutBox.getChildren().addAll(endView.getKey());
            rootContainer.getChildren().addAll(layoutBox);
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