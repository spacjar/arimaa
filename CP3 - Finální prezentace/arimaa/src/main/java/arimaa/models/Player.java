package arimaa.models;

import arimaa.enums.PieceColor;

public class Player {
    private PieceColor color;

    public Player(PieceColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color + " Player";
    }

    public PieceColor getColor() {
        return color;
    }
}
