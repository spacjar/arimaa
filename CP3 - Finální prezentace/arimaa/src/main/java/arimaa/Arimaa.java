package arimaa;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;

import arimaa.utils.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Arimaa {

    Board board = new Board();
    boolean isGameRunning = false;

    // Golden player
    Player goldenPlayer = new Player(PieceColor.GOLDEN);
    int goldenPlayerMoves = 4;

    // Silver player
    Player silverPlayer = new Player(PieceColor.SILVER);
    int silverPlayerMoves = 4;

    // Current player
    Player currentPlayer = goldenPlayer;


    public void startGame() {
        isGameRunning = true;
        
        // ---- Game init logic ----
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN), 0, i);
        }

        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER), 7, i);
        }

        // ---- Game logic ----
        while (isGameRunning) {
            board.printBoard();

            try {
                System.out.println("Current player: " + currentPlayer);
                System.out.println("Golden player moves: " + goldenPlayerMoves);
                System.out.println("Silver player moves: " + silverPlayerMoves);

                int fromRow = InputUtils.getIntFromInput("Select the row where is the piece you would like to move: ");
                int fromCol = InputUtils.getIntFromInput("Select the column where is the piece you would like to move: ");
                
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