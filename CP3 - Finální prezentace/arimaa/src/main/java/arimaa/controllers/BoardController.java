package arimaa.controllers;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class BoardController implements Initializable {

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize() {
        // Initialize the chessboard (set up pieces)
        // ...

        // Set the square colors based on row and column
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = (Rectangle) gridPane.getChildren().get(row * 8 + col);
                square.setFill(getSquareColor(row, col));
            }
        }
    }

    private Color getSquareColor(int row, int col) {
        // Implement logic to determine the color based on row and column (e.g., alternating for checkerboard pattern)
        return (row + col) % 2 == 0 ? Color.WHITE : Color.BROWN;
    }

    // Implement methods to handle user interaction with squares (optional)
    // ...
}