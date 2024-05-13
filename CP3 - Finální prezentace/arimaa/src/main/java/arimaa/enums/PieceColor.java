package arimaa.enums;

public enum PieceColor {
    GOLDEN {
        @Override
        public String toString() {
            return "G";
        }
    },
    SILVER {
        @Override
        public String toString() {
            return "S";
        }
    }
}