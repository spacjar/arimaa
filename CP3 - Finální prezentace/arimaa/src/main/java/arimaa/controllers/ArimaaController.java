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
        // Check if the board is not null and if the game is not already initialized
        if (board != null && !isInitialized) {
            // If the setup is not finished, initialize the pieces
            if(!arimaa.getIsSetupFinished()) {
                arimaa.initializePieces();
            }

            // Mark the game as initialized
            isInitialized = true;
            logger.info("Game initialized.");

            // Render the view
            renderView();
        }
    }

    /**
     * Renders the view based on the current state of the Arimaa game.
     * If the setup is not finished, it loads the ArimaaSetupView.
     * Otherwise, it loads the ArimaaView.
     * Sets the necessary properties and initializes the ArimaaController.
     */
    public void renderView() {
        try {
            // Create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            Parent view;
    
            // Set the location of the FXML file to load based on whether the setup is finished
            if (!arimaa.getIsSetupFinished()) {
                loader.setLocation(getClass().getResource("../views/ArimaaSetupView.fxml"));
            } else {
                loader.setLocation(getClass().getResource("../views/ArimaaView.fxml"));
            }
            
            // Load the FXML file
            view = loader.load();

            // Set the loaded view as the bottom of the root pane
            root.setBottom(view);
    
            // Get the controller of the loaded view
            Object controller = loader.getController();
    
            // If the controller is an instance of ArimaaController, set the necessary properties
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

    // Input field for entering the from row number
    @FXML
    private TextField fromRowInput;

    // Input field for entering the from column number
    @FXML
    private TextField fromColInput;

    // Input field for entering the to row number
    @FXML
    private TextField toRowInput;

    // Input field for entering the to column number
    @FXML
    private TextField toColInput;


    /**
     * Submits a game move based on the input coordinates.
     *
     * @throws IllegalArgumentException if there is something wrong with the board checking conditions
     */
    protected void submitGameMove(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) throws IllegalArgumentException {
        logger.info("Submitting a game move.");

        try {
            if(!board.isOccupied(fromRow, fromCol)) {
                throw new IllegalArgumentException("There is no piece at the specified location!");
            }
        
            Piece piece = board.getPieceAt(fromRow, fromCol);

            if (piece.getColor() != arimaa.getCurrentPlayer().getColor()) {
                throw new IllegalArgumentException("You can only move with your own pieces!");
            }

            board.movePiece(fromRow, fromCol, toRow, toCol);

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
                arimaa.switchPlayers();
                arimaa.resetCurrentPlayerMoves();
            }

            // Render the board
            boardController.displayBoard();

            // Set a feedback message
            // feedbackMessage.setText("The selected coordinates are: " + fromRowInputText + ", " + fromColInputText + ", " + toRowInputText + ", " + toColInputText);
        } catch (Exception e) {
            feedbackMessage.setText(e.getMessage());
            logger.severe(e.getMessage());
        }
    }


    /**
     * Skips the turn of the current player.
     * If the current player is the golden player and has made less than 4 moves, the turn is skipped and the current player is set to the silver player.
     * If the current player is the silver player and has made less than 4 moves, the turn is skipped and the current player is set to the golden player.
     * If neither of the above conditions are met, an exception is thrown indicating that the current player cannot skip their move without making at least one move with their pieces.
     *
     * @throws IOException if an I/O error occurs.
     * @throws IllegalArgumentException if the current player tries to skip their move without making any moves with their pieces.
     */
    @FXML
    private void skipTurn() throws IOException, IllegalArgumentException {
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
    // Input field for entering the type of piece during setup
    private TextField pieceTypeInputSetup;
    
    @FXML
    // Input field for entering the row number during setup
    private TextField rowInputSetup;
    
    @FXML
    // Input field for entering the column number during setup
    private TextField colInputSetup;
    
    @FXML
    // Button for submitting the setup
    private Button setupButton;
    
    @FXML
    // Label for displaying feedback messages during setup
    private Label feedbackMessageSetup;
    
    @FXML 
    // Label for displaying the number of available pieces during setup
    private Label availablePiecesSetup;
    
    
    /**
     * Method for setting up the board automatically (in developer mode).
     * This method calls the setupBoardDev method in the boardController to set up the board.
     * It then sets the isSetupFinished flag in the arimaa object to true and renders the view.
     */
    @FXML
    public void devSetup() {
        boardController.setupBoardDev();
        arimaa.setIsSetupFinished(true);
        renderView();
    }
    

    /**
     * Submits the game setup.
     * This method is called when the user submits the setup by clicking a button.
     * It parses the chosen row, column, and piece type from the input fields, places the chosen piece on the board, and updates the game state accordingly.
     * If all pieces for the golden player are placed, it starts the setup for the silver player.
     * If no pieces are left, it changes player and renders the game.
     * If an error occurs during the setup, it displays an error message.
     */
    @FXML
    public void submitSetup() {
        logger.info("Submitting game setup.");
        
        try {
            // Parse the chosen row, column, and piece type from the input fields
            int chosenSetupRow = Integer.parseInt(rowInputSetup.getText())-1;
            int chosenSetupCol = Integer.parseInt(colInputSetup.getText())-1;
            int chosenSetupPieceType = Integer.parseInt(pieceTypeInputSetup.getText());
       
            // Get the chosen piece type
            PieceType chosenPieceType = getChosenPieceType(chosenSetupPieceType);
            logger.info("Chosen piece type: " + chosenPieceType);
    
            // Get the current player
            Player currentPlayer = arimaa.getCurrentPlayer();
            logger.info("Current player: " + currentPlayer);
            
            // Place the chosen piece on the board
            arimaa.setupPiece(currentPlayer, chosenPieceType, chosenSetupRow, chosenSetupCol);
            logger.info("Placed piece on the board.");
    
            // Display the board
            boardController.displayBoard();
            
            // Get the current pieces of the current player
            Map<PieceType, Integer> currentPieces = arimaa.getCurrentPieces(currentPlayer);
            // Check if there are any pieces left
            boolean areCurrentPiecesAvailable = currentPieces.values().stream().anyMatch(count -> count > 0);
            
            // Print the available pieces
            printAvailablePieces(currentPieces);
    
            // If all pieces are placed, finish the setup and change the player
            if (arimaa.areAllPiecesPlaced()) {
                arimaa.setIsSetupFinished(true);
                arimaa.changePlayer(currentPlayer);
                logger.info("All pieces are placed. Setup is finished.");
                renderView();
            }
    
            // If no pieces are left, change the player
            if (!areCurrentPiecesAvailable) {
                arimaa.changePlayer(currentPlayer);
                logger.info("No pieces available. Changing player.");
                return;
            }
    
            // Clear the feedback message
            feedbackMessageSetup.setText("");
        } catch (Exception e) {
            // Display the error message and log it
            feedbackMessageSetup.setText(e.getMessage());
            logger.severe(e.getMessage());
        }
    }


    /**
     * Gets the piece based on the number provided
     * 
     * @param chosenSetupPieceType a piece number
     * @throws IllegalArgumentException the piece with the number provided does not exist
     */
    private PieceType getChosenPieceType(int chosenSetupPieceType) throws IllegalArgumentException {
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


    /**
     * Prints the available pieces to the console.
     *
     * @param currentPieces a map containing the current piece types and their quantities
     */
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
