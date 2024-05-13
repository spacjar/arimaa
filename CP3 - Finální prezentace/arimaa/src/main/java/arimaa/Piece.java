package arimaa;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.enums.PieceState;

public class Piece {
    private PieceType type;
    private PieceColor color;
    private PieceState state;

    public Piece(PieceType type, PieceColor color, PieceState state) {
        this.type = type;
        this.color = color;
        this.state = state;
    }

    @Override
    public String toString() {
        return color.toString() + "-" + type.toString();
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

    public int getPieceWeight() {
        switch(this.type) {
            case RABBIT:
                return 1;
            case CAT:
                return 2;
            case DOG:
                return 3;
            case HORSE: 
                return 4;
            case CAMEL:
                return 5;
            case ELEPHANT:
                return 6;
            default:
                throw new IllegalArgumentException("Unexpected value: " + this.type);
        }
    }

    public void setState(PieceState state) {
        this.state = state;
    }
}

