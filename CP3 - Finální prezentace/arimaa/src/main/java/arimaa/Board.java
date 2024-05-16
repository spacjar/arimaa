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


    /**
     * Retrieves the piece at the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return the piece at the specified position
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public Piece getPieceAt(int row, int col) throws IndexOutOfBoundsException {
        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        return board[row][col];
    }


    /**
     * Sets a piece at the specified position on the board.
     *
     * @param piece the piece to be set
     * @param row the row index of the position
     * @param col the column index of the position
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public void setPiece(Piece piece, int row, int col) throws IndexOutOfBoundsException {
        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        board[row][col] = piece;
    }


    /**
     * Removes a piece from the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public void removePiece(int row, int col) throws IndexOutOfBoundsException {
        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        board[row][col] = null;
    }


    /**
     * Checks if a specific position on the board is occupied by a piece.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return true if the position is occupied, false if not
     */
    public boolean isOccupied(int row, int col) {
        if(getPieceAt(row, col) == null) {
            return false;
        }
        return true;
    }


    /**
     * Checks if a piece at the specified position is in a trap field.
     *
     * @param row the row index of the position to check
     * @param col the column index of the position to check
     * @return true if the piece is in a trap field, false if not
     */
    public boolean isInTrap(int row, int col) {
        // ? note(not important): Think of a way to make it dynamic. For example if I set the board to 16x16, than these sqaures will not be sufficient. However, due to the rules, this cannot happen.
        if ((row == 2 && col == 2) || (row == 2 && col == 5) || (row == 5 && col == 2) || (row == 5 && col == 5)) { 
            return true;
        }
        return false;
    }


    public int checkPathCost(int fromRow, int fromCol, int toRow, int toCol) {
        // Logic that calculates how much fields are in the current step of the player
        // In my code, it should return 1, in the future, the steps can be more complicated than 1, but for now, it should be 1
        return 0;
    }


    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Check if the move is front, back, left, right (not to 45deg)
        
        // Check if the piece is rabbit (he cannot move backwards)

        return true;
    }


    
    /**
     * Returns a list of adjacent pieces to the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return a list of adjacent pieces
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public List<Piece> getAdjacentPieces(int row, int col) throws IndexOutOfBoundsException {
        List<Piece> adjacentPieces = new ArrayList<Piece>();

        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }

        // Check above
        if(row+1 < ROW_SIZE && isOccupied(row+1, col)) {
            adjacentPieces.add(getPieceAt(row+1, col));
        }
        // Check below
        if(row-1 >= 0 && isOccupied(row-1, col)) {
            adjacentPieces.add(getPieceAt(row-1, col));
        }
        // Check right
        if(col+1 < COL_SIZE && isOccupied(row, col+1)) {
            adjacentPieces.add(getPieceAt(row, col+1));
        }
        // Check left
        if(col-1 >= 0 && isOccupied(row, col-1)) {
            adjacentPieces.add(getPieceAt(row, col-1));
        }
    
        return adjacentPieces;
    }


    /**
     * Checks if there are adjacent friendly pieces to the piece at the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return true if there are adjacent friendly pieces, false otherwise
     */
    public boolean hasAdjacentFriendlyPieces(int row, int col) {
        Piece currentPiece = getPieceAt(row, col);
        List<Piece> adjacentPieces = getAdjacentPieces(row, col);
    
        for (Piece piece : adjacentPieces) {
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPieces() method.
            if (piece != null && currentPiece != null && piece.getColor() == currentPiece.getColor()) {
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
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPieces() method.
            if (piece != null && currentPiece != null && currentPiece.getColor() != piece.getColor() && currentPiece.getPieceWeight() < piece.getPieceWeight()) {
                return true;
            }
        }
    
        return false;
    }


    // Conditional method for movement
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isOccupied(fromRow, fromCol)) {
            throw new IllegalArgumentException("There is no piece at the specified location.");
        }

        if (isFrozen(fromCol, fromRow)) {
            throw new IllegalArgumentException("This piece is frozen.");
        }

        Piece piece = getPieceAt(fromRow, fromCol);
        removePiece(fromRow, fromCol);

        // The !isFrozen() does not need to be here, since I check it above (but i am going to think about it)
        if (!isOccupied(toRow, toCol) && !isFrozen(fromCol, fromRow)) {
            setPiece(piece, toRow, toCol);
        } else {
            throw new IllegalArgumentException("Invalid move.");
        }

        if(isInTrap(toRow, toCol) && !hasAdjacentFriendlyPieces(toRow, toCol)) {
            piece.setState(PieceState.DEAD);
            removePiece(toRow, toCol);
        }

        if(isFrozen(toCol, toRow)) {
            piece.setState(PieceState.FROZEN);
        }

        // ! TD: Check adjacent pieces (fromRow, fromCol), to see if they are still frozen etc.
        // ! TD: Check adjacent pieces (toRow, toCol), to see if they become frozen etc.
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
  
