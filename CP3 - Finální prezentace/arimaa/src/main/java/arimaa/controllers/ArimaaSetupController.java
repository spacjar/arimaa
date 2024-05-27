package arimaa.controllers;

import java.util.logging.Logger;

import arimaa.enums.PieceColor;
import arimaa.models.Arimaa;
import arimaa.models.Board;
import arimaa.models.Piece;
import arimaa.models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ArimaaSetupController {
    private Arimaa arimaa;
    private Board board;
    private BoardController boardController;
    
    public ArimaaSetupController(Arimaa arimaa, Board board) {
        this.arimaa = arimaa;
        this.board = board;
    }

    // Logger
    private static final Logger logger = Logger.getLogger(ArimaaSetupController.class.getName());

    // State
    private boolean isGoldenPlayerSetupReady = false;
    private boolean isSilverPlayerSetupReady = false;


    // --- Getters and setters ---
    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    // UI Elements
    @FXML
    private Label setupCurrentPlayerLabel; 
    @FXML
    private Label setupfeedbackMessage; 
    @FXML
    private Button setupGoldenPlayerReadyButton;
    @FXML
    private Button setupSilverPlayerReadyButton;
    @FXML
    private Button setupAllPlayersReadyButton;


    // Init
    @FXML
    public void initialize() {
        logger.info("Setup controller initialized");
        if(!arimaa.getIsGameUploaded()) {
            isGoldenPlayerSetupReady = false;
            isSilverPlayerSetupReady = false;
            setupGoldenPlayerReadyButton.setDisable(false);
            setupSilverPlayerReadyButton.setDisable(false);
            board.clearBoard();
            boardController.generateBoardPieceSetup();
            boardController.displayBoard();
            arimaa.setCurrentPlayer(arimaa.getGoldenPlayer());
            displayCurrentPlayer();
        }
    }


    @FXML
    public void handleGoldenPlayerReady(ActionEvent event) {
        if(!isGoldenPlayerSetupReady && !isSilverPlayerSetupReady) {
            logger.info("Golden player setup is ready");
            isGoldenPlayerSetupReady = true;
            setupGoldenPlayerReadyButton.setDisable(true);
            arimaa.setCurrentPlayer(arimaa.getSilverPlayer());
            displayCurrentPlayer();
        }
    }


    @FXML
    public void handleSilverPlayerReady(ActionEvent event) {
        if(isGoldenPlayerSetupReady && !isSilverPlayerSetupReady) {
            logger.info("Silver player setup is ready");
            isSilverPlayerSetupReady = true;
            setupSilverPlayerReadyButton.setDisable(true);
            arimaa.setCurrentPlayer(arimaa.getGoldenPlayer());
        }
    }
    

    @FXML
    public void handleStartGame(ActionEvent event) {
        if(isGoldenPlayerSetupReady && isSilverPlayerSetupReady) {
            logger.info("Ready to start!");
            arimaa.setIsGameSetup(true);
        }
    }


    private void displayCurrentPlayer() {
        setupCurrentPlayerLabel.setText("Current player: " + arimaa.getCurrentPlayer().toString());
    }


    /**
     * Swaps the pieces on the game board between the specified "from" position and the specified "to" position.
     * 
     * @param fromRow The row index of the "from" position.
     * @param fromCol The column index of the "from" position.
     * @param toRow The row index of the "to" position.
     * @param toCol The column index of the "to" position.
     */
    public void swapPieces(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        logger.info("Swapping pieces.");
        
        setupfeedbackMessage.setText("");

        try {
            // Check if there is a piece at the selected "from" position
            if(!board.isOccupied(fromRow, fromCol)) {
                throw new IllegalArgumentException("There is no piece at the specified location!");
            }

            // Check if the "to" position is already occupied by another piece
            if(!board.isOccupied(toRow, toCol)) {
                throw new IllegalArgumentException("There is no piece at the new specified location!");
            }

            // Get the player
            Player currentPlayer = arimaa.getCurrentPlayer();

            // Get the pieces at the selected positions
            Piece fromPiece = board.getPieceAt(fromRow, fromCol);
            Piece toPiece = board.getPieceAt(toRow, toCol);

            // Check if the pieces belong to the current player
            if(fromPiece.getColor() != currentPlayer.getColor() || toPiece.getColor() != currentPlayer.getColor()) {
                throw new IllegalArgumentException("Both pieces must belong to the current player!");
            }

            // Check if the pieces are on the correct rows
            if(currentPlayer.getColor() == PieceColor.GOLDEN && fromRow > 1 && toRow > 1 && fromRow < 0 && toRow < 0) {
                throw new IllegalArgumentException("Golden player can only swap pieces on rows 1 and 2!");
            }
            if(currentPlayer.getColor() == PieceColor.SILVER && fromRow < 6 && toRow < 6 && fromRow > 7 && toRow > 7) {
                throw new IllegalArgumentException("Silver player can only swap pieces on rows 7 and 8!");
            }

            // Swap the pieces
            board.removePiece(fromRow, fromCol);
            board.removePiece(toRow, toCol);
            board.setPiece(fromPiece, toRow, toCol);
            board.setPiece(toPiece, fromRow, fromCol);

            // Render the board
            boardController.displayBoard();
        } catch (Exception e) {
            logger.severe("(!) Setup Error: " + e.getMessage());
            setupfeedbackMessage.setText("(!) Error: " + e.getMessage());
        }
    }
}
