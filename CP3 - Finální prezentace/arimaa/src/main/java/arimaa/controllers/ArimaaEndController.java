package arimaa.controllers;

import arimaa.models.Arimaa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ArimaaEndController {
    private Arimaa arimaa;

    public ArimaaEndController(Arimaa arimaa) {
        this.arimaa = arimaa;
    }


    @FXML
    private Button returnToMenu;

    @FXML
    private Label endGameResultLabel;


    @FXML
    public void initialize() {
        if(arimaa.getWinner() != null) {
            endGameResultLabel.setText(arimaa.getWinner() + " won the game!");
        }
    }


    @FXML
    public void handleReturnToMenu(ActionEvent event) {
        arimaa.resetGameRound();
        arimaa.setIsPushing(false);
        arimaa.setIsPlayingAgainstComputer(false);
        arimaa.setIsGameStart(false);
        arimaa.setIsGameSetup(false);
        arimaa.setIsGameEnd(false);
    }
}
