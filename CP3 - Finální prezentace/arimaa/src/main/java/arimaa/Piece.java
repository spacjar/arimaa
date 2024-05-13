package arimaa;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.enums.PieceState;

public class Piece {
    private PieceType type;
    private PieceColor color;
    private PieceState state;
    private int row;
    private int col;

    public Piece(PieceType type, PieceColor color, PieceState state, int row, int col) {
        this.type = type;
        this.color = color;
        this.state = state;
        this.row = row;
        this.col = col;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceState getState() {
        return state;
    }

    public void setState(PieceState state) {
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

