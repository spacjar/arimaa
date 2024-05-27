package arimaa.controllers;

import arimaa.models.PlayerTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;


public class PlayerTimerController {
    private PlayerTimer goldenPlayerTimer;
    private PlayerTimer silverPlayerTimer;

    @FXML
    private Label goldenTimerLabel;
    @FXML
    private Label silverTimerLabel;

    public PlayerTimerController(PlayerTimer goldenPlayerTimer, PlayerTimer silverPlayerTimer) {
        this.goldenPlayerTimer = goldenPlayerTimer;
        this.silverPlayerTimer = silverPlayerTimer;
    }

    @FXML
    public void initialize() {
        if (goldenPlayerTimer != null && goldenTimerLabel != null) {
            goldenPlayerTimer.startTimer();
            goldenTimerLabel.textProperty().bind(Bindings.concat("Golden player spent time on their move: ", goldenPlayerTimer.timeSpentProperty()));
        }
        if (silverPlayerTimer != null && silverTimerLabel != null) {
            silverPlayerTimer.startTimer();
            silverTimerLabel.textProperty().bind(Bindings.concat("Silver player spent time on their move: ", silverPlayerTimer.timeSpentProperty()));
        }
    }

    public void startGoldenPlayerTimer() {
        if (goldenPlayerTimer != null) {
            goldenPlayerTimer.startTimer();
        }
    }

    public void stopGoldenPlayerTimer() {
        if (goldenPlayerTimer != null) {
            goldenPlayerTimer.stopTimer();
        }
    }

    public void startSilverPlayerTimer() {
        if (silverPlayerTimer != null) {
            silverPlayerTimer.startTimer();
        }
    }

    public void stopSilverPlayerTimer() {
        if (silverPlayerTimer != null) {
            silverPlayerTimer.stopTimer();
        }
    }
}