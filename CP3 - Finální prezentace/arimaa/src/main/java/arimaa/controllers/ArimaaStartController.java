package arimaa.controllers;

import arimaa.models.Arimaa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ArimaaStartController {
    private Arimaa arimaa;

    public ArimaaStartController(Arimaa arimaa) {
        this.arimaa = arimaa;
    }
    

    @FXML
    public void initialize() {}


    @FXML
    private Button playerVsPlayerButton;

    @FXML
    private Button playerVsComputerButton;

    @FXML
    private Button loadGameButton;

    
    @FXML
    public void handleStartPlayerVsPlayer(ActionEvent event) {
        System.out.println("Start game player vs player");
        arimaa.setIsPlayingAgainstHuman(true);
        arimaa.setIsGameStart(true);
    }

    @FXML
    public void handleStartPlayerVsComputer(ActionEvent event) {
        System.out.println("Start game player vs robot");
        arimaa.setIsPlayingAgainstComputer(true);
        arimaa.setIsGameStart(true);
    }

    @FXML
    public void handleLoadGame(ActionEvent event) {
        System.out.println("Handle game load");
        // TODO: Load logic
    }
}
