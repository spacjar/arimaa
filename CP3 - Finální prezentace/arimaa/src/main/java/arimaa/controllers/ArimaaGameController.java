package arimaa.controllers;

import java.io.IOException;

import arimaa.enums.PieceColor;
import arimaa.models.Arimaa;
import arimaa.models.Board;
import arimaa.models.ComputerPlayer;
import arimaa.models.Piece;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import arimaa.models.Player;
import arimaa.utils.FileUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class ArimaaGameController {
    
    private Arimaa arimaa;
    private Board board;
    private BoardController boardController;
    private PlayerTimerController playerTimerController;

    // Setting up the models
    public ArimaaGameController(Arimaa arimaa, Board board) {
        this.arimaa = arimaa;
        this.board = board;
    }
    
    // Logger
    private static final Logger logger = Logger.getLogger(ArimaaGameController.class.getName());
    
    // State
    private boolean isMoved = false;

    // File utils
    FileUtils fileUtils = new FileUtils();

    
    // --- Getters and setters ---
    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setPlayerTimerController(PlayerTimerController playerTimerController) {
        this.playerTimerController = playerTimerController;
    }


    // UI elements
    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Label goldenPlayerMovesLabel;

    @FXML
    private Label silverPlayerMovesLabel;
   
    @FXML
    private Label feedbackMessage;


    // UI init
    @FXML
    public void initialize() {
        if(!arimaa.getIsGameUploaded()) {
            arimaa.setGoldenPlayerMoves(4);
            arimaa.setSilverPlayerMoves(4);
            arimaa.setCurrentPlayer(arimaa.getGoldenPlayer());
        }

        playerTimerController.startGoldenPlayerTimer();
        playerTimerController.stopSilverPlayerTimer();
        updatePlayerInfo();
    }


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
            Player otherPlayer = arimaa.getOtherPlayer(currentPlayer);
    
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
            checkGameStatus(currentPlayer);
            checkGameStatus(otherPlayer);

            // If the current player has no moves left, switch to the other player and reset their moves
            if (arimaa.isCurrentPlayerOutOfMoves()) {
                arimaa.changePlayer(arimaa.getCurrentPlayer());
                arimaa.resetCurrentPlayerMoves();

                // If the current player is a computer player, generate its moves
                // if (arimaa.getCurrentPlayer().getColor() == PieceColor.SILVER && arimaa.getIsPlayingAgainstComputer()) {
                //     logger.info("----------- COMPUTER 1 -----------");
                //     if (arimaa.getCurrentPlayer() instanceof ComputerPlayer) {
                //         logger.info("----------- COMPUTER 2 -----------");
                //         ((ComputerPlayer) arimaa.getCurrentPlayer()).generateMoves();
                //         arimaa.changePlayer(arimaa.getCurrentPlayer());
                //         arimaa.resetCurrentPlayerMoves();
                //     }
                // }

                if (arimaa.getCurrentPlayer().getColor() == PieceColor.GOLDEN) {
                    playerTimerController.stopSilverPlayerTimer();
                    playerTimerController.startGoldenPlayerTimer();
                } else {
                    playerTimerController.stopGoldenPlayerTimer();
                    playerTimerController.startSilverPlayerTimer();
                }

            }

            // Update the game info
            updatePlayerInfo();

            // Render the board
            boardController.displayBoard();
        } catch (Exception e) {
            logger.severe("(!) Arimaa erorre: " + e.getMessage());
            feedbackMessage.setText("ERROR: " + e.getMessage());
        }
    }


    private void updatePlayerInfo() {
        currentPlayerLabel.setText("Current player: " + arimaa.getCurrentPlayer().toString());
        goldenPlayerMovesLabel.setText("Golden player moves: " + String.valueOf(arimaa.getGoldenPlayerMoves()));
        silverPlayerMovesLabel.setText("Silver player moves: " + String.valueOf(arimaa.getSilverPlayerMoves()));
    }


    private void performPushingMove(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol, Boolean isPushingOrPulling ) {
        logger.info("Pushing");
        board.movePiece(fromRow, fromCol, toRow, toCol, isPushingOrPulling);
        arimaa.setPreviousMove(fromRow, fromCol, toRow, toCol);
        arimaa.decrementCurrentPlayerMoves();
    }


    private void checkGameStatus(Player player) {
        if (board.isGameWon(player)) {
            logger.info(player + " won the game!");
            playerTimerController.stopGoldenPlayerTimer();
            playerTimerController.stopSilverPlayerTimer();
            arimaa.setWinner(player);
            arimaa.setIsGameEnd(true);
        } else if (board.isGameLost(player)) {
            Player otherPlayer = arimaa.getOtherPlayer(player);
            logger.info(otherPlayer + " won the game!");
            playerTimerController.stopGoldenPlayerTimer();
            playerTimerController.stopSilverPlayerTimer();
            arimaa.setWinner(otherPlayer);
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
    public void handleSkipTurn(ActionEvent event) throws IOException, IllegalArgumentException {
        try {
            if (arimaa.getIsPushing()) {
                logger.warning("Cannot skip turn while you are pushing or pulling!");
                throw new IllegalArgumentException("Cannot skip turn while you are pushing or pulling!");
            }

            if (arimaa.getCurrentPlayer().equals(arimaa.getGoldenPlayer()) && arimaa.getGoldenPlayerMoves() < 4) {
                arimaa.changePlayer(arimaa.getCurrentPlayer());
                arimaa.resetCurrentPlayerMoves();
                playerTimerController.stopGoldenPlayerTimer();
                playerTimerController.startSilverPlayerTimer();
                updatePlayerInfo();
                logger.info("Turn skipped!");
                feedbackMessage.setText("Turn skipped!");
            } else if (arimaa.getCurrentPlayer().equals(arimaa.getSilverPlayer()) && arimaa.getSilverPlayerMoves() < 4) {
                arimaa.changePlayer(arimaa.getCurrentPlayer());
                arimaa.resetCurrentPlayerMoves();
                playerTimerController.stopSilverPlayerTimer();
                playerTimerController.startGoldenPlayerTimer();
                updatePlayerInfo();
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

    @FXML
    public void handleSaveGame(ActionEvent event) {
        Map<String, Object> gameState = new HashMap<>();
        gameState.put("isGameStart", arimaa.getIsGameStart());
        gameState.put("isGameSetup", arimaa.getIsGameSetup());
        gameState.put("isGameEnd", arimaa.getIsGameEnd());
        gameState.put("isPlayerPlayingAgainstHuman", arimaa.getIsPlayingAgainstHuman());
        gameState.put("isPlayerPlayingAgainstComputer", arimaa.getIsPlayingAgainstComputer());
        gameState.put("currentPlayer", arimaa.getCurrentPlayer());
        gameState.put("goldenPlayerMoves", arimaa.getGoldenPlayerMoves());
        gameState.put("silverPlayerMoves", arimaa.getSilverPlayerMoves());
        gameState.put("isPushing", arimaa.getIsPushing());
        gameState.put("previousMove", arimaa.getPreviousMove());
        gameState.put("board", board.getBoard());

        try {
            // String filenameWithUUID = "arimaaGameState_" + UUID.randomUUID().toString() + ".json";
            String filenameWithUUID = "arimaaGameState.json";
            fileUtils.saveGame(filenameWithUUID, gameState);
        } catch (Exception e) {
            feedbackMessage.setText("Error while saving file: " + e.getMessage());
            logger.severe("(!) Error while saving: " + e.getMessage());
        }
    }
}
