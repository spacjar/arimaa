package arimaa.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ArimaaController {
    @FXML
    private Label feedbackMessage;

    @FXML
    private TextField rowInput;

    @FXML
    private TextField colInput;

    @FXML
    private void submit() throws IOException {
        System.out.println("Submit");

        String rowInputText = rowInput.getText();
        String colInputText = colInput.getText();

        System.out.println("Row: " + rowInputText);
        System.out.println("Col: " + colInputText);

        feedbackMessage.setText("Submited");
    }
}
