package arimaa.models;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class PlayerTimer {
    private long startTime;
    private Task<Void> task;
    private boolean isRunning;
    private StringProperty timeSpent;

    public PlayerTimer() {
        this.isRunning = false;
        this.timeSpent = new SimpleStringProperty();
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;

        this.task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (isRunning) {
                    long timeSpentMillis = System.currentTimeMillis() - startTime;
                    Platform.runLater(() -> timeSpent.set(formatTime(timeSpentMillis)));
                    Thread.sleep(1000);
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void stopTimer() {
        this.isRunning = false;
    }

    public StringProperty timeSpentProperty() {
        return timeSpent;
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
