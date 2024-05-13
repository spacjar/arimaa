package arimaa.enums;

public enum PieceType {
    ELEPHANT {
        @Override
        public String toString() {
            return "E";
        }
    },
    CAMEL {
        @Override
        public String toString() {
            return "C";
        }
    },
    HORSE {
        @Override
        public String toString() {
            return "H";
        }
    },
    DOG {
        @Override
        public String toString() {
            return "D";
        }
    },
    CAT {
        @Override
        public String toString() {
            return "C";
        }
    },
    RABBIT {
        @Override
        public String toString() {
            return "R";
        }
    }
}
