package arimaa.controllers;

import java.io.IOException;

import arimaa.models.Arimaa;
import arimaa.models.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ArimaaController {
    private Board board;
    private BoardController boardController;
    private Arimaa arimaa;

    
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setArimaa(Arimaa arimaa) {
        this.arimaa = arimaa;
    }

    public void initialize() {
        if (board != null) {}
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
}
