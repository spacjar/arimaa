package arimaa.models;

import arimaa.enums.PieceColor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ArimaaGameRecorder {
    private ObservableList<String> moves;
    private int moveNumber;
    private boolean isTurnEnded;
    private boolean isGoldenTurn;


    public ArimaaGameRecorder() {
        this.moves = FXCollections.observableArrayList();
        this.moveNumber = 1;
        this.isTurnEnded = true;
        this.isGoldenTurn = true; 
    }

    public ObservableList<String> getMoves() {
        System.out.println(moves);
        return moves;
    }

    public void clearMoves() {
        this.moveNumber = 1;
        this.moves.clear();
    }

    public void recordPiecePlacement(Piece piece, int row, int col) {
        String move = piece.getPiecePrefix() + getColumnLetter(col) + (row+1);
        appendMove(move);
        System.out.println(moves);
    }

    public void recordMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        String move = piece.getPiecePrefix() + getColumnLetter(fromCol) + (fromRow+1) + getDirection(fromRow, fromCol, toRow, toCol);
        appendMove(move);
        System.out.println(moves);
    }

    public void recordRemoval(Piece piece, int row, int col) {
        String move = piece.getPiecePrefix() + getColumnLetter(col) + (row+1) + "x";
        appendMove(move);
        System.out.println(moves);
    }

    public void recordResignation() {
        appendMove("resigns");
    }

    public void recordLoss() {
        appendMove("lost");
    }

    public void recordTakeback() {
        appendMove("takeback");
    }

    
    public void endTurn() {
        // Set isTurnEnded to true
        isTurnEnded = true;

        // Toggle isGoldenTurn
        isGoldenTurn = !isGoldenTurn;

        // Increment the move number if it's golden's turn
        if (isGoldenTurn) {
            moveNumber++;
        }
    }

    private void appendMove(String move) {
        // Determine the player's color based on isGoldenTurn
        String color = isGoldenTurn ? "g" : "s";
    
        // Prepend the move number and the player's color to the move
        String movePrefix = moveNumber + color + " ";
    
        if (moves.isEmpty() || isTurnEnded) {
            moves.add(movePrefix + move);
            isTurnEnded = false;
        } else {
            String lastMove = moves.get(moves.size() - 1);
            if (!lastMove.startsWith(movePrefix)) {
                lastMove += "\n" + movePrefix;
            }
            lastMove += " " + move;
            moves.set(moves.size() - 1, lastMove);
        }
    }

    public String getGameRecord() {
        StringBuilder gameRecord = new StringBuilder();
        for (String move : moves) {
            gameRecord.append(move).append("\n");
        }
        return gameRecord.toString();
    }


    /**
     * Returns the piece prefix based on the given piece and player.
     *
     * @param piece  the piece for which to get the prefix
     * @param player the player whose color determines the prefix case
     * @return the piece prefix
     */
    private String getPiecePrefix(Piece piece, Player player) throws IllegalArgumentException {
        String piecePrefix = piece.getPiecePrefix();

        if(player.getColor().equals(PieceColor.GOLDEN)) {
            piecePrefix = piecePrefix.toUpperCase();
        }

        if(player.getColor().equals(PieceColor.SILVER)) {
            piecePrefix = piecePrefix.toLowerCase();  
        }

        return piecePrefix;
    }


    /**
     * Returns the column letter corresponding to the given column index.
     *
     * @param col the column index (0-7)
     * @return the column letter (A-H)
     * @throws IllegalArgumentException if the column index is invalid
     */
    private String getColumnLetter(Integer col) throws IllegalArgumentException {
        switch(col) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3: 
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
            default:
                throw new IllegalArgumentException("This column does not exist: " + col);
        }
    }


    /**
     * Returns the direction from the initial position to the final position.
     *
     * @param initialRow the initial row position
     * @param initialCol the initial column position
     * @param finalRow the final row position
     * @param finalCol the final column position
     * @return the direction as a string ("n" for north, "s" for south, "e" for east, "w" for west)
     * @throws IllegalArgumentException if the initial and final positions are the same
     */
    public String getDirection(int initialRow, int initialCol, int finalRow, int finalCol) {
        if (finalRow > initialRow) {
            return "n"; // north
        } else if (finalRow < initialRow) {
            return "s"; // south
        } else if (finalCol > initialCol) {
            return "e"; // east
        } else if (finalCol < initialCol) {
            return "w"; // west
        } else {
            throw new IllegalArgumentException("Invalid move. Initial and final positions are the same.");
        }
    }
}