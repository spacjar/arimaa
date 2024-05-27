package arimaa.controllers;

import java.util.ArrayList;
import java.util.List;

public class ArimaaGameRecorderController {
    private List<String> moves;
    private int moveNumber;

    public ArimaaGameRecorderController() {
        this.moves = new ArrayList<>();
        this.moveNumber = 1;
    }

    public void recordMove(String piece, String from, String direction) {
        String move = piece + from + direction;
        moves.add(move);
    }

    public void recordPiecePlacement(String piece, String position) {
        String move = piece + position;
        moves.add(move);
    }

    public void recordRemoval(String piece, String position) {
        String move = piece + position + "x";
        moves.add(move);
    }

    public void recordResignation() {
        moves.add("resigns");
    }

    public void recordLoss() {
        moves.add("lost");
    }

    public void recordTakeback() {
        moves.add("takeback");
    }

    public String getGameRecord() {
        StringBuilder gameRecord = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                gameRecord.append(moveNumber).append("g ");
            } else {
                gameRecord.append(moveNumber).append("s ");
                moveNumber++;
            }
            gameRecord.append(moves.get(i)).append("\n");
        }
        return gameRecord.toString();
    }
}
