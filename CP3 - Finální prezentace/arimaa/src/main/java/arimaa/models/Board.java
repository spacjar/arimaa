package arimaa.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;

public class Board {
    private Piece[][] board;
    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 8;

    // Logger
    private static final Logger logger = Logger.getLogger(Board.class.getName());

    // Create the board
    public Board() {
        board = new Piece[ROW_SIZE][COL_SIZE];
    }


    // ----- Getters and setters -----
    public int getRowSize() {
        return ROW_SIZE;
    }

    public int getColSize() {
        return COL_SIZE;
    }


    // ----- Core logic -----
    /**
     * Retrieves the piece at the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return the piece at the specified position
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public Piece getPieceAt(int row, int col) throws IndexOutOfBoundsException {
        // logger.info("Getting piece at row " + row + ", column " + col);
        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            logger.warning("The selected row or column exceeds board size.");
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
        // logger.info("Setting piece at row " + row + ", column " + col);
        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            logger.warning("The selected row or column exceeds board size.");
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
        // logger.info("Removing piece at row " + row + ", column " + col);
        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            logger.warning("The selected row or column exceeds board size.");
            throw new IndexOutOfBoundsException("The selected row or column exceeds board size.");
        }
        board[row][col] = null;
    }


    
    // -------------------- Board conditions and advanced logic --------------------
    /**
     * Checks if a specific position on the board is occupied by a piece.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return true if the position is occupied, false if not
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean isOccupied(int row, int col) throws IndexOutOfBoundsException {
        // logger.info("Checking if position at row " + row + ", column " + col + " is occupied.");
        if(getPieceAt(row, col) == null) {
            // logger.info("Position is not occupied.");
            return false;
        }
        // logger.info("Position is occupied.");
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
        // logger.info("Checking if position at row " + row + ", column " + col + " is a trap.");
        // ? TD: Think of a way to make it dynamic. For example if I set the board to 16x16, than these sqaures will not be sufficient. However, due to the rules, this cannot happen.
        if ((row == 2 && col == 2) || (row == 2 && col == 5) || (row == 5 && col == 2) || (row == 5 && col == 5)) { 
            // logger.info("Position is a trap.");
            return true;
        }
        // logger.info("Position is not a trap.");
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

        // logger.info("Checking if move from row " + fromRow + ", column " + fromCol + " to row " + toRow + ", column " + toCol + " is one step.");
        if (fromRow == toRow && Math.abs(fromCol - toCol) == 1) {
            // logger.info("Move is one step.");
            return true;
        }
        if (fromCol == toCol && Math.abs(fromRow - toRow) == 1) {
            // logger.info("Move is one step.");
            return true;
        }   
    
        logger.info("Move is not one step.");
        return false;
    }


    /**
     * Checks if a move for a rabbit is valid.
     *
     * @param fromRow the row index of the starting position
     * @param fromCol the column index of the starting position
     * @param toRow the row index of the destination position
     * @param toCol the column index of the destination position
     * @return true if the rabbit move is valid, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    // ! TD: Cleanup this method logic, the goal is to have the method return false by default, and true if it is really true!
    public boolean isRabbitMoveValid(int fromRow, int fromCol, int toRow, int toCol) throws IndexOutOfBoundsException {
        // logger.info("Checking if rabbit move from row " + fromRow + ", column " + fromCol + " to row " + toRow + ", column " + toCol + " is valid.");

        Piece piece = getPieceAt(fromRow, fromCol);
        // ? TD: Fix when a player can choose sides, now it is that the GOLDEN player always has to start at the [0] index of the array, and the SILVER player at the [7] index of the array
        if (piece != null && piece.getType() == PieceType.RABBIT) {
            if (piece.getColor() == PieceColor.GOLDEN && fromRow > toRow) {
                logger.info("Invalid move for silver rabbit.");
                return false;
            }
            if (piece.getColor() == PieceColor.SILVER && fromRow < toRow) {
                logger.info("Invalid move for silver rabbit.");
                return false;
            }
        }

        // logger.info("Move is one step.");
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
        // logger.info("Getting adjacent piece positions for row " + row + ", column " + col + ".");
        List<int[]> adjacentPieces = new ArrayList<int[]>();

        if(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE) {
            logger.warning("The selected row or column exceeds board size.");
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
    
        // logger.info("Found " + adjacentPieces.size() + " adjacent pieces.");
        return adjacentPieces;
    }


    /**
     * Checks if there are adjacent friendly pieces to the piece at the specified position on the board.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return true if there are adjacent friendly pieces, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean hasAdjacentFriendlyPieces(int row, int col) throws IndexOutOfBoundsException {
        // logger.info("Checking if there are adjacent friendly pieces at row " + row + ", column " + col + ".");
        Piece currentPiece = getPieceAt(row, col);
        List<int[]> adjacentPiecePositions = getAdjacentPiecePositions(row, col);
    
        for (int[] position : adjacentPiecePositions) {
            Piece piece = getPieceAt(position[0], position[1]);
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPiecePositions() method.
            if (piece != null && currentPiece != null && piece.getColor() == currentPiece.getColor()) {
                // logger.info("Found adjacent friendly piece.");
                return true;
            }
        }

        logger.info("No adjacent friendly pieces found.");
        return false;
    }


    public boolean hasAdjacentEnemyPiecesWithHigherValue(int row, int col) {
        Piece currentPiece = getPieceAt(row, col);
        List<int[]> adjacentPiecePositions = getAdjacentPiecePositions(row, col);

        for (int[] position : adjacentPiecePositions) {
            Piece enemyPiece = getPieceAt(position[0], position[1]);
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPiecePositions() method.
            if (currentPiece != null && enemyPiece != null && currentPiece.getColor() != enemyPiece.getColor() && currentPiece.getPieceWeight() < enemyPiece.getPieceWeight()) {
                // logger.info("Found adjacent friendly piece.");
                return true;
            }
        }

        logger.info("There are no enemy pieces with a higher piece weight");
        return false;
    }


    /**
     * Checks if a piece at the specified position is frozen.
     *
     * @param row the row index of the position
     * @param col the column index of the position
     * @return true if the piece is frozen, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean isFrozen(int row, int col) throws IndexOutOfBoundsException {
        // logger.info("Checking if piece at row " + row + ", column " + col + " is frozen.");
        Piece currentPiece = getPieceAt(row, col);
        List<int[]> adjacentPiecePositions = getAdjacentPiecePositions(row, col);

        for (int[] position : adjacentPiecePositions) {
            Piece piece = getPieceAt(position[0], position[1]);
            // ? TD: Think if I need the piece != null, since I check it in the getAdjacentPiecePositions() method.
            if (piece != null && currentPiece != null && currentPiece.getColor() != piece.getColor() && currentPiece.getPieceWeight() < piece.getPieceWeight() && !hasAdjacentFriendlyPieces(row, col)) {
                logger.info("Piece is frozen.");
                return true;
            }
        }
    
        // logger.info("Piece is not frozen.");
        return false;
    }

    
    /**
     * Moves a piece from one position on the board to another while checking if the move is valid according to the game's rules.
     * 
     * @param fromRow the row index of the piece's current position
     * @param fromCol the column index of the piece's current position
     * @param toRow the row index of the piece's new position
     * @param toCol the column index of the piece's new position
     * @throws IllegalArgumentException if the piece doesn't exist at the specified location, if the piece is frozen, if the move is invalid (not one-step or rabbit moving backwards), or if there is already a piece at the new position
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol, boolean pushingOrPullingOverride) throws IllegalArgumentException, IndexOutOfBoundsException {
        logger.info("Attempting to move piece from row " + fromRow + ", column " + fromCol + " to row " + toRow + ", column " + toCol + ".");

        // Check if the piece exists
        if (!isOccupied(fromRow, fromCol)) {
            logger.warning("No piece at the specified location.");
            throw new IllegalArgumentException("There is no piece at the specified location!");
        }

        // Check if the piece is frozen
        if (isFrozen(fromRow, fromCol) && pushingOrPullingOverride != true) {
            logger.warning("Piece is frozen.");
            throw new IllegalArgumentException("This piece is frozen!");
        }

        // Check if the move is one-step
        if(!isOneStep(fromRow, fromCol, toRow, toCol)) {
            logger.warning("Invalid move, not one-step.");
            throw new IllegalArgumentException("This move is invalid, you can only move 1 space at a time!");
        }

        // Check if the peice is a rabbit trying to move backwards
        if(!isRabbitMoveValid(fromRow, fromCol, toRow, toCol) && pushingOrPullingOverride != true) {
            logger.warning("Invalid move, rabbits cannot move backwards.");
            throw new IllegalArgumentException("This move is invalid, a rabbit piece cannot move backwards!");
        }

        Piece piece = getPieceAt(fromRow, fromCol);
        
        // Check if the new position is occupied and if not, move the piece
        if (!isOccupied(toRow, toCol)) {
            logger.info("New position is not occupied. Moving piece.");
            removePiece(fromRow, fromCol);
            setPiece(piece, toRow, toCol);
        } else {
            logger.warning("New position is occupied.");
            throw new IllegalArgumentException("There is already a piece at the new position!");
        }

        // After moving the piece, check if some conditions (for example being in a trap and not being protected anymore) didn't change for the adjacent pieces at the previous position 
        List<int[]> adjacentPeicePositionsFrom = getAdjacentPiecePositions(fromRow, fromCol);
        
        for (int[] position : adjacentPeicePositionsFrom) {
            int positionFromRow = position[0];
            int positionFromCol = position[1];

            if(isInTrap(positionFromRow, positionFromCol) && !hasAdjacentFriendlyPieces(positionFromRow, positionFromCol)) {
                // logger.info("Adjacent piece at previous position is in trap and not protected. Removing piece.");
                removePiece(positionFromRow, positionFromCol);
                return;
            }
        }

        // Check after moving the piece, if the piece is in a trap
        if(isInTrap(toRow, toCol) && !hasAdjacentFriendlyPieces(toRow, toCol)) {
            // logger.info("Moved piece is in trap and not protected. Removing piece.");
            removePiece(toRow, toCol);
            return;
        }

        // After moving the piece, check if some conditions (for example being in a trap and not being protected anymore) didn't change for the adjacent pieces at the new position 
        List<int[]> adjacentPeicePositionsTo = getAdjacentPiecePositions(toRow, toCol);

        for (int[] position : adjacentPeicePositionsTo) {
            int positionToRow = position[0];
            int positionToCol = position[1];

            if(isInTrap(positionToRow, positionToCol) && !hasAdjacentFriendlyPieces(positionToRow, positionToCol)) {
                // logger.info("Adjacent piece at new position is in trap and not protected. Removing piece.");
                removePiece(positionToRow, positionToCol);
                return;
            }
        }

        logger.info("Piece moved successfully.");
    }


    // -------------------- Game end conditions  --------------------
    /**
     * Checks if the game is won by the current player.
     *
     * @param currentPlayer the player for whom the game is being checked
     * @return true if the game is won by the current player, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean isGameWon(Player currentPlayer) throws IndexOutOfBoundsException {
        // logger.info("Checking if game is won by current player: " + currentPlayer.getColor());
        if(isRabbitOnTheOtherSide(currentPlayer)) {
            logger.info("Game is won by current player.");
            return true;
        }

        // logger.info("Game is not won by current player.");
        return false;
    }


    /**
     * Checks if the game is lost by the current player.
     *
     * @param currentPlayer the player for whom the game is being checked
     * @return true if the game is lost by the current player, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean isGameLost(Player currentPlayer) throws IndexOutOfBoundsException {
        // logger.info("Checking if game is lost by current player: " + currentPlayer.getColor());
        if(isEveryPieceFrozen(currentPlayer) || isPlayerUnableToMove(currentPlayer) || isPlayerWithoutAllRabbits(currentPlayer)) {
            logger.info("Game is lost by current player.");
            return true;
        }

        // logger.info("Game is not lost by current player.");
        return false;
    }

    
    /**
     * Checks if there is a rabbit piece on the other side of the board for the given player.
     *
     * @param player the player for whom to check the rabbit piece
     * @return true if there is a rabbit piece on the other side, false otherwise
     * @throws IndexOutOfBoundsException if the row or column index is out of bounds
     */
    public boolean isRabbitOnTheOtherSide(Player player) throws IndexOutOfBoundsException {
        // logger.info("Checking if there is a rabbit piece on the other side for player: " + player.getColor());
        PieceColor playerColor = player.getColor();

        int targetRow = playerColor == PieceColor.GOLDEN ? 7 : 0;
    
        for (int col = 0; col < COL_SIZE; col++) {
            Piece piece = getPieceAt(targetRow, col);
            if (piece != null && piece.getType() == PieceType.RABBIT && piece.getColor() == playerColor) {
                // logger.info("Found rabbit piece on the other side for player.");
                return true;
            }
        }
    
        logger.info("No rabbit piece on the other side for player.");
        return false;
    }


