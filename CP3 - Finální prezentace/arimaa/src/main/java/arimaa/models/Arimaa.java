package arimaa.models;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.utils.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Arimaa {
    // Game board
    private Board board;

    // Game state
    private boolean isSetupFinished = false;
    private boolean isGameRunning = false;

    // Golden player
    private Player goldenPlayer = new Player(PieceColor.GOLDEN);
    private int goldenPlayerMoves = 4;

    // Silver player
    private Player silverPlayer = new Player(PieceColor.SILVER);
    private int silverPlayerMoves = 4;

    // Current player (first is always the golden player)
    private Player currentPlayer = goldenPlayer;

    private Map<PieceType, Integer> goldenPieces = new HashMap<>();
    private Map<PieceType, Integer> silverPieces = new HashMap<>();
    private Map<PieceType, Integer> currentPieces = currentPlayer.getColor() == PieceColor.GOLDEN ? goldenPieces : silverPieces;
    private boolean allGoldenPiecesPlaced = false;
    private boolean allSilverPiecesPlaced = false;


    // ----- Getters and setters -----
    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean getIsSetupFinished() {
        return isSetupFinished;
    }
    
    public void setIsSetupFinished(boolean isSetupFinished) {
        this.isSetupFinished = isSetupFinished;
    }
    
    public boolean getIsGameRunning() {
        return isGameRunning;
    }
    
    public void setIsGameRunning(boolean isGameRunning) {
        this.isGameRunning = isGameRunning;
    }
    
    public Player getGoldenPlayer() {
        return goldenPlayer;
    }
    
    public void setGoldenPlayer(Player goldenPlayer) {
        this.goldenPlayer = goldenPlayer;
    }
    
    public int getGoldenPlayerMoves() {
        return goldenPlayerMoves;
    }
    
    public void setGoldenPlayerMoves(int goldenPlayerMoves) {
        this.goldenPlayerMoves = goldenPlayerMoves;
    }
    
    public Player getSilverPlayer() {
        return silverPlayer;
    }
    
    public void setSilverPlayer(Player silverPlayer) {
        this.silverPlayer = silverPlayer;
    }
    
    public int getSilverPlayerMoves() {
        return silverPlayerMoves;
    }
    
    public void setSilverPlayerMoves(int silverPlayerMoves) {
        this.silverPlayerMoves = silverPlayerMoves;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOtherPlayer() {
        return (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }
    
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Map<PieceType, Integer> getCurrentPieces(Player player) {
        return player.getColor() == PieceColor.GOLDEN ? goldenPieces : silverPieces;
    }

    public void setCurrentPieces(Player player) {
        this.currentPieces = getCurrentPieces(player);
    }

    public void changePlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer.getColor() == PieceColor.GOLDEN ? silverPlayer : goldenPlayer;
    }

    public void decrementCurrentPlayerMoves() {
        if (currentPlayer == goldenPlayer) {
            goldenPlayerMoves--;
        } else if (currentPlayer == silverPlayer) {
            silverPlayerMoves--;
        }
    }

    public boolean isCurrentPlayerOutOfMoves() {
        return (currentPlayer == goldenPlayer && goldenPlayerMoves == 0) || (currentPlayer == silverPlayer && silverPlayerMoves == 0);
    }
    

    
    public void resetCurrentPlayerMoves() {
        if (currentPlayer == goldenPlayer) {
            goldenPlayerMoves = 4;
        } else {
            silverPlayerMoves = 4;
        }
    }


    
    /**
     * Sets up a player's piece on the game board at the specified position.
     *
     * @param player the player whose pieces are being set up
     * @param piecesCount a map containing the number of each piece type the player has
     * @param chosenPieceType the type of piece to be placed on the board
     * @param row the row index of the position
     * @param col the row index of the position
     * @throws IllegalArgumentException if the position is invalid, already occupied, or if there are no more pieces of the selected type available
     */
    private void setupPlayerPieces(Player player, Map<PieceType, Integer> piecesCount, PieceType chosenPieceType, int row, int col) throws IllegalArgumentException {
        if (player.getColor() == PieceColor.GOLDEN && row > 1) {
            throw new IllegalArgumentException("Golden player can only place pieces at row 0 or 1!");
        }
    
        if (player.getColor() == PieceColor.SILVER && row < 6) {
            throw new IllegalArgumentException("Silver player can only place pieces at row 6 or 7!");
        }
    
        if (board.getPieceAt(row, col) != null) {
            throw new IllegalArgumentException("There is already a piece at the selected position!");
        }
    
        if (piecesCount.get(chosenPieceType) <= 0) {
            throw new IllegalArgumentException("There are no more pieces of the selected type are available!");
        }
    
        Piece piece = new Piece(chosenPieceType, player.getColor());
        board.setPiece(piece, row, col);
        piecesCount.put(chosenPieceType, piecesCount.get(chosenPieceType) - 1);
    }

    
    /**
     * Sets up the game by initializing the piece counts, allowing players to place their pieces on the board, and checking if all pieces have been placed for both players.
     * 
     * This method loops until all pieces have been placed for both players. It prompts the current player to choose
     * a piece type and a location on the board to place the piece. The method also checks if the chosen piece type
     * is valid and if the chosen location is available. If any errors occur during the setup process, an error message
     * is displayed.
     * 
     * After all pieces have been placed, the method sets the game control value of the setup as finished.
     */
    public void initializePieces() {
        for (PieceType type : PieceType.values()) {
            int count = 0;

            if (type == PieceType.ELEPHANT || type == PieceType.CAMEL) {
                count = 1;
            } else if (type == PieceType.HORSE || type == PieceType.DOG || type == PieceType.CAT) {
                count = 2;
            } else if (type == PieceType.RABBIT) {
                count = 8;
            }

            goldenPieces.put(type, count);
            silverPieces.put(type, count);
        }
    }


    public boolean areAllPiecesPlaced() {
        return allGoldenPiecesPlaced && allSilverPiecesPlaced;
    }


    public void placePiece(Player currentPlayer, PieceType chosenPieceType, int chosenRow, int chosenCol) {
        setCurrentPieces(currentPlayer);
        
        setupPlayerPieces(currentPlayer, currentPieces, chosenPieceType, chosenRow, chosenCol);


        allGoldenPiecesPlaced = !goldenPieces.values().stream().anyMatch(count -> count > 0);
        allSilverPiecesPlaced = !silverPieces.values().stream().anyMatch(count -> count > 0);
    }


    public void startGame() {
        // Set the game control value of the phase as running
        isGameRunning = true;

        // Reset the current player to be golden player, since he always starts the game
        currentPlayer = goldenPlayer;
        // ----- DEBUG game init end -----


        // ---- Game init logic ----
        if(isSetupFinished == false) {
            // setupGame();
        }

        System.out.println("\n***** - | Game | - *****\n");

        // ---- Game logic ----
        while (isGameRunning) {
            try {
                // Print the game status
                // ! board.printBoard();
                System.out.println("Current player: " + currentPlayer);
                System.out.println("Golden player moves left: " + goldenPlayerMoves);
                System.out.println("Silver player moves left: " + silverPlayerMoves);

                // Get the from row number or skip the turn 
                Integer fromRow = InputUtils.handlePositionInput("Select the row where is the piece you would like to move or type 'skip' to skip your turn: ");
                if (fromRow == null) {
                    handleTurnSkip();
                    continue;
                }
                
                // Get the from col number or skip the turn
                Integer fromCol = InputUtils.handlePositionInput("Select the column where is the piece you would like to move or type 'skip' to skip your turn: ");
                if (fromCol == null) {
                    handleTurnSkip();
                    continue;
                }
                
                // Check if the piece to be moved exists
                if(!board.isOccupied(fromRow, fromCol)) {
                    throw new IllegalArgumentException("There is no piece at the specified location!");
                }

                // Get the piece that is going to be moved
                Piece piece = board.getPieceAt(fromRow, fromCol);

                // Check if the player can move the piece based on its color
                if (piece.getColor() != currentPlayer.getColor()) {
                    throw new IllegalArgumentException("You can only move with your own pieces!");
                }
        
                // Get the to row number or skip the turn
                Integer toRow = InputUtils.handlePositionInput("Select the row where you would like to move the piece or type 'skip' to skip your turn: ");
                if (toRow == null) {
                    handleTurnSkip();
                    continue;
                }

                // Get the to col number or skip the turn
                Integer toCol = InputUtils.handlePositionInput("Select the column where you would like to move the piece or type 'skip' to skip your turn: ");
                if (toCol == null) {
                    handleTurnSkip();
                    continue;
                }

                board.movePiece(fromRow, fromCol, toRow, toCol);
        
                // Decrement the current player's moves
                if (currentPlayer == goldenPlayer) {
                    goldenPlayerMoves--;
                } else {
                    silverPlayerMoves--;
                }

                // Check if the current player won the game
                if(board.isGameWon(currentPlayer)) {
                    System.out.println(currentPlayer + " won the game!");
                    isGameRunning = false;
                    return;
                };
 
                // Check if the current player lost the game
                if(board.isGameLost(currentPlayer)) {
                    System.out.println(currentPlayer + " lost the game!");
                    isGameRunning = false;
                    return;
                };

                // Check if the other player won the game
                if(board.isGameWon(currentPlayer == goldenPlayer ? goldenPlayer : silverPlayer)) {
                    System.out.println(currentPlayer == goldenPlayer ? goldenPlayer : silverPlayer + " won the game!");
                    isGameRunning = false;
                    return;
                };

                // Check if the other player won the game
                if(board.isGameLost(currentPlayer == goldenPlayer ? goldenPlayer : silverPlayer)) {
                    System.out.println(currentPlayer == goldenPlayer ? goldenPlayer : silverPlayer + " lost the game!");
                    isGameRunning = false;
                    return;
                };


                // If the current player has no moves left, switch to the other player and reset their moves
                if ((currentPlayer == goldenPlayer && goldenPlayerMoves == 0) || (currentPlayer == silverPlayer && silverPlayerMoves == 0)) {
                    currentPlayer = (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
                    if (currentPlayer == goldenPlayer) {
                        goldenPlayerMoves = 4;
                    } else {
                        silverPlayerMoves = 4;
                    }
                }
            } catch (Exception e) {
                System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
            }
        }
    }


    /**
     * Handles the skipping of a player's turn.
     * If the current player has made at least 1 move, the turn is skipped and the current player is switched.
     * Otherwise, an exception is thrown indicating that the player cannot skip their move without making any moves.
     */
    private void handleTurnSkip() {
        if (currentPlayer == goldenPlayer && goldenPlayerMoves < 4) {
            currentPlayer = silverPlayer;
            System.out.println("Turn skipped!");
        } else if (currentPlayer == silverPlayer && silverPlayerMoves < 4) {
            currentPlayer = goldenPlayer;
            System.out.println("Turn skipped!");
        } else {
            throw new IllegalArgumentException("You cannot skip your move when you have not moved with any of your pieces!");
        }
    }


    public void saveGame(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // TODO: Implement
        } catch (IOException e) {
            System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
        }
    }


    public void loadGame(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            // TODO: Implement
        } catch (IOException e) {
            System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
        }
    }

}