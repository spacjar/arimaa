package arimaa.controllers;

import java.io.IOException;

import arimaa.models.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ArimaaController {
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
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

        feedbackMessage.setText("The selected coordinates are: " + fromRowInputText + ", " + fromColInputText + ", " + toRowInputText + ", " + toColInputText);
    }
}
