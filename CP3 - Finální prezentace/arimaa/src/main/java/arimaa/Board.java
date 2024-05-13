package arimaa;

import arimaa.enums.PieceColor;

public class Board {
	private static final int ROW_SIZE = 8;
	private static final int COL_SIZE = 8;
  	private Piece[][] board;

	public Board() {
        board = new Piece[ROW_SIZE][COL_SIZE];
	}

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void placePiece(Piece piece, int row, int col) {
        board[row][col] = piece;
    }

    public void removePiece(int row, int col) {
        board[row][col] = null;
    }

    public boolean isOccupied(int row, int col) {
        if(board[row][col] != null) {
            return true;
        }
        return false;
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO: Validation logic
        return false;
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = getPieceAt(fromRow, fromCol);
        removePiece(fromRow, fromCol);
        placePiece(piece, toRow, toCol);
        return true;
    }

    public boolean isWin(PieceColor color) {
        // TODO: Validation logic
        return false;
    }

    public boolean isLoss(PieceColor color) {
        // TODO: Validation logic
        return false;
    }

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
  
