package arimaa;

public class Board {
    private Piece[][] board;
	private static final int ROW_SIZE = 8;
	private static final int COL_SIZE = 8;

	public Board() {
        board = new Piece[ROW_SIZE][COL_SIZE];
	}

    // Basic methods for movement
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

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TD: More logic
        return isOccupied(toRow, toCol);
    }


    // IS IN TRAP
    // IS FROZEN
    // DOES IT HAVE ADJACENT PIECES


    // Conditional method for movement
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isOccupied(fromRow, fromCol)) {
            throw new IllegalArgumentException("There is no piece at the specified location.");
        }

        Piece piece = getPieceAt(fromRow, fromCol);
        removePiece(fromRow, fromCol);

        if (isValidMove(fromRow, fromCol, toRow, toCol)) {
            setPiece(piece, toRow, toCol);
        } else {
            throw new IllegalArgumentException("Invalid move");
        }
    }


    // CHECK IF THE GAME ENDED (WIN OR LOSE)


	public void printBoard() {
		System.out.println("-   A    B    C    D    E    F    G    H");

		for (int row = 0; row < ROW_SIZE; row++) {
			System.out.print((row + 1) + " ");

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
  
