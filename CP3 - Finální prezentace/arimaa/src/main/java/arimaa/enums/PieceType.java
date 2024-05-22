package arimaa.enums;

public enum PieceType {
    ELEPHANT {
        @Override
        public String toString() {
            return "Elephant";
        }
    },
    CAMEL {
        @Override
        public String toString() {
            return "Camel";
        }
    },
    HORSE {
        @Override
        public String toString() {
            return "Horse";
        }
    },
    DOG {
        @Override
        public String toString() {
            return "Dog";
        }
    },
    CAT {
        @Override
        public String toString() {
            return "Cat";
        }
    },
    RABBIT {
        @Override
        public String toString() {
            return "Rabbit";
        }
    }
}
