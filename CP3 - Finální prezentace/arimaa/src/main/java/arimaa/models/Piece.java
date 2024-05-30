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
        String prefix;
        switch(this.type) {
            case RABBIT:
                prefix = "R";
                break;
            case CAT:
                prefix = "C";
                break;
            case DOG:
                prefix = "D";
                break;
            case HORSE: 
                prefix = "H";
                break;
            case CAMEL:
                prefix = "M";
                break;
            case ELEPHANT:
                prefix = "E";
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + this.type);
        }

        return this.color == PieceColor.GOLDEN ? prefix : prefix.toLowerCase();
    }   
}

