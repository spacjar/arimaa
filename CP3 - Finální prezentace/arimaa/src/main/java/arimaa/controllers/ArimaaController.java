package arimaa.controllers;

import java.io.IOException;
import java.util.Map;
import arimaa.enums.PieceType;
import arimaa.models.Arimaa;
import arimaa.models.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import arimaa.models.Player;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ArimaaController {
    
    private boolean isInitialized = false;
    private Board board;
    private BoardController boardController;
    private Arimaa arimaa;
    private Stage stage;
    private BorderPane root;
    
    // Logger
    private static final Logger logger = Logger.getLogger(ArimaaController.class.getName());


    public void setBoard(Board board) {
        this.board = board;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setArimaa(Arimaa arimaa) {
        this.arimaa = arimaa;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    
    // ---------- Init ----------
    @FXML
    public void initialize() {
        if (board != null && !isInitialized) {
            if(!arimaa.getIsSetupFinished()) {
                arimaa.initializePieces();
            }

            isInitialized = true;
            logger.info("Game initialized.");
            renderView();
        }
    }

    public void renderView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent view;
    
            if (!arimaa.getIsSetupFinished()) {
                loader.setLocation(getClass().getResource("../views/ArimaaSetupView.fxml"));
            } else {
                loader.setLocation(getClass().getResource("../views/ArimaaView.fxml"));
            }
    
            view = loader.load();
            root.setBottom(view);
    
            Object controller = loader.getController();
    
            if (controller instanceof ArimaaController) {
                ArimaaController arimaaController = (ArimaaController) controller;
                arimaaController.setBoard(board);
                arimaaController.setBoardController(boardController);
                arimaaController.setArimaa(arimaa);
                arimaaController.setRoot(root);
                arimaaController.setStage(stage);
                if (!isInitialized) {
                    arimaaController.initialize();
                }
            }
        } catch (IOException e) {
            logger.severe("(!) ERROR: " + e.getMessage());
        }
    }


    // ---------- Game ----------    
    @FXML
    private Label feedbackMessage;

    @FXML
    private TextField fromRowInput;

    @FXML
    private TextField fromColInput;

    @FXML
    private TextField toRowInput;

    @FXML
    private TextField toColInput;

    @FXML
    private void submitGame() throws IOException {
        logger.info("Submitting a game move.");

        String fromRowInputText = fromRowInput.getText();
        String fromColInputText = fromColInput.getText();
        String toRowInputText = toRowInput.getText();
        String toColInputText = toColInput.getText();

        int fromRowInputNum = Integer.parseInt(fromRowInputText);
        int fromColInputNum = Integer.parseInt(fromColInputText);
        int toRowInputNum = Integer.parseInt(toRowInputText);
        int toColInputNum = Integer.parseInt(toColInputText);

        board.movePiece(fromRowInputNum, fromColInputNum, toRowInputNum, toColInputNum);

        boardController.displayBoard();

        feedbackMessage.setText("The selected coordinates are: " + fromRowInputText + ", " + fromColInputText + ", " + toRowInputText + ", " + toColInputText);
    }


    // ---------- Game setup ----------
    @FXML
    private Label pieceTypeLabelSetup;

    @FXML
    private TextField pieceTypeInputSetup;

    @FXML
    private Label rowLabelSetup;

    @FXML
    private TextField rowInputSetup;

    @FXML
    private Label colLabelSetup;

    @FXML
    private TextField colInputSetup;

    @FXML
    private Button setupButton;

    @FXML
    private Label feedbackMessageSetup;

    @FXML 
    private Label availablePiecesSetup;

    @FXML
    public void submitSetup() {
        logger.info("Submitting game setup.");
        
        try {
            int chosenSetupRow = Integer.parseInt(rowInputSetup.getText());
            int chosenSetupCol = Integer.parseInt(colInputSetup.getText());
            int chosenSetupPieceType = Integer.parseInt(pieceTypeInputSetup.getText());
   
            PieceType chosenPieceType = getChosenPieceType(chosenSetupPieceType);
            logger.info("Chosen piece type: " + chosenPieceType);

            Player currentPlayer = arimaa.getCurrentPlayer();
            logger.info("Current player: " + currentPlayer);
            
            arimaa.placePiece(currentPlayer, chosenPieceType, chosenSetupRow, chosenSetupCol);
            logger.info("Placed piece on the board.");

            boardController.displayBoard();
            
            Map<PieceType, Integer> currentPieces = arimaa.getCurrentPieces(currentPlayer);
            boolean areCurrentPiecesAvailable = currentPieces.values().stream().anyMatch(count -> count > 0);
            
            printAvailablePieces(currentPieces);

            if (arimaa.areAllPiecesPlaced()) {
                arimaa.setIsSetupFinished(true);
                logger.info("All pieces are placed. Setup is finished.");
                renderView();
            }

            if (!areCurrentPiecesAvailable) {
                arimaa.changePlayer(currentPlayer);
                logger.info("No pieces available. Changing player.");
                return;
            }

            feedbackMessageSetup.setText("");
        } catch (Exception e) {
            feedbackMessageSetup.setText(e.getMessage());
            logger.severe(e.getMessage());
        }
    }

    private PieceType getChosenPieceType(int chosenSetupPieceType) {
        logger.info("Getting chosen piece type: " + chosenSetupPieceType);
        switch(chosenSetupPieceType) {
            case 1: 
                return PieceType.RABBIT;
            case 2: 
                return PieceType.CAT;
            case 3: 
                return PieceType.DOG;
            case 4: 
                return PieceType.HORSE;
            case 5: 
                return PieceType.CAMEL;
            case 6: 
                return PieceType.ELEPHANT;
            default:    
                throw new IllegalArgumentException("The piece type with the number " + chosenSetupPieceType + " does not exist!" );
        }
    }

    private void printAvailablePieces(Map<PieceType, Integer> currentPieces) {
        logger.info("Printing available pieces.");
        StringBuilder piecesString = new StringBuilder("Currently available pieces: ");
        for (Map.Entry<PieceType, Integer> entry : currentPieces.entrySet()) {
            piecesString.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        // Remove the last comma and space
        if (piecesString.length() > 0) {
            piecesString.setLength(piecesString.length() - 2);
        }
        availablePiecesSetup.setText(piecesString.toString());
    }
}
