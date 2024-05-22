package arimaa.controllers;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Board;
import arimaa.models.Piece;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class BoardController {
    @FXML
    private GridPane boardGrid;

    public void initialize() {
        Board board = new Board();

        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.GOLDEN), 0, i);
        }
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.GOLDEN), 1, i);
        }

        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.ELEPHANT, PieceColor.SILVER), 7, i);
        }
        for(int i = 0; i <= 7; i++) {
            board.setPiece(new Piece(PieceType.RABBIT, PieceColor.SILVER), 6, i);
        }
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
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
