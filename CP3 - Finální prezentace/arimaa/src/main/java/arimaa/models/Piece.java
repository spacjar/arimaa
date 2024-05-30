package arimaa.models;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;

public class Piece {
    private PieceType type;
    private PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    
    // ----- Getters and setters -----
    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }


    @Override
    public String toString() {
        return color.toString() + "-" + type.toString();
    }


    /**
     * Returns the weight of the piece based on its type.
     *
     * @return the weight of the piece
     * @throws IllegalArgumentException if the piece type is not valid
     */
    public int getPieceWeight() throws IllegalArgumentException {
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


    /**
     * Returns the prefix of the piece based on its type.
     * 
     * @return the prefix of the piece
     * @throws IllegalArgumentException if the piece type is unexpected
     */
    public String getPiecePrefix() throws IllegalArgumentException {
        switch(this.type) {
            case RABBIT:
                return "R";
            case CAT:
                return "C";
            case DOG:
                return "D";
            case HORSE: 
                return "H";
            case CAMEL:
                return "M";
            case ELEPHANT:
                return "E";
            default:
                throw new IllegalArgumentException("Unexpected value: " + this.type);
        }
    }
}

