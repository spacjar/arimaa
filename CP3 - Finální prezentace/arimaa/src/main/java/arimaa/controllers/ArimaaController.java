package arimaa.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ArimaaController {
    @FXML
    private Label feedbackMessage;

    @FXML
    private void submit() throws IOException {
        System.out.println("Submit");
        feedbackMessage.setText("It works!");
    }    
}
