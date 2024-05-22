package arimaa.controllers;

import java.io.IOException;
import java.util.Map;

import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Arimaa;
import arimaa.models.Board;
import arimaa.utils.InputUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import arimaa.models.Player;

public class ArimaaController {
    private boolean isInitialized = false;
    private Board board;
    private BoardController boardController;
    private Arimaa arimaa;
    private Stage stage;
    private BorderPane root;


    public void setBoard(Board board) {
        this.board = board;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setArimaa(Arimaa arimaa) {
        this.arimaa = arimaa;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }



    public void initialize() {
        if (board != null && !isInitialized) {
            if(!arimaa.getIsSetupFinished()) {
                arimaa.initializePieces();
            }

            isInitialized = true;
            renderView();
        }
    }


    @FXML
    private Label feedbackMessage;

    @FXML
    private TextField fromRowInput;

    @FXML
    private TextField fromColInput;

    @FXML
    private TextField toRowInput;

    @FXML
    private TextField toColInput;

    @FXML
    private void submit() throws IOException {
        System.out.println("Submit");

        String fromRowInputText = fromRowInput.getText();
        String fromColInputText = fromColInput.getText();
        String toRowInputText = toRowInput.getText();
        String toColInputText = toColInput.getText();

        int fromRowInputNum = Integer.parseInt(fromRowInputText);
        int fromColInputNum = Integer.parseInt(fromColInputText);
        int toRowInputNum = Integer.parseInt(toRowInputText);
        int toColInputNum = Integer.parseInt(toColInputText);

        board.movePiece(fromRowInputNum, fromColInputNum, toRowInputNum, toColInputNum);

        boardController.displayBoard();

        feedbackMessage.setText("The selected coordinates are: " + fromRowInputText + ", " + fromColInputText + ", " + toRowInputText + ", " + toColInputText);
    }



    // Instance variables to store the piece type, row, and column
    private int chosenSetupRow;
    private int chosenSetupCol;
    private int chosenSetupPieceType;

    @FXML
    private Label pieceTypeLabelSetup;

    @FXML
    private TextField pieceTypeInputSetup;

    @FXML
    private Label rowLabelSetup;

    @FXML
    private TextField rowInputSetup;

    @FXML
    private Label colLabelSetup;

    @FXML
    private TextField colInputSetup;

    @FXML
    private Button setupButton;

    @FXML
    private Label feedbackMessageSetup;

    @FXML
    public void placeSetup() {
        try {
            chosenSetupRow = Integer.parseInt(rowInputSetup.getText());
            chosenSetupCol = Integer.parseInt(colInputSetup.getText());
            chosenSetupPieceType = Integer.parseInt(pieceTypeInputSetup.getText());

            PieceType chosenPieceType = getChosenPieceType(chosenSetupPieceType);
            Player currentPlayer = arimaa.getCurrentPlayer();
            
            arimaa.placePiece(currentPlayer, chosenPieceType, chosenSetupRow, chosenSetupCol);
            boardController.displayBoard();
            
            Map<PieceType, Integer> currentPieces = arimaa.getCurrentPieces(currentPlayer);
            boolean areCurrentPiecesAvailable = currentPieces.values().stream().anyMatch(count -> count > 0);

            if (arimaa.areAllPiecesPlaced()) {
                arimaa.setIsSetupFinished(true);
                renderView();
            }

            if (!areCurrentPiecesAvailable) {
                arimaa.changePlayer(currentPlayer);
                return;
            }

            feedbackMessageSetup.setText("");
        } catch (Exception e) {
            feedbackMessageSetup.setText(e.getMessage());
            System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
        }
    }

    private PieceType getChosenPieceType(int chosenSetupPieceType) {
        switch(chosenSetupPieceType) {
            case 1: return PieceType.RABBIT;
            case 2: return PieceType.CAT;
            case 3: return PieceType.DOG;
            case 4: return PieceType.HORSE;
            case 5: return PieceType.CAMEL;
            case 6: return PieceType.ELEPHANT;
            default: throw new IllegalArgumentException("The piece type with the number " + chosenSetupPieceType + " does not exist!" );
        }
    }

    private void printAvailablePieces(Map<PieceType, Integer> currentPieces) {
        System.out.println("Currently available pieces: ");
        for (Map.Entry<PieceType, Integer> entry : currentPieces.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }


    public void renderView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent view;
    
            if (!arimaa.getIsSetupFinished()) {
                loader.setLocation(getClass().getResource("../views/ArimaaSetupView.fxml"));
            } else {
                loader.setLocation(getClass().getResource("../views/ArimaaView.fxml"));
            }
    
            view = loader.load();
            root.setBottom(view);
    
            Object controller = loader.getController();
    
            if (controller instanceof ArimaaController) {
                ArimaaController arimaaController = (ArimaaController) controller;
                arimaaController.setBoard(board);
                arimaaController.setBoardController(boardController);
                arimaaController.setArimaa(arimaa);
                arimaaController.setRoot(root);
                arimaaController.setStage(stage);
                if (!isInitialized) {
                    arimaaController.initialize();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
