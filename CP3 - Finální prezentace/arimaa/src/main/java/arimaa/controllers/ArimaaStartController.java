package arimaa.controllers;

import arimaa.models.Arimaa;
import arimaa.models.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ArimaaStartController {
    private Arimaa arimaa;
    private Board board;

    public ArimaaStartController(Arimaa arimaa, Board board) {
        this.arimaa = arimaa;
        this.board = board;
    }

    @FXML
    public void initialize() {}

    @FXML
    private Button playerVsPlayerButton;

    @FXML
    private Button playerVsComputerButton;

    @FXML
    public void handleStartPlayerVsPlayer(ActionEvent event) {
        System.out.println("Clicked");
        arimaa.setIsGameEnd(true);
    }

    @FXML
    public void handleStartPlayerVsComputer(ActionEvent event) {
        System.out.println("Clicked");
        arimaa.setIsGameEnd(true);
    }
}
