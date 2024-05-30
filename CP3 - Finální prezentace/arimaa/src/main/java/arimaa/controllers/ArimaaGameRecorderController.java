package arimaa.controllers;

import arimaa.models.ArimaaGameRecorder;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ArimaaGameRecorderController {
    private ArimaaGameRecorder arimaaGameRecorder;

    @FXML
    private ListView<String> movesListView;

    public ArimaaGameRecorderController(ArimaaGameRecorder arimaaGameRecorder) {
        this.arimaaGameRecorder = arimaaGameRecorder;
    }

    public void initialize() { 
        // Add existing items to the ListView
        movesListView.getItems().addAll(arimaaGameRecorder.getMoves());

        // Add the listener
        arimaaGameRecorder.getMoves().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    movesListView.getItems().clear();
                    movesListView.getItems().addAll(arimaaGameRecorder.getMoves());
                }
            }
        });
    }
}
