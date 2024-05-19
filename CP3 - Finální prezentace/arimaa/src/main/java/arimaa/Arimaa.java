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

    // Game state
    boolean isSetupFinished = false;
    boolean isGameRunning = false;

    // Golden player
    Player goldenPlayer = new Player(PieceColor.GOLDEN);
    int goldenPlayerMoves = 4;

    // Silver player
    Player silverPlayer = new Player(PieceColor.SILVER);
    int silverPlayerMoves = 4;

    // Current player (first is always the golden player)
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
    private void setupGame() {
        System.out.println("\n***** - | Game setup | - *****\n");

        // Initialize the piece counts
        Map<PieceType, Integer> goldenPieces = new HashMap<>();
        Map<PieceType, Integer> silverPieces = new HashMap<>();

        // Loop over the piece types and set their counts into the hashmaps
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

        boolean allGoldenPiecesPlaced = false;
        boolean allSilverPiecesPlaced = false;
        
        // Loop until the players setup their pieces
        while (!allGoldenPiecesPlaced || !allSilverPiecesPlaced) {
            try {
                // Set the piece types hashmap based on the players color
                Map<PieceType, Integer> currentPieces = currentPlayer.getColor() == PieceColor.GOLDEN ? goldenPieces : silverPieces;
                
                // Check if there are still available pieces to be placed
                boolean areCurrentPiecesAvailable = currentPieces.values().stream().anyMatch(count -> count > 0);

                // Check if the golden player placed all of his pieces and then switch to the silver player (+ skip the current iteration)
                if (!areCurrentPiecesAvailable) {
                    currentPlayer = currentPlayer.getColor() == PieceColor.GOLDEN ? silverPlayer : goldenPlayer;
                    continue;
                }

                // Print the current player
                System.out.println("\nCurrent player: " + currentPlayer);
                
                // Print the board
                board.printBoard();
                
                // Print out all of the available piece types
                System.out.println("Currently available pieces: ");
                // Loop over the hashmap and print out the keys (piece types) and values (counts)
                for (Map.Entry<PieceType, Integer> entry : currentPieces.entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue());
                }
                
                // Ask the player to choose a piece type
                System.out.println("[1]: RABBIT, [2]: CAT, [3]: DOG, [4]: HORSE, [5]: CAMEL, [6]: ELEPHANT");
                int chosenPieceTypeNum = InputUtils.getIntFromInput("Please choose a piece type number: ");
                PieceType chosenPieceType;

                // Get the piece type based on the provided number
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
                        throw new IllegalArgumentException("The piece type with the number " + chosenPieceTypeNum + " does not exist!" );
                }
                
                // Ask the player to choose a row and column
                int chosenRow = InputUtils.getIntFromInput("Select the row where you would like to place the piece: ");
                int chosenCol = InputUtils.getIntFromInput("Select the col where you would like to place the piece: ");

                // Try to setup the chosen piece at the chosen location
                setupPlayerPieces(currentPlayer, currentPieces, chosenPieceType, chosenRow, chosenCol);

                // Print the board
                board.printBoard();

                // Check if all of the pieces for the golden player have been setup
                allGoldenPiecesPlaced = !goldenPieces.values().stream().anyMatch(count -> count > 0);
                // Check if all of the pieces for the silver player have been setup
                allSilverPiecesPlaced = !silverPieces.values().stream().anyMatch(count -> count > 0);
            } catch (Exception e) {
                System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
            }
        }

        // Set the game control value of the setup as finished
        isSetupFinished = true;
    }


    public void startGame() {
        // Set the game control value of the phase as running
        isGameRunning = true;

        // Reset the current player to be golden player, since he always starts the game
        currentPlayer = goldenPlayer;

        // ----- DEBUG game init start -----
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN), 0, i);
        }
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.GOLDEN), 1, i);
        }

        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.SILVER), 7, i);
        }
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER), 6, i);
        }
        isSetupFinished = true;
        // ----- DEBUG game init end -----


        // ---- Game init logic ----
        if(isSetupFinished == false) {
            setupGame();
        }

        System.out.println("\n***** - | Game | - *****\n");

        // ---- Game logic ----
        while (isGameRunning) {
            board.printBoard();

            try {
                System.out.println("Current player: " + currentPlayer);
                System.out.println("Golden player moves left: " + goldenPlayerMoves);
                System.out.println("Silver player moves left: " + silverPlayerMoves);

                // ! TD: Clean up the shitty code
                Integer fromRow = InputUtils.getPositionsFromInput("Select the row where is the piece you would like to move or type 'skip' to skip your turn: ");
                if (fromRow == null) {
                    if (currentPlayer == goldenPlayer && goldenPlayerMoves < 4) {
                        currentPlayer = silverPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }
                    if (currentPlayer == silverPlayer && silverPlayerMoves < 4) {
                        currentPlayer = goldenPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }

                    throw new IllegalArgumentException("You cannot skip your move when you have not moved with any of your pieces!");
                }
                
                Integer fromCol = InputUtils.getPositionsFromInput("Select the column where is the piece you would like to move or type 'skip' to skip your turn: ");
                if (fromCol == null) {
                    if (currentPlayer == goldenPlayer && goldenPlayerMoves < 4) {
                        currentPlayer = silverPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }
                    if (currentPlayer == silverPlayer && silverPlayerMoves < 4) {
                        currentPlayer = goldenPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }

                    throw new IllegalArgumentException("You cannot skip your move when you have not moved with any of your pieces!");
                }
                
                if(!board.isOccupied(fromRow, fromCol)) {
                    throw new IllegalArgumentException("There is no piece at the specified location!");
                }

                Piece piece = board.getPieceAt(fromRow, fromCol);

                if (piece.getColor() != currentPlayer.getColor()) {
                    throw new IllegalArgumentException("You can only move with your own pieces!");
                }
        
                Integer toRow = InputUtils.getPositionsFromInput("Select the row where you would like to move the piece or type 'skip' to skip your turn: ");
                if (toRow == null) {
                    if (currentPlayer == goldenPlayer && goldenPlayerMoves < 4) {
                        currentPlayer = silverPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }
                    if (currentPlayer == silverPlayer && silverPlayerMoves < 4) {
                        currentPlayer = goldenPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }

                    throw new IllegalArgumentException("You cannot skip your move when you have not moved with any of your pieces!");
                }

                Integer toCol = InputUtils.getPositionsFromInput("Select the column where you would like to move the piece or type 'skip' to skip your turn: ");
                if (toCol == null) {
                    if (currentPlayer == goldenPlayer && goldenPlayerMoves < 4) {
                        currentPlayer = silverPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }
                    if (currentPlayer == silverPlayer && silverPlayerMoves < 4) {
                        currentPlayer = goldenPlayer;
                        System.out.println("Turn skipped!");
                        continue;
                    }

                    throw new IllegalArgumentException("You cannot skip your move when you have not moved with any of your pieces!");
                }

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
                System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
            }
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