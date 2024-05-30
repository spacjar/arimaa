package arimaa.models;

import arimaa.enums.PieceColor;

import java.util.Random;
import java.util.List;

public class ComputerPlayer extends Player {
    // private Arimaa arimaa;
    private Board board;
    private Random random = new Random();

    public ComputerPlayer(PieceColor pieceColor, Board board) {
        super(pieceColor);
        // this.arimaa = arimaa;
        this.board = board;
    }


    public void generateMove() {
        int fromRow, fromCol;
        List<int[]> adjacentFreeSpacePositions;

        boolean pieceSelectSeccessful = false; 

        while(pieceSelectSeccessful == false) {
            fromRow = random.nextInt(8);
            fromCol = random.nextInt(8);

            Piece currentPiece = board.getPieceAt(fromRow, fromCol);

            if(currentPiece != null && currentPiece.getColor() == this.getColor()) {
                adjacentFreeSpacePositions = board.getFreeAdjacentPositions(fromRow, fromCol);
            } else {
                continue;
            }

            if (adjacentFreeSpacePositions != null && !adjacentFreeSpacePositions.isEmpty()) {
                int randomIndex = new Random().nextInt(adjacentFreeSpacePositions.size());
                int[] randomPosition = adjacentFreeSpacePositions.get(randomIndex);
                try {
                    board.movePiece(fromRow, fromCol, randomPosition[0], randomPosition[1], false);
                    pieceSelectSeccessful = true;
                    System.out.println("-------- MOVED PIECE: " + fromRow + ", " + fromCol + ", " + randomPosition[0] + ", " + randomPosition[1]);
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }
}