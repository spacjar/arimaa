package arimaa;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceState;
import arimaa.enums.PieceType;

import arimaa.utils.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Arimaa {

    Board board = new Board();
    boolean isGameRunning = false;
    
    public void startGame() {
        isGameRunning = true;
        
        // Game init logic
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN, PieceState.ALIVE), 0, i);
        }

        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER, PieceState.ALIVE), 7, i);
        }

        // Game logic
        while (isGameRunning) {
            board.printBoard();

            try {
                int fromRow = InputUtils.getIntFromInput("Select the row where is the piece you would like to move: ");
                int fromCol = InputUtils.getIntFromInput("Select the column where is the piece you would like to move: ");
                int toRow = InputUtils.getIntFromInput("Select the row where you would like to move the piece: ");
                int toCol = InputUtils.getIntFromInput("Select the column where you would like to move the piece: ");
                board.movePiece(fromRow, fromCol, toRow, toCol);  
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