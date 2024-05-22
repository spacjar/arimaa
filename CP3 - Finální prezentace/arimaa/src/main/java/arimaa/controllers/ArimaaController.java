package arimaa.controllers;

import java.io.IOException;
import java.util.Map;
import arimaa.enums.PieceType;
import arimaa.models.Arimaa;
import arimaa.models.Board;
import arimaa.models.Piece;
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

        try {
            String fromRowInputText = fromRowInput.getText();
            String fromColInputText = fromColInput.getText();
            String toRowInputText = toRowInput.getText();
            String toColInputText = toColInput.getText();

            int fromRowInputNum = Integer.parseInt(fromRowInputText);
            int fromColInputNum = Integer.parseInt(fromColInputText);
            
            if(!board.isOccupied(fromRowInputNum, fromColInputNum)) {
                throw new IllegalArgumentException("There is no piece at the specified location!");
            }
            
            int toRowInputNum = Integer.parseInt(toRowInputText);
            int toColInputNum = Integer.parseInt(toColInputText);

            Piece piece = board.getPieceAt(fromRowInputNum, fromColInputNum);

            if (piece.getColor() != arimaa.getCurrentPlayer().getColor()) {
                throw new IllegalArgumentException("You can only move with your own pieces!");
            }

            board.movePiece(fromRowInputNum, fromColInputNum, toRowInputNum, toColInputNum);

            // Decrement the current player's moves
            arimaa.decrementCurrentPlayerMoves();

            Player otherPlayer = arimaa.getOtherPlayer();

            // Check if one of the players won the game
            if(board.isGameWon(arimaa.getCurrentPlayer())) {
                logger.info(arimaa.getCurrentPlayer() + " won the game!");
                feedbackMessage.setText(arimaa.getCurrentPlayer() + " won the game!");
                arimaa.setIsGameRunning(false);
                return;
            } 
            if(board.isGameWon(otherPlayer)) {
                logger.info(otherPlayer + " won the game!");
                feedbackMessage.setText(otherPlayer + " won the game!");
                arimaa.setIsGameRunning(false);
                return;
            }
            
            // Check if one of the players lost the game
            if(board.isGameLost(arimaa.getCurrentPlayer())) {
                logger.info(arimaa.getCurrentPlayer() + " lost the game!");
                feedbackMessage.setText(arimaa.getCurrentPlayer() + " lost the game!");
                arimaa.setIsGameRunning(false);
                return;
            }
            if(board.isGameLost(otherPlayer)) {
                logger.info(otherPlayer + " lost the game!");
                feedbackMessage.setText(otherPlayer + " lost the game!");
                arimaa.setIsGameRunning(false);
                return;
            }

            // If the current player has no moves left, switch to the other player and reset their moves
            if (arimaa.isCurrentPlayerOutOfMoves()) {
                arimaa.switchPlayer();
                arimaa.resetCurrentPlayerMoves();
            }

            boardController.displayBoard();

            feedbackMessage.setText("The selected coordinates are: " + fromRowInputText + ", " + fromColInputText + ", " + toRowInputText + ", " + toColInputText);
        } catch (Exception e) {
            feedbackMessage.setText(e.getMessage());
            logger.severe(e.getMessage());
        }
        
    }

    @FXML
    private void skipTurn() throws IOException {
        try {
            if (arimaa.getCurrentPlayer() == arimaa.getGoldenPlayer() && arimaa.getGoldenPlayerMoves() < 4) {
                arimaa.setCurrentPlayer(arimaa.getSilverPlayer());
                arimaa.setGoldenPlayerMoves(4);
                logger.info("Turn skipped!");
                feedbackMessage.setText("Turn skipped!");
            } else if (arimaa.getCurrentPlayer() == arimaa.getSilverPlayer() && arimaa.getSilverPlayerMoves() < 4) {
                arimaa.setCurrentPlayer(arimaa.getGoldenPlayer());
                arimaa.setSilverPlayerMoves(4);
                logger.info("Turn skipped!");
                feedbackMessage.setText("Turn skipped!");
            } else {
                logger.warning("Turn was not skipped, move with at least one of your pieces first!");
                throw new IllegalArgumentException("You cannot skip your move when you have not moved with any of your pieces!");
            }  
        } catch (Exception e) {
            feedbackMessage.setText(e.getMessage());
            logger.severe(e.getMessage());
        }
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
    public void devSetup() {
        boardController.setupBoardDev();
        arimaa.setIsSetupFinished(true);
        renderView();
    }

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
                arimaa.changePlayer(currentPlayer);
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
