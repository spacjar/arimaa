package arimaa;

import java.util.ArrayList;
import java.util.List;

import arimaa.enums.PieceState;

public class Board {
    private Piece[][] board;
	private static final int ROW_SIZE = 8;
	private static final int COL_SIZE = 8;

	public Board() {
        board = new Piece[ROW_SIZE][COL_SIZE];
	}

    public Piece getPieceAt(int row, int col) {
        if(row <= 0 && col <= 0 && row > ROW_SIZE && col > COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        return board[row][col];
    }

    public void setPiece(Piece piece, int row, int col) {
        if(row <= 0 && col <= 0 && row > ROW_SIZE && col > COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        board[row][col] = piece;
    }

    public void removePiece(int row, int col) {
        if(row <= 0 && col <= 0 && row > ROW_SIZE && col > COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        board[row][col] = null;
    }

    // Validation
    public boolean isOccupied(int row, int col) {
        if(getPieceAt(row, col) != null) {
            return true;
        }
        return false;
    }

    // Get all adjacent pieces to the piece
    public List<Piece> getAdjacentPieces(int row, int col) {
        List<Piece> adjacentPieces = new ArrayList<>();
        
        // ! TD: Shitty code - Refactor logic!!
        if(row+1 >= 0 && col >= 0 && row+1 < ROW_SIZE && col < COL_SIZE) {
            adjacentPieces.add(getPieceAt(row+1, col));
        } else if(row-1 >= 0 && col >= 0 && row-1 < ROW_SIZE && col < COL_SIZE) {
            adjacentPieces.add(getPieceAt(row-1, col));
        } else if(row >= 0 && col+1 >= 0 && row < ROW_SIZE && col+1 < COL_SIZE) {
            adjacentPieces.add(getPieceAt(row, col+1));
        } else if(row >= 0 && col-1 >= 0 && row < ROW_SIZE && col-1 < COL_SIZE) {
            adjacentPieces.add(getPieceAt(row, col-1));
        }
    
        return adjacentPieces;
    }

    // Checks if the piece has friendly adjacent pieces
    public boolean hasAdjacentFriendlyPieces(int row, int col) {
        Piece currentPiece = getPieceAt(row, col);
        List<Piece> adjacentPieces = getAdjacentPieces(row, col);
    
        for (Piece piece : adjacentPieces) {
            if (piece != null && piece.getColor() == currentPiece.getColor()) {
                return true;
            }
        }
    
        return false;
    }

    // Checks if the piece is frozen
    public boolean isFrozen(int row, int col) {
        Piece currentPiece = getPieceAt(row, col);
        List<Piece> adjacentPieces = getAdjacentPieces(row, col);

        for (Piece piece : adjacentPieces) {
            // ! TD: Add type to number (weight)
            if (piece != null && currentPiece != null && piece.getColor() != currentPiece.getColor() && piece.getPieceWeight() > currentPiece.getPieceWeight()) {
                return true;
            }
        }
    
        return false;
    }

    // Checks if the piece is in the trap field
    public boolean isInTrap(int row, int col) {
        // ? note(not important): Think of a way to make it dynamic. For example if I set the board to 16x16, than these sqaures will not be sufficient. However, due to the rules, this cannot happen.
        if ((row == 2 && col == 2) || (row == 2 && col == 5) || (row == 5 && col == 2) || (row == 5 && col == 5)) { 
            return true;
        }
        return false;
    }

    // Conditional method for movement
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isOccupied(fromRow, fromCol)) {
            throw new IllegalArgumentException("There is no piece at the specified location.");
        }

        if (isFrozen(fromCol, fromRow)) {
            throw new IllegalArgumentException("This piece is frozen");
        }

        Piece piece = getPieceAt(fromRow, fromCol);
        removePiece(fromRow, fromCol);

        // The !isFrozen() does not need to be here, since I check it above (but i am going to think about it)
        if (!isOccupied(toRow, toCol) && !isFrozen(fromCol, fromRow)) {
            setPiece(piece, toRow, toCol);
        } else {
            throw new IllegalArgumentException("Invalid move");
        }

        if(isInTrap(toRow, toCol) && !hasAdjacentFriendlyPieces(toRow, toCol)) {
            piece.setState(PieceState.DEAD);
            removePiece(toRow, toCol);
        }

        if(isFrozen(toCol, toRow)) {
            piece.setState(PieceState.FROZEN);
        }
    }

    // TD: PUSH ENEMY PIECE

    // TD: PULL ENEMY PIECE

    // TD: CHECK IF THE GAME ENDED (WIN OR LOSE)


	public void printBoard() {
		System.out.println("-   0    1    2    3    4    5    6    7");

		for (int row = 0; row < ROW_SIZE; row++) {
			System.out.print((row) + " ");

			for (int col = 0; col < COL_SIZE; col++) {
				Piece piece = board[row][col];
				if (piece == null) {
					System.out.print(" ... ");
				} else {
					System.out.print(" " + piece.toString() + " ");
				}
			}
            
            System.out.println();
        }
    }
}
  
