package arimaa.controllers;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Board;
import arimaa.models.Piece;
import arimaa.models.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
                if ((row + col) % 2 == 0) {
                    square.setStyle("-fx-background-color: white;");
                } else {
                    square.setStyle("-fx-background-color: gray;");
                }

                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    Label label = new Label(piece.getColor() + "-" + piece.getType().toString() + (board.isFrozen(row, col) ? "-FRZ" : ""));

                    
                    switch (piece.getColor()) {
                        case GOLDEN:
                            label.setStyle("-fx-text-fill: goldenrod;");
                            break;
                        case SILVER:
                            label.setStyle("-fx-text-fill: silver;");
                            break;
                        default:
                            break;
                    }

                    if(board.isFrozen(row, col)) {
                        label.setStyle("-fx-text-fill: blue;");
                    }
                    
                    square.getChildren().add(label);
                }

                boardGrid.add(square, col, row);
            }
        }
    }

    public void setupBoardDev() {
        for (int i = 0; i < 8; i++) {
            board.setPiece(new Piece(PieceType.RABBIT,PieceColor.GOLDEN), 0, i);
        }
        for (int i = 0; i < 8; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT,PieceColor.GOLDEN), 1, i);
        }
        for (int i = 0; i < 8; i++) {
            board.setPiece(new Piece(PieceType.RABBIT,PieceColor.SILVER), 6, i);
        }
        for (int i = 0; i < 8; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT,PieceColor.SILVER), 7, i);
        }

        displayBoard();
    }
}
