package arimaa.controllers;

import arimaa.models.Arimaa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ArimaaEndController {
    private Arimaa arimaa;

    public ArimaaEndController(Arimaa arimaa) {
        this.arimaa = arimaa;
    }

    @FXML
    public void initialize() {}

    @FXML
    private Button returnToMenu;

    @FXML
    public void handleReturnToMenu(ActionEvent event) {
        arimaa.setIsGameStart(false);
        arimaa.setIsGameSetup(false);
        arimaa.setIsGameEnd(false);
    }
}
