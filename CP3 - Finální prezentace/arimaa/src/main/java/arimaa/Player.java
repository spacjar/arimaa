package arimaa;

import arimaa.enums.PieceColor;

public class Player {
    private PieceColor color;

    public Player(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }
}
