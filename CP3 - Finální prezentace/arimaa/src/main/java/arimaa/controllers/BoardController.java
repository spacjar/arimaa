package arimaa.controllers;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Board;
import arimaa.models.Piece;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.logging.Logger;


public class BoardController {
    private Board board;
    private ArimaaController arimaaController;
    private static final Logger logger = Logger.getLogger(BoardController.class.getName());

    private Integer fromRow = null;
    private Integer fromCol = null;
    private Integer toRow = null;
    private Integer toCol = null;

    // --- Getters and setters ---
    public void setBoard(Board board) {
        this.board = board;
        logger.info("Board set.");
    }

    public void setArimaaController(ArimaaController arimaaController) {
        this.arimaaController = arimaaController;
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
     * If a piece is present on a square, a circle and a label displaying the piece's color and type is added to the Pane.
     * The Panes are then added to the boardGrid, which is a GridPane object.
     *
     * @throws IndexOutOfBoundsException if the specified row or column exceeds the board size
     */
    public void displayBoard() throws IndexOutOfBoundsException {
        logger.info("Displaying board.");

        // Iterate over each row and col
        for (int row = 0; row < board.getRowSize(); row++) {
            for (int col = 0; col < board.getColSize(); col++) {
                // logger.info("Processing square at row " + row + ", col " + col);
                // Create a new Pane for the square
                Pane square = new Pane();
                square.setPrefSize(120, 120);

                // Add a click event handler to the square
                final int finalRow = row;
                final int finalCol = col;
                square.setOnMouseClicked(event -> {
                    if (fromRow == null && fromCol == null) {
                        fromRow = finalRow;
                        fromCol = finalCol;
                        square.setStyle("-fx-border-color: red;");
                    } else if (fromRow == finalRow && fromCol == finalCol) {
                        // Deselect the "from" square
                        fromRow = null;
                        fromCol = null;
                        square.setStyle("-fx-border-color: #1C1212;");
                    } else if (fromRow != null && fromCol != null && toRow == null && toCol == null) {
                        toRow = finalRow;
                        toCol = finalCol;

                        try {
                            logger.info("Arimaa move: (" + fromRow + ", " + fromCol + " -> " + toRow + ", " + toCol + ")");
                            arimaaController.submitGameMove(fromRow, fromCol, toRow, toCol);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }

                        fromRow = null;
                        fromCol = null;
                        toCol = null;
                        toRow = null;
                    }
                    logger.info("Clicked square at row " + finalRow + ", col " + finalCol);
                });

                // Change the cursor to a hand when the mouse enters the square
                square.setOnMouseEntered(event -> {
                    // square.setStyle("-fx-border-color: red;");
                    square.setCursor(Cursor.HAND);
                });

                // Change the cursor back to the default when the mouse exits the square
                square.setOnMouseExited(event -> {
                    // square.setStyle("-fx-border-color: #1C1212;");
                    square.setCursor(Cursor.DEFAULT);
                });

                // Check if the square is a trap (and if so change the color)
                if (board.isInTrap(row, col)) {
                    square.setStyle("-fx-background-color: #1C1212; -fx-border-color: #1C1212;");
                } else {
                    square.setStyle("-fx-background-color: #DADADA; -fx-border-color: #1C1212;");
                }

                Piece piece = board.getPieceAt(row, col);
                // logger.info("Got piece at row " + row + ", col " + col);

                if (piece != null) {
                    // Label and circle representing the piece
                    Label label = new Label(piece.getColor() + "-" + piece.getType().toString()); 
                    Circle pieceCircle = new Circle(40);
                    
                    // Change color based on the piece color
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

                    // Change the color of the piece if it is frozen
                    if(board.isFrozen(row, col)) {
                        pieceCircle.setFill(Color.BLUE);
                        label.setStyle("-fx-text-fill: #FFFFFF;");
                    }
                    
                    // Create a StackPane, add the circle and the label to it
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(pieceCircle, label);
                    stack.setAlignment(Pos.CENTER);
                    square.getChildren().add(stack);
                }

                // Add the square to the board grid
                // logger.info("Adding square to board grid at row " + row + ", col " + col);
                boardGrid.add(square, col, row);
            }

        }
        logger.info("Finished displaying board.");
    }


    /**
     * Sets up the board with predefined piece positions for development purposes.
     * Automatically places and displays pieces on the board according to the specified positions.
     */
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
