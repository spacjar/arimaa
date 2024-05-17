package arimaa;

import java.util.ArrayList;
import java.util.List;

import arimaa.enums.PieceColor;
import arimaa.enums.PieceState;
import arimaa.enums.PieceType;

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


    /**
     * Checks if a move is a one-step move from a given position to another position on the board.
     *
     * @param fromRow the row index of the starting position
     * @param fromCol the column index of the starting position
     * @param toRow the row index of the destination position
     * @param toCol the column index of the destination position
     * @return true if the move is a one-step move, false otherwise
     */
    public boolean isOneStep(int fromRow, int fromCol, int toRow, int toCol) {
        // ? TD: In the furute, the code could calculate the distance the player will take - public int getPathCost(int fromRow, int fromCol, int toRow, int toCol)

        if (fromRow == toRow && Math.abs(fromCol - toCol) == 1) {
            return true;
        }
        if (fromCol == toCol && Math.abs(fromRow - toRow) == 1) {
            return true;
        }   
            
        return false;
    }


    /**
     * Checks if a move from one position to another on the board is valid.
     *
     * @param fromRow the row index of the starting position
     * @param fromCol the column index of the starting position
     * @param toRow the row index of the destination position
     * @param toCol the column index of the destination position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Check if the move is one-step (front, back, left, right - not to diagonal (45deg))
        if(!isOneStep(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // Check if the piece is rabbit (he cannot move backwards)
        Piece piece = getPieceAt(fromRow, fromCol);
        // ! TD: Fix when a player can choose sides, now it is that the GOLDEN player always has to start at the [0] index of the array, and the SILVER player at the [7] index of the array
        if (piece != null && piece.getType() == PieceType.RABBIT) {
            if (piece.getColor() == PieceColor.GOLDEN && fromRow > toRow) {
                return false;
            }
            if (piece.getColor() == PieceColor.SILVER && fromRow < toRow) {
                return false;
            }
        }

        return true;
    }

    
    /**
     * Returns a list of positions of the adjacent pieces to the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return a list of positions of adjacent pieces
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public List<int[]> getAdjacentPiecePositions(int row, int col) throws IndexOutOfBoundsException {
        List<int[]> adjacentPieces = new ArrayList<int[]>();

        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }

        // Check above
        if(row+1 < ROW_SIZE && isOccupied(row+1, col)) {
            adjacentPieces.add(new int[]{row+1, col});
        }
        // Check below
        if(row-1 >= 0 && isOccupied(row-1, col)) {
            adjacentPieces.add(new int[]{row-1, col});
        }
        // Check right
        if(col+1 < COL_SIZE && isOccupied(row, col+1)) {
            adjacentPieces.add(new int[]{row, col+1});
        }
        // Check left
        if(col-1 >= 0 && isOccupied(row, col-1)) {
            adjacentPieces.add(new int[]{row, col-1});
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
        List<int[]> adjacentPiecePositions = getAdjacentPiecePositions(row, col);
    
        for (int[] position : adjacentPiecePositions) {
            Piece piece = getPieceAt(position[0], position[1]);
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPiecePositions() method.
            if (piece != null && currentPiece != null && piece.getColor() == currentPiece.getColor()) {
                return true;
            }
        }
    
        return false;
    }


    /**
     * Checks if a piece at the specified position is frozen.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return true if the piece is frozen, false otherwise
     */
    public boolean isFrozen(int row, int col) {
        Piece currentPiece = getPieceAt(row, col);
        List<int[]> adjacentPiecePositions = getAdjacentPiecePositions(row, col);

        for (int[] position : adjacentPiecePositions) {
            Piece piece = getPieceAt(position[0], position[1]);
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPiecePositions() method.
            if (piece != null && currentPiece != null && currentPiece.getColor() != piece.getColor() && currentPiece.getPieceWeight() < piece.getPieceWeight() && !hasAdjacentFriendlyPieces(row, col)) {
                return true;
            }
        }
    
        return false;
    }


    // Conditional method for movement
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
        // Check before moving the piece
        if (!isOccupied(fromRow, fromCol)) {
            throw new IllegalArgumentException("There is no piece at the specified location.");
        }

        if (isFrozen(fromRow, fromCol)) {
            throw new IllegalArgumentException("This piece is frozen.");
        }

        if(!isValidMove(fromRow, fromCol, toRow, toCol)) {
            throw new IllegalArgumentException("This movement is invalid!");
        }

        // Move the piece
        Piece piece = getPieceAt(fromRow, fromCol);
        
        if (!isOccupied(toRow, toCol)) {
            removePiece(fromRow, fromCol);
            setPiece(piece, toRow, toCol);
        } else {
            throw new IllegalArgumentException("Invalid move.");
        }

        // Check after moving the piece
        if(isInTrap(toRow, toCol) && !hasAdjacentFriendlyPieces(toRow, toCol)) {
            piece.setState(PieceState.DEAD);
            removePiece(toRow, toCol);
        }

        if(isFrozen(toRow, toCol)) {
            piece.setState(PieceState.FROZEN);
        } else if (piece.getState() == PieceState.FROZEN) {
            piece.setState(PieceState.ALIVE);
        }

        // ! TD: Check adjacent pieces (fromRow, fromCol), to see if they are still frozen etc.
        List<int[]> adjacentPeicePositionsFrom = getAdjacentPiecePositions(fromRow, fromCol);

        for (int[] position : adjacentPeicePositionsFrom) {
            int positionFromRow = position[0];
            int positionFromCol = position[1];

            Piece pieceFrom = getPieceAt(positionFromRow, positionFromCol);
            if(isInTrap(positionFromRow, positionFromCol) && !hasAdjacentFriendlyPieces(positionFromRow, positionFromCol)) {
                pieceFrom.setState(PieceState.DEAD);
                removePiece(positionFromRow, positionFromCol);
            }
            
            if(isFrozen(positionFromRow, positionFromCol)) {
                pieceFrom.setState(PieceState.FROZEN);
            } else if (pieceFrom.getState() == PieceState.FROZEN) {
                pieceFrom.setState(PieceState.ALIVE);
            }
        }

        // ! TD: Check adjacent pieces (toRow, toCol), to see if they become frozen etc.
        List<int[]> adjacentPeicePositionsTo = getAdjacentPiecePositions(toRow, toCol);

        for (int[] position : adjacentPeicePositionsTo) {
            int positionToRow = position[0];
            int positionToCol = position[1];

            Piece pieceTo = getPieceAt(positionToRow, positionToCol);
            if(isInTrap(positionToRow, positionToCol) && !hasAdjacentFriendlyPieces(positionToRow, positionToCol)) {
                pieceTo.setState(PieceState.DEAD);
                removePiece(positionToRow, positionToCol);
            }
            
            if(isFrozen(positionToRow, positionToCol)) {
                pieceTo.setState(PieceState.FROZEN);
            } else if (pieceTo.getState() == PieceState.FROZEN) {
                pieceTo.setState(PieceState.ALIVE);
            }
            
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
                    if(isInTrap(row, col)) {
                        System.out.print(" xxx ");
                    } else {
                        System.out.print(" ... ");
                    }
				} else {
					System.out.print(" " + piece.toString() + (piece.getState() == PieceState.FROZEN ? "FRZ" : "") + " ");
				}
			}
            
            System.out.println();
        }
    }
}
  
