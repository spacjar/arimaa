package arimaa;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;

import arimaa.utils.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Arimaa {

    Board board = new Board();
    boolean isSetupFinished = false;
    boolean isGameRunning = false;

    // Golden player
    Player goldenPlayer = new Player(PieceColor.GOLDEN);
    int goldenPlayerMoves = 4;

    // Silver player
    Player silverPlayer = new Player(PieceColor.SILVER);
    int silverPlayerMoves = 4;

    // Current player
    Player currentPlayer = goldenPlayer;



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
            throw new IllegalArgumentException("No more pieces of the selected type are available!");
        }
    
        Piece piece = new Piece(chosenPieceType, player.getColor());
        board.setPiece(piece, row, col);
        piecesCount.put(chosenPieceType, piecesCount.get(chosenPieceType) - 1);
    }

    

    private void setupGame() {
        // Initialize the piece counts
        Map<PieceType, Integer> goldenPieces = new HashMap<>();
        Map<PieceType, Integer> silverPieces = new HashMap<>();

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

        // Ask the player to setup their pieces
        // ! TD: Finish logic
        boolean allPiecesAvailable = true;

        // ! TD: Loop
        if (allPiecesAvailable) {
            // Ask the player to choose a piece
            
            System.out.println("Piece types numbers - (1: RABBIT | 2: CAT | 3: DOG | 4: HORSE | 5: CAMEL | 6 ELEPHANT)");
            int chosenPieceTypeNum = InputUtils.getIntFromInput("Please choose a piece type number: ");
            PieceType chosenPieceType;

            switch(chosenPieceTypeNum) {
                case 1:
                    chosenPieceType = PieceType.RABBIT;
                    break;
                case 2:
                    chosenPieceType = PieceType.CAT;
                    break;
                case 3:
                    chosenPieceType = PieceType.DOG;
                    break;
                case 4:
                    chosenPieceType = PieceType.HORSE;
                    break;
                case 5:
                    chosenPieceType = PieceType.CAMEL;
                    break;
                case 6:
                    chosenPieceType = PieceType.ELEPHANT;
                    break;
                default:
                    throw new IllegalArgumentException("This piece with the following number does not exist: " + chosenPieceTypeNum);
            }

            
            // Ask the player to choose a row and column
            int chosenRow = InputUtils.getIntFromInput("Select the row where you would like to place the piece: ");
            int chosenCol = InputUtils.getIntFromInput("Select the col where you would like to place the piece: ");

            // Try to setup the chosen piece at the chosen location
            setupPlayerPieces(currentPlayer, goldenPieces, chosenPieceType, chosenRow, chosenCol);
        } else {
            System.out.println("No more pieces of the selected type are available!");
        }
    }


    public void startGame() {
        isGameRunning = true;

        // ---- Game init logic ----
        if(isSetupFinished == false) {
            setupGame();
        }
        
        // for(int i = 0; i <= 7; i++) {
        //     board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN), 0, i);
        // }
        // for(int i = 0; i <= 7; i++) {
        //     board.setPiece(new Piece(PieceType.RABBIT, PieceColor.GOLDEN), 1, i);
        // }

        // for(int i = 0; i <= 7; i++) {
        //     board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.SILVER), 7, i);
        // }
        // for(int i = 0; i <= 7; i++) {
        //     board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER), 6, i);
        // }

        // ---- Game logic ----
        while (isGameRunning) {
            board.printBoard();

            try {
                System.out.println("Current player: " + currentPlayer);
                System.out.println("Golden player moves: " + goldenPlayerMoves);
                System.out.println("Silver player moves: " + silverPlayerMoves);

                int fromRow = InputUtils.getIntFromInput("Select the row where is the piece you would like to move: ");
                int fromCol = InputUtils.getIntFromInput("Select the column where is the piece you would like to move: ");
                
                if(!board.isOccupied(fromRow, fromCol)) {
                    System.err.println("There is no piece at the specified location!");
                    continue;
                }

                Piece piece = board.getPieceAt(fromRow, fromCol);

                if (piece.getColor() != currentPlayer.getColor()) {
                    System.err.println("You can only move your own pieces.");
                    continue;
                }
        
                int toRow = InputUtils.getIntFromInput("Select the row where you would like to move the piece: ");
                int toCol = InputUtils.getIntFromInput("Select the column where you would like to move the piece: ");

                board.movePiece(fromRow, fromCol, toRow, toCol);
        
                // Decrement the current player's moves
                if (currentPlayer == goldenPlayer) {
                    goldenPlayerMoves--;
                } else {
                    silverPlayerMoves--;
                }

                // !!! TD: Fix game won/lost check - when I play as golden player, I don't check for silver player
                // Check if the game is won
                if(board.isGameWon(currentPlayer)) {
                    System.out.println(currentPlayer + " won the game!");
                    isGameRunning = false;
                    return;
                };
 
                // Check if the game is won
                if(board.isGameLost(currentPlayer)) {
                    System.out.println(currentPlayer + " lost the game!");
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

                // currentPlayer
            } catch (Exception e) {
                System.err.println("--- ERROR: " + e.getMessage());
            }
        }
    }

    public void saveGame(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // TODO: Implement
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            // TODO: Implement
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
    }

}