package arimaa;

import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Board;
import arimaa.models.Piece;
import arimaa.models.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private Piece piece;

    @BeforeEach
    public void setUp() {
        board = new Board();
        piece = Mockito.mock(Piece.class);
    }

    @Test
    public void testGetPieceAt() {
        board.setPiece(piece, 0, 0);
        assertEquals(piece, board.getPieceAt(0, 0));
    }

    @Test
    public void testSetPiece() {
        board.setPiece(piece, 0, 0);
        assertEquals(piece, board.getPieceAt(0, 0));
    }

    @Test
    public void testRemovePiece() {
        board.setPiece(piece, 0, 0);
        board.removePiece(0, 0);
        assertNull(board.getPieceAt(0, 0));
    }

    @Test
    public void testIsOccupied() {
        assertFalse(board.isOccupied(0, 0));
        board.setPiece(piece, 0, 0);
        assertTrue(board.isOccupied(0, 0));
    }

    @Test
    public void testIsInTrap() {
        assertFalse(board.isInTrap(0, 0));
        assertTrue(board.isInTrap(2, 2));
    }

    @Test
    public void testIsOneStep() {
        assertTrue(board.isOneStep(0, 0, 0, 1));
        assertFalse(board.isOneStep(0, 0, 1, 1));
    }

        @Test
    public void testIsRabbitMoveValid() {
        Mockito.when(piece.getType()).thenReturn(PieceType.RABBIT);
        Mockito.when(piece.getColor()).thenReturn(PieceColor.GOLDEN);
        board.setPiece(piece, 1, 0);

        // Trying to move backwards
        assertFalse(board.isRabbitMoveValid(1, 0, 0, 0));

        // Moving forwards
        assertTrue(board.isRabbitMoveValid(1, 0, 2, 0));
    }

    @Test
    public void testGetAdjacentPiecePositions() {
        board.setPiece(piece, 0, 0);
        assertEquals(0, board.getAdjacentPiecePositions(0, 0).size());
        board.setPiece(piece, 0, 1);
        assertEquals(1, board.getAdjacentPiecePositions(0, 0).size());
    }

    @Test
    public void testGetFreeAdjacentPositions() {
        assertEquals(2, board.getFreeAdjacentPositions(0, 0).size());
        assertEquals(3, board.getFreeAdjacentPositions(0, 1).size());
    }

    @Test
    public void testHasAdjacentFriendlyPieces() {
        Mockito.when(piece.getColor()).thenReturn(PieceColor.GOLDEN);
        board.setPiece(piece, 0, 0);
        assertFalse(board.hasAdjacentFriendlyPieces(0, 0));
        board.setPiece(piece, 0, 1);
        assertTrue(board.hasAdjacentFriendlyPieces(0, 0));
    }

    @Test
    public void testHasAdjacentEnemyPiecesWithHigherValue() {
        Mockito.when(piece.getColor()).thenReturn(PieceColor.GOLDEN);
        Mockito.when(piece.getPieceWeight()).thenReturn(1);
        board.setPiece(piece, 0, 0);
        assertFalse(board.hasAdjacentEnemyPiecesWithHigherValue(0, 0));
        Piece enemyPiece = Mockito.mock(Piece.class);
        Mockito.when(enemyPiece.getColor()).thenReturn(PieceColor.SILVER);
        Mockito.when(enemyPiece.getPieceWeight()).thenReturn(2);
        board.setPiece(enemyPiece, 0, 1);
        assertTrue(board.hasAdjacentEnemyPiecesWithHigherValue(0, 0));
    }

    @Test
    public void testIsFrozen() {
        Mockito.when(piece.getColor()).thenReturn(PieceColor.GOLDEN);
        Mockito.when(piece.getPieceWeight()).thenReturn(1);
        board.setPiece(piece, 0, 0);
        assertFalse(board.isFrozen(0, 0));
        Piece enemyPiece = Mockito.mock(Piece.class);
        Mockito.when(enemyPiece.getColor()).thenReturn(PieceColor.SILVER);
        Mockito.when(enemyPiece.getPieceWeight()).thenReturn(2);
        board.setPiece(enemyPiece, 0, 1);
        assertTrue(board.isFrozen(0, 0));
    }

    @Test
    void testMovePiece() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
        board.setPiece(piece, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> board.movePiece(0, 0, 2, 2, false));
        assertDoesNotThrow(() -> board.movePiece(0, 0, 0, 1, false));
    }

    @Test
    void testIsGameWon() {
        Board board = new Board();
        Player player = new Player(PieceColor.GOLDEN);
        assertFalse(board.isGameWon(player));
        Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
        board.setPiece(piece, 7, 0);
        assertTrue(board.isGameWon(player));
    }

    @Test
    void testIsGameLost() {
        Board board = new Board();
        Player player = new Player(PieceColor.GOLDEN);
        assertTrue(board.isGameLost(player));
        for (int i = 0; i < 8; i++) {
            Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
            board.setPiece(piece, 0, i);
            Piece blockingPiece = new Piece(PieceType.ELEPHANT, PieceColor.SILVER);
            board.setPiece(blockingPiece, 1, i);
        }
        assertFalse(board.isGameLost(player));
    }

    @Test
    void testIsRabbitOnTheOtherSide() {
        Board board = new Board();
        Player player = new Player(PieceColor.GOLDEN);
        assertFalse(board.isRabbitOnTheOtherSide(player));
        Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
        board.setPiece(piece, 7, 0);
        assertTrue(board.isRabbitOnTheOtherSide(player));
    }

    @Test
    void testIsEveryPieceFrozen() {
        Board board = new Board();
        Player player = new Player(PieceColor.GOLDEN);
        assertTrue(board.isEveryPieceFrozen(player));
        for (int i = 0; i < 8; i++) {
            Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
            board.setPiece(piece, 0, i);
        }
        assertFalse(board.isEveryPieceFrozen(player));
    }

    @Test
    void testIsPlayerUnableToMove() {
        Board board = new Board();
        Player player = new Player(PieceColor.GOLDEN);
        assertTrue(board.isPlayerUnableToMove(player));
        for (int i = 0; i < 8; i++) {
            Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
            board.setPiece(piece, 0, i);
        }
        assertFalse(board.isPlayerUnableToMove(player));
    }

    @Test
    void testIsPlayerWithoutAllRabbits() {
        Board board = new Board();
        Player player = new Player(PieceColor.GOLDEN);
        assertTrue(board.isPlayerWithoutAllRabbits(player));
        for (int i = 0; i < 8; i++) {
            Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
            board.setPiece(piece, 0, i);
        }
        assertFalse(board.isPlayerWithoutAllRabbits(player));
    }

    @Test
    void testClearBoard() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.RABBIT, PieceColor.GOLDEN);
        board.setPiece(piece, 0, 0);
        board.clearBoard();
        assertNull(board.getPieceAt(0, 0));
    }
}