package arimaa.models;

import arimaa.enums.PieceColor;

import java.util.Random;
import java.util.List;

public class ComputerPlayer extends Player {
    private Arimaa arimaa;
    private Board board;
    private Random random = new Random();

    public ComputerPlayer(PieceColor pieceColor, Arimaa arimaa, Board board) {
        super(pieceColor);
        this.arimaa = arimaa;
        this.board = board;
    }

    public void generateMove() {
        int fromRow, fromCol;
        List<int[]> adjacentFreeSpacePositions;

        boolean pieceSelectSeccessful = false; 

        // Get a random piece using board.getPieceAt(row, col)
        // Check if piece exists, if not get another random piece
        while(pieceSelectSeccessful == false) {
            fromRow = random.nextInt(8);
            fromCol = random.nextInt(8);

            Piece currentPiece = board.getPieceAt(fromRow, fromCol);

            if(currentPiece != null && currentPiece.getColor() == arimaa.getSilverPlayer().getColor()) {
                adjacentFreeSpacePositions = board.getFreeAdjacentPositions(fromRow, fromCol);
            } else {
                continue;
            }

            if (adjacentFreeSpacePositions != null) {
                int randomIndex = new Random().nextInt(adjacentFreeSpacePositions.size());
                int[] randomPosition = adjacentFreeSpacePositions.get(randomIndex);
                try {
                    board.movePiece(fromRow, fromCol, randomPosition[0], randomPosition[1], false);
                    pieceSelectSeccessful = true;
                } catch (Exception e) {
                    // Log
                    continue;
                }
            }
        }
    }

    public void generateMoves() {
        int movesCount = random.nextInt(4) + 1;

        for (int i = 0; i < movesCount; i++) {
            generateMove();
            try {
                // Sleep for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}