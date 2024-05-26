package arimaa.controllers;

import arimaa.models.Arimaa;
import arimaa.models.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ArimaaEndController {
    private Arimaa arimaa;
    private Board board;

    public ArimaaEndController(Arimaa arimaa, Board board) {
        this.arimaa = arimaa;
        this.board = board;
    }

    @FXML
    public void initialize() {}

    @FXML
    private Button returnToMenu;

    @FXML
    public void handleReturnToMenu(ActionEvent event) {
        System.out.println("Clicked");
        arimaa.setIsGameEnd(false);
    }
}
