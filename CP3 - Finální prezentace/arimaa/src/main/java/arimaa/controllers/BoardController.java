package arimaa.controllers;
import arimaa.models.Board;
import arimaa.models.Piece;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.logging.Logger;


public class BoardController {
    private Board board;
    private static final Logger logger = Logger.getLogger(BoardController.class.getName());

    public void setBoard(Board board) {
        this.board = board;
        logger.info("Board set.");
    }

    @FXML
    private GridPane boardGrid;

    public void initialize() {
        if (board != null) {
        //     for(int i = 0; i <= 7; i++) {
        //         board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN), 0, i);
        //     }
        //     for(int i = 0; i <= 7; i++) {
        //         board.setPiece(new Piece(PieceType.RABBIT, PieceColor.GOLDEN), 1, i);
        //     }

        //     for(int i = 0; i <= 7; i++) {
        //         board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.SILVER), 7, i);
        //     }
        //     for(int i = 0; i <= 7; i++) {
        //         board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER), 6, i);
        //     }
            
            logger.info("Initializing board.");
            displayBoard();
        } else {
            logger.warning("Board is null during initialization.");
        }
    }

    public void displayBoard() throws IndexOutOfBoundsException {
        logger.info("Displaying board.");

        for (int row = 0; row < board.getRowSize(); row++) {
            for (int col = 0; col < board.getColSize(); col++) {
                Pane square = new Pane();
                square.setPrefSize(60, 60);
                if ((row + col) % 2 == 0) {
                    square.setStyle("-fx-background-color: white;");
                } else {
                    square.setStyle("-fx-background-color: gray;");
                }

                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    Label label = new Label(piece.getColor() + "-" + piece.getType().toString());
                    square.getChildren().add(label);
                }

                boardGrid.add(square, col, row);
            }
        }
    }
}
