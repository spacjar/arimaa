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
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ArimaaController {
    
    private Arimaa arimaa;
    private Board board;
    private BoardController boardController;

    // Setting up the models
    public ArimaaController(Arimaa arimaa, Board board) {
        this.arimaa = arimaa;
        this.board = board;
    }
    
    // Logger
    private static final Logger logger = Logger.getLogger(ArimaaController.class.getName());
    
    // State
    private boolean isInitialized = false;
    private boolean isMoved = false;


    // --- Getters and setters ---

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    

    // ---------- Init ----------
    // private final StringProperty currentPlayerProperty = new SimpleStringProperty();

    // private void updateCurrentPlayer(Player player) {
    //     currentPlayerProperty.set("Current player: " + player);
    // }

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Label goldenPlayerMovesLabel;

    @FXML
    private Label silverPlayerMovesLabel;

    @FXML
    public void initialize() {
        // Check if the board is not null and if the game is not already initialized
        if (board != null && !isInitialized) {
            // If the setup is not finished, initialize the pieces
            // if(!arimaa.getIsSetupFinished()) {
            if(!arimaa.getIsGameStart()) {
                arimaa.initializePieces();
            }
            
            // Mark the game as initialized
            isInitialized = true;
            logger.info("Game initialized.");
        }
    }






    /**
     * Renders the view based on the current state of the Arimaa game.
     * If the setup is not finished, it loads the ArimaaSetupView.
     * Otherwise, it loads the ArimaaView.
     * Sets the necessary properties and initializes the ArimaaController.
     */
    // public void renderView() {
    //     try {
    //         // Create a new FXMLLoader
    //         FXMLLoader loader = new FXMLLoader();
    //         Parent view;
    
    //         // Set the location of the FXML file to load based on whether the setup is finished
    //         if (!arimaa.getIsSetupFinished()) {
    //             loader.setLocation(getClass().getResource("../views/ArimaaSetupView.fxml"));
    //         } else {
    //             loader.setLocation(getClass().getResource("../views/ArimaaView.fxml"));
    //         }
            
    //         // Load the FXML file
    //         view = loader.load();

    //         // Set the loaded view as the bottom of the root pane
    //         root.setBottom(view);
    
    //         // Get the controller of the loaded view
    //         Object controller = loader.getController();
    
    //         // If the controller is an instance of ArimaaController, set the necessary properties
    //         if (controller instanceof ArimaaController) {
    //             ArimaaController arimaaController = (ArimaaController) controller;
    //             // arimaaController.setBoard(board);
    //             arimaaController.setBoardController(boardController);
    //             // arimaaController.setArimaa(arimaa);
    //             arimaaController.setRoot(root);
    //             arimaaController.setStage(stage);
    //             if (!isInitialized) {
    //                 arimaaController.initialize();
    //             }
    //         }
    //     } catch (IOException e) {
    //         logger.severe("(!) ERROR: " + e.getMessage());
    //     }
    // }



    // ---------- Game ----------    
    @FXML
    private Label feedbackMessage;


    /**
     * Submits a game move based on the input coordinates.
     *
     * @throws IllegalArgumentException if there is something wrong with the board checking conditions
     */
    public void submitGameMove(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        logger.info("Submitting a game move.");

        try {
            // Reset the values
            isMoved = false;

            // Check if there is a piece at the selected "from" position
            if(!board.isOccupied(fromRow, fromCol)) {
                throw new IllegalArgumentException("There is no piece at the specified location!");
            }

            // Check if the "to" position is already occupied by another piece
            if(board.isOccupied(toRow, toCol)) {
                throw new IllegalArgumentException("There is already piece at the new specified location!");
            }

            // Get the players
            Player currentPlayer = arimaa.getCurrentPlayer();
            Player otherPlayer = arimaa.getOtherPlayer();
    
            // Get the piece at the currently selected position
            Piece currentPiece = board.getPieceAt(fromRow, fromCol);
            
            // Previous piece position
            Integer[] previousMove = arimaa.getPreviousMove();
            Integer previousMoveFromRow = previousMove[0];
            Integer previousMoveFromCol = previousMove[1];
            Integer previousMoveToRow = previousMove[2];
            Integer previousMoveToCol = previousMove[3];

            // Get the piece that was at the previous position
            Piece previousPiece = null;

            if(previousMoveToRow != null && previousMoveToCol != null){
                previousPiece = board.getPieceAt(previousMoveToRow, previousMoveToCol);
            }


            // PUSH
            // 1. Current player chooses the position of the enemy piece
            // 2. The enemy piece (selected piece) checks if there are any adjacent current players pieces with bigger weight
            // 3. The enemy piece (selected piece) checks if there are any free adjacent spaces
            // 4. The enemy piece moves to the free adjacent space and its previous location (where it moved from) is noted
            // 5. The current player can only move the previously adjacent pieces with a bigger weight (than the moved enemy piece)
            // 6. The current player can only move these pieces to the previous location of the enemy piece
            // NOTE: While you are pushing an enemy piece (2 moves) you cannot skip the round
            
            // Check that you can move your friendly piece only to the previous location of the enemy piece
            if(arimaa.getIsPushing() && (toRow != previousMoveFromRow || toCol != previousMoveFromCol)) {
                logger.warning("You can move only to the position of the previous enemy piece!");
                throw new IllegalArgumentException("You can move only to the position of the previous enemy piece!");
            }

            if(arimaa.getIsPushing() && (toRow == previousMoveFromRow || toCol == previousMoveFromCol)) {
                if(isMoved == false) {
                    arimaa.setIsPushing(false);
                    performPushingMove(fromRow, fromCol, toRow, toCol, false);
                    isMoved = true;
                }
            }

            
            if (currentPiece.getColor() != arimaa.getCurrentPlayer().getColor()) {
                if(board.hasAdjacentEnemyPiecesWithHigherValue(fromRow, fromCol) && arimaa.getPlayersMoves(currentPlayer) >= 2) {
                    if(isMoved == false) {
                        logger.info("---------------- Pushing ----------------");
                        arimaa.setIsPushing(true);
                        performPushingMove(fromRow, fromCol, toRow, toCol, true);
                        isMoved = true;
                    }
                }
            }


            // PULL
            // 1. Current player chooses and moves his piece
            // 2. Current player chooses an enemy piece he would like to pull
            // 3. The enemy piece checks if the piece at the previous position (the players piece that the player moved at 1.) has bigger weight
            // 4. The enemy piece can ONLY move to the previous position of the previously adjacent piece with bigger weight
            // NOTE: Unlike push, you can skip during this move, because you don't know if the player is just moving his piece with bigger weight from an enemy piece, or if he is trying to pull
            if (!arimaa.getIsPushing() && currentPiece.getColor() != arimaa.getCurrentPlayer().getColor()) {
                if(currentPiece != null && previousPiece != null && previousPiece.getPieceWeight() > currentPiece.getPieceWeight()) {
                    // Check if the pieces are adjacent
                    if(board.isOneStep(fromRow, fromCol, previousMoveFromRow, previousMoveFromCol)) {
                        // 4. The enemy piece can ONLY move to the previous position of the previously adjacent piece with bigger weight
                        if(previousMoveFromRow == toRow && previousMoveFromCol == toCol) {
                            if(isMoved == false) {
                                logger.info("---------------- Pulling ----------------");
                                performPushingMove(fromRow, fromCol, toRow, toCol, true);
                                isMoved = true;
                            }
                        } else {
                            logger.warning("You can pull the enemy piece only to the position of your previous piece!");
                            throw new IllegalArgumentException("You can pull the enemy piece only to the position of your previous piece!");
                        }
                    }
                }
            }
    
    
            if (currentPiece.getColor() == arimaa.getCurrentPlayer().getColor()) {
                if(isMoved == false) {
                    performPushingMove(fromRow, fromCol, toRow, toCol, false);
                    isMoved = true;
                }
            }

            // Decrement the current player's moves
            // arimaa.decrementCurrentPlayerMoves();

            // Check if one of the players won or lost the game
            checkGameStatus(currentPlayer, "won");
            checkGameStatus(otherPlayer, "won");
            checkGameStatus(currentPlayer, "lost");
            checkGameStatus(otherPlayer, "lost");

            // If the current player has no moves left, switch to the other player and reset their moves
            if (arimaa.isCurrentPlayerOutOfMoves()) {
                arimaa.changePlayer(arimaa.getCurrentPlayer());
                arimaa.resetCurrentPlayerMoves();
            }

            // Render the board
            boardController.displayBoard();
        } catch (Exception e) {
            logger.severe("(!) Arimaa erorre: " + e.getMessage());
            feedbackMessage.setText("ERROR: " + e.getMessage());
        }
    }

    private void performPushingMove(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol, Boolean isPushingOrPulling ) {
        logger.info("Pushing");
        board.movePiece(fromRow, fromCol, toRow, toCol, isPushingOrPulling);
        arimaa.setPreviousMove(fromRow, fromCol, toRow, toCol);
        arimaa.decrementCurrentPlayerMoves();
    }

    private void checkGameStatus(Player player, String status) {
        if ((status.equals("won") && board.isGameWon(player)) || (status.equals("lost") && board.isGameLost(player))) {
            logger.info(player + " " + status + " the game!");
            feedbackMessage.setText(player + " " + status + " the game!");
            // arimaa.setIsGameRunning(false);
            arimaa.setIsGameEnd(true);
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
            if (arimaa.getIsPushing() || arimaa.getIsPulling()) {
                logger.warning("Cannot skip turn while you are pushing or pulling!");
                throw new IllegalArgumentException("Cannot skip turn while you are pushing or pulling!");
            }

            if (arimaa.getCurrentPlayer() == arimaa.getGoldenPlayer() && arimaa.getGoldenPlayerMoves() < 4) {
                arimaa.changePlayer(arimaa.getCurrentPlayer());
                arimaa.resetCurrentPlayerMoves();
                logger.info("Turn skipped!");
                feedbackMessage.setText("Turn skipped!");
            } else if (arimaa.getCurrentPlayer() == arimaa.getSilverPlayer() && arimaa.getSilverPlayerMoves() < 4) {
                arimaa.changePlayer(arimaa.getCurrentPlayer());
                arimaa.resetCurrentPlayerMoves();
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
        // arimaa.setIsSetupFinished(true);
        arimaa.setIsGameSetup(true);
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
            
            // Get the current pieces of the current player
            Map<PieceType, Integer> currentPieces = arimaa.getCurrentPieces(currentPlayer);
            // Check if there are any pieces left
            boolean areCurrentPiecesAvailable = currentPieces.values().stream().anyMatch(count -> count > 0);
            
            // Print the available pieces
            printAvailablePieces(currentPieces);
    
            // If all pieces are placed, finish the setup and change the player
            if (arimaa.areAllPiecesPlaced()) {
                // arimaa.setIsSetupFinished(true);
                arimaa.setIsGameSetup(true);
                arimaa.changePlayer(currentPlayer);
                logger.info("All pieces are placed. Setup is finished.");
                // renderView();
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
            feedbackMessage.setText(e.getMessage());
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