    /**
     * Checks if every piece belonging to the current player is frozen on the board.
     *
     * @param player the player whose pieces are being checked
     * @return true if every piece belonging to the current player is frozen, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean isEveryPieceFrozen(Player player) throws IndexOutOfBoundsException {
        // logger.info("Checking if every piece belonging to the current player is frozen.");
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                Piece piece = getPieceAt(row, col);
                if (piece != null && piece.getColor() == player.getColor() && !isFrozen(row, col)) {
                    // logger.info("Found a piece that is not frozen.");
                    return false;
                }
            }
        }
        
        logger.info("Every piece belonging to the current player is frozen.");
        return true;
    }


    
    /**
     * Checks if a player is unable to make any moves on the board.
     *
     * @param player the player whose pieces are being checked
     * @return true if the player is unable to move, false otherwise
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public boolean isPlayerUnableToMove(Player player) throws IndexOutOfBoundsException {
        // logger.info("Checking if the player is unable to make any moves.");
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                Piece piece = getPieceAt(row, col);
                int numOfAdjacentPieces = getAdjacentPiecePositions(row, col).size();

                // Check if the current piece is a) not frozen and b) has less than 4 pieces adjacent to it - this way you can determine if the piece is able to move
                if (piece != null && piece.getColor() == player.getColor() && !isFrozen(row, col) && numOfAdjacentPieces < 4) {
                    // logger.info("Found a piece that can move.");
                    return false;
                }
            }
        }

        logger.info("The player is unable to make any moves.");
        return true;
    }


    /**
     * Checks if a player has no rabbits left on the board.
     *
     * @param player the player whose pieces are being checked
     * @return true if the player has no rabbits left, false otherwise
     * @throws IndexOutOfBoundsException if the row or column index is out of bounds
     */
    public boolean isPlayerWithoutAllRabbits(Player player) throws IndexOutOfBoundsException {
        // logger.info("Checking if the player: " + player.getColor() + " has no rabbits left on the board.");
        PieceColor playerColor = player.getColor();

        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                Piece piece = getPieceAt(row, col);
                if (piece != null && piece.getType() == PieceType.RABBIT && piece.getColor() == playerColor) {
                    // logger.info("Found a rabbit belonging to the player.");
                    return false;
                }
            }
        }

        logger.info("The player has no rabbits left on the board.");
        return true;
    }



    // ----- Utils -----
    public Piece[][] getBoard() {
        return this.board;
    }


    public void setBoard(Piece[][] newBoard) {
        this.board = newBoard;
    }

    /**
     * Clears the entire board by setting all positions to null.
     */
    public void clearBoard() {
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                board[row][col] = null;
            }
        }
    }
}