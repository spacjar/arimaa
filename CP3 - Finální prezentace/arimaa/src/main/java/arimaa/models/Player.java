package arimaa.models;

import arimaa.enums.PieceColor;

public class Player {
    private PieceColor color;

    public Player(PieceColor color) {
        this.color = color;
    }

    // ----- Getters and setters -----
    public PieceColor getColor() {
        return color;
    }

    
    @Override
    public String toString() {
        return color + " Player";
    }

}
