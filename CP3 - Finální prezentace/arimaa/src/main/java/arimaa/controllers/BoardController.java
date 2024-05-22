package arimaa.controllers;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Board;
import arimaa.models.Piece;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.logging.Logger;


public class BoardController {
    private Board board;
    private static final Logger logger = Logger.getLogger(BoardController.class.getName());


    // --- Getters and setters ---
    public void setBoard(Board board) {
        this.board = board;
        logger.info("Board set.");
    }

    
    @FXML
    private GridPane boardGrid;


    /**
     * Initializes the board.
     * If the board is not null, it logs an info message and displays the board.
     * If the board is null, it logs a warning message.
     */
    public void initialize() {
        if (board != null) {            
            logger.info("Initializing board.");
            displayBoard();
        } else {
            logger.warning("Board is null during initialization.");
        }
    }


    /**
     * Displays the board by creating a graphical representation of the game board.
     * Each square on the board is represented by a Pane object with a specific background color.
     * If a piece is present on a square, a label displaying the piece's color and type is added to the Pane.
     * The Panes are then added to the boardGrid, which is a GridPane object.
     *
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public void displayBoard() throws IndexOutOfBoundsException {
        logger.info("Displaying board.");

        for (int row = 0; row < board.getRowSize(); row++) {
            for (int col = 0; col < board.getColSize(); col++) {
                Pane square = new Pane();
                square.setPrefSize(120, 120);

                if (board.isInTrap(row, col)) {
                    square.setStyle("-fx-background-color: #1C1212; -fx-border-color: #1C1212;");
                } else {
                    square.setStyle("-fx-background-color: #DADADA; -fx-border-color: #1C1212;");
                }

                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    Label label = new Label(piece.getColor() + "-" + piece.getType().toString());
                    Circle pieceCircle = new Circle(40);
                    
                    switch (piece.getColor()) {
                        case GOLDEN:
                            pieceCircle.setFill(Color.GOLDENROD);
                            label.setStyle("-fx-text-fill: #000000;");
                            break;
                        case SILVER:
                            pieceCircle.setFill(Color.SILVER);
                            label.setStyle("-fx-text-fill: #000000;");
                            break;
                        default:
                            break;
                    }

                    if(board.isFrozen(row, col)) {
                        pieceCircle.setFill(Color.BLUE);
                        label.setStyle("-fx-text-fill: #FFFFFF;");
                    }
                    
                        StackPane stack = new StackPane();
                        stack.getChildren().addAll(pieceCircle, label);
                        stack.setAlignment(Pos.CENTER); // Align children to the center
                        square.getChildren().add(stack);
                }

                boardGrid.add(square, col, row);
            }
        }
    }

    public void setupBoardDev() {
        logger.info("Automatically placing pieces.");

        board.setPiece(new Piece(PieceType.CAT, PieceColor.GOLDEN), 0, 0);
        board.setPiece(new Piece(PieceType.CAT, PieceColor.GOLDEN), 0, 1);
        board.setPiece(new Piece(PieceType.DOG, PieceColor.GOLDEN), 0, 2);
        board.setPiece(new Piece(PieceType.DOG, PieceColor.GOLDEN), 0, 3);
        board.setPiece(new Piece(PieceType.HORSE, PieceColor.GOLDEN), 0, 4);
        board.setPiece(new Piece(PieceType.HORSE, PieceColor.GOLDEN), 0, 5);
        board.setPiece(new Piece(PieceType.CAMEL, PieceColor.GOLDEN), 0, 6);
        board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN), 0, 7);

        for (int i = 0; i < 8; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.GOLDEN), 1, i);
        }

        board.setPiece(new Piece(PieceType.CAT, PieceColor.SILVER), 7, 0);
        board.setPiece(new Piece(PieceType.CAT, PieceColor.SILVER), 7, 1);
        board.setPiece(new Piece(PieceType.DOG, PieceColor.SILVER), 7, 2);
        board.setPiece(new Piece(PieceType.DOG, PieceColor.SILVER), 7, 3);
        board.setPiece(new Piece(PieceType.HORSE, PieceColor.SILVER), 7, 4);
        board.setPiece(new Piece(PieceType.HORSE, PieceColor.SILVER), 7, 5);
        board.setPiece(new Piece(PieceType.CAMEL, PieceColor.SILVER), 7, 6);
        board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.SILVER), 7, 7);

        for (int i = 0; i < 8; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER), 6, i);
        }

        displayBoard();
    }
}
