package arimaa.models;

import arimaa.Main;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashMap;
import java.util.Map;


public class Arimaa {
    // Game board (model)
    // private Main main;
    private Board board;

    // public Arimaa(Main main, Board board) {
    public Arimaa(Board board) {
        // this.main = main;
        this.board = board;
    }

    // Game state
    private BooleanProperty isGameStart = new SimpleBooleanProperty(false);
    private BooleanProperty isGameSetup = new SimpleBooleanProperty(false);
    private BooleanProperty isGameEnd = new SimpleBooleanProperty(false);

    // Player playing against a robot or a human
    private boolean isPlayerPlayingAgainstHuman = false;
    private boolean isPlayerPlayingAgainstComputer = false;

    // Golden player
    private Player goldenPlayer = new Player(PieceColor.GOLDEN);
    private int goldenPlayerMoves = 4;

    // Silver player
    private Player silverPlayer = new Player(PieceColor.SILVER);
    private int silverPlayerMoves = 4;

    // Current player (first is always the golden player)
    private Player currentPlayer = goldenPlayer;

    // Hashmaps for pieces (setup)
    private Map<PieceType, Integer> goldenPieces = new HashMap<>();
    private Map<PieceType, Integer> silverPieces = new HashMap<>();
    private Map<PieceType, Integer> currentPieces = currentPlayer.getColor() == PieceColor.GOLDEN ? goldenPieces : silverPieces;

    // Check if all of the pieces are placed during the setup
    private boolean allGoldenPiecesPlaced = false;
    private boolean allSilverPiecesPlaced = false;

    // Check if the player is pushing or pulling
    private boolean isPushing = false;
    private boolean isPulling = false;
    private Integer pushingFromRow = null;
    private Integer pushingFromCol = null;
    private Integer pullingFromRow = null;
    private Integer pullingFromCol = null;

    // Previous move
    private Integer previousMoveFromRow = null;
    private Integer previousMoveFromCol = null;
    private Integer previousMoveToRow = null;
    private Integer previousMoveToCol = null;

    // End game (winner)
    private Player winner;

    // File upload
    private boolean isGameUploaded = false;

    public boolean getIsGameUploaded() {
        return isGameUploaded;
    }

    public void setIsGameUploaded(boolean isGameUploaded) {
        this.isGameUploaded = isGameUploaded;
    }




    // ----- Getters and setters -----
    // --- Game logic ---
    // public boolean getIsSetupFinished() {
    //     return isGameSetup;
    // }
    
    // public void setIsSetupFinished(boolean isGameSetup) {
    //     this.isGameSetup = isGameSetup;
    // }
    
    // public boolean getIsGameRunning() {
    //     return isGameStart;
    // }
    
    // public void setIsGameRunning(boolean isGameStart) {
    //     this.isGameStart = isGameStart;
    // }

    // Get game state
    public boolean getIsGameStart() {
        return isGameStart.get();
    }

    public void setIsGameStart(boolean isGameStart) {
        this.isGameStart.set(isGameStart);
    }

    public BooleanProperty isGameStartProperty() {
        return isGameStart;
    }

    public boolean getIsGameSetup() {
        return isGameSetup.get();
    }

    public void setIsGameSetup(boolean isGameSetup) {
        this.isGameSetup.set(isGameSetup);
    }

    public BooleanProperty isGameSetupProperty() {
        return isGameSetup;
    }

    public boolean getIsGameEnd() {
        return isGameEnd.get();
    }

    public void setIsGameEnd(boolean isGameEnd) {
        this.isGameEnd.set(isGameEnd);
    }

    public BooleanProperty isGameEndProperty() {
        return isGameEnd;
    }

    // --- Is playing against player or robot ---
    public boolean getIsPlayingAgainstHuman() {
        return isPlayerPlayingAgainstHuman;
    }

    public void setIsPlayingAgainstHuman(boolean isPlayerPlayingAgainstHuman) {
        this.isPlayerPlayingAgainstHuman = isPlayerPlayingAgainstHuman;
    }

    public boolean getIsPlayingAgainstComputer() {
        return isPlayerPlayingAgainstComputer;
    }

    public void setIsPlayingAgainstComputer(boolean isPlayerPlayingAgainstComputer) {
        this.isPlayerPlayingAgainstComputer = isPlayerPlayingAgainstComputer;
    }
    

    // --- Players ---
    public Player getGoldenPlayer() {
        return goldenPlayer;
    }
    
    public void setGoldenPlayer(Player goldenPlayer) {
        this.goldenPlayer = goldenPlayer;
    }

    public Player getSilverPlayer() {
        return silverPlayer;
    }
    
    public void setSilverPlayer(Player silverPlayer) {
        this.silverPlayer = silverPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getOtherPlayer(Player currentPlayer) {
        return (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }

    public void switchPlayers() {
        this.currentPlayer = (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }

    public void changePlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer.getColor() == PieceColor.GOLDEN ? silverPlayer : goldenPlayer;
    }
    

    // --- Player moves ---
    public int getGoldenPlayerMoves() {
        return goldenPlayerMoves;
    }

    public void setGoldenPlayerMoves(int goldenPlayerMoves) {
        this.goldenPlayerMoves = goldenPlayerMoves;
    }

    public int getSilverPlayerMoves() {
        return silverPlayerMoves;
    }

    public void setSilverPlayerMoves(int silverPlayerMoves) {
        this.silverPlayerMoves = silverPlayerMoves;
    }

    public int getPlayersMoves(Player player) {
        if (currentPlayer == goldenPlayer) {
            return getGoldenPlayerMoves();
        }
        
        if (currentPlayer == silverPlayer) {
            return getSilverPlayerMoves();
        } 
        
        throw new IllegalStateException("Current player is neither golden nor silver");
    }
    

    // -- Piece setup --
    public Map<PieceType, Integer> getCurrentPieces(Player player) {
        return currentPieces;
    }

    public void setCurrentPieces(Player player) {
        this.currentPieces = getCurrentPieces(player);
    }


    // -- Pushing --
    public boolean getIsPushing() {
        return isPushing;
    }

    public void setIsPushing(boolean isPushing) {
        this.isPushing = isPushing;
    }

    public Integer[] getPushingFromCoordinates() {
        return new Integer[] {this.pushingFromRow, this.pushingFromCol};
    }

    public void setPushingFromCoordinates(Integer row, Integer col) {
        this.pushingFromRow = row;
        this.pushingFromCol = col;
    }


    // -- Previous moves (pulling) --
    public Integer[] getPreviousMove() {
        return new Integer[] {this.previousMoveFromRow, this.previousMoveFromCol, this.previousMoveToRow, this.previousMoveToCol};
    }

    public void setPreviousMove(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        this.previousMoveFromRow = fromRow;
        this.previousMoveFromCol = fromCol;
        this.previousMoveToRow = toRow;
        this.previousMoveToCol = toCol;
    }


    public boolean getIsPulling() {
        return isPulling;
    }

    public void setIsPulling(boolean isPulling) {
        this.isPulling = isPulling;
    }

    public Integer[] getPullingFromCoordinates() {
        return new Integer[] {this.pullingFromRow, this.pullingFromCol};
    }

    public void setPullingFromCoordinates(Integer row, Integer col) {
        this.pushingFromRow = row;
        this.pushingFromCol = col;
    }

    // End game (winner)
    public void setWinner(Player player) {
        this.winner = player;
    }

    public Player getWinner() {
        return this.winner;
    }
    
    



    /**
     * Decrements the number of moves for the current player.
     * If the current player is the golden player, the golden player's moves are decremented by 1.
     * If the current player is the silver player, the silver player's moves are decremented by 1.
     */
    public void decrementCurrentPlayerMoves() {
        if (currentPlayer == goldenPlayer) {
            goldenPlayerMoves--;
        } else if (currentPlayer == silverPlayer) {
            silverPlayerMoves--;
        }
    }
    

    /**
     * Resets the number of moves for the current player.
     * If the current player is the golden player, the number of moves is set to 4.
     * If the current player is the silver player, the number of moves is set to 4.
     */
    public void resetCurrentPlayerMoves() {
        if (currentPlayer == goldenPlayer) {
            goldenPlayerMoves = 4;
        } else {
            silverPlayerMoves = 4;
        }
    }


    /**
     * Checks if the current player is out of moves.
     *
     * @return true if the current player is out of moves, false otherwise.
     */
    public boolean isCurrentPlayerOutOfMoves() {
        return (currentPlayer == goldenPlayer && goldenPlayerMoves == 0) || (currentPlayer == silverPlayer && silverPlayerMoves == 0);
    }

    
    /**
     * Sets up the game by initializing the piece counts, allowing players to place their pieces on the board, and checking if all pieces have been placed for both players.
     * 
     * This method loops until all pieces have been placed for both players. It prompts the current player to choose
     * a piece type and a location on the board to place the piece. The method also checks if the chosen piece type
     * is valid and if the chosen location is available. If any errors occur during the setup process, an error message
     * is displayed.
     * 
     * After all pieces have been placed, the method sets the game control value of the setup as finished.
     */
    public void initializePieces() {
        for (PieceType type : PieceType.values()) {
            int count = 0;

            if (type == PieceType.ELEPHANT || type == PieceType.CAMEL) {
                count = 1;
            } else if (type == PieceType.HORSE || type == PieceType.DOG || type == PieceType.CAT) {
                count = 2;
            } else if (type == PieceType.RABBIT) {
                count = 8;
            }

            goldenPieces.put(type, count);
            silverPieces.put(type, count);
        }
    }


    /**
     * Checks if all pieces have been placed on the board.
     *
     * @return true if all golden and silver pieces have been placed, false otherwise.
     */
    public boolean areAllPiecesPlaced() {
        return allGoldenPiecesPlaced && allSilverPiecesPlaced;
    }


    /**
     * Sets up (places) a piece on the game board for the current player.
     * 
     * @param currentPlayer The current player who is placing the piece.
     * @param chosenPieceType The type of piece to be placed.
     * @param chosenRow The row where the piece will be placed.
     * @param chosenCol The column where the piece will be placed.
     * @throws IllegalArgumentException if the position is invalid, already occupied, or if there are no more pieces of the selected type available
     */
    public void setupPiece(Player currentPlayer, PieceType chosenPieceType, int chosenRow, int chosenCol) throws IllegalArgumentException {
        // Sets up the remaining pieces for the current player
        setCurrentPieces(currentPlayer);
        
        // 
        setupPlayerPieces(currentPlayer, currentPieces, chosenPieceType, chosenRow, chosenCol);

        allGoldenPiecesPlaced = !goldenPieces.values().stream().anyMatch(count -> count > 0);
        allSilverPiecesPlaced = !silverPieces.values().stream().anyMatch(count -> count > 0);
    }


    /**
     * Sets up a player's piece on the game board at the specified position.
     *
     * @param player the player whose pieces are being set up
     * @param piecesCount a map containing the number of each piece type the player has
     * @param chosenPieceType the type of piece to be placed on the board
     * @param row the row index of the position
     * @param col the row index of the position
     * @throws IllegalArgumentException if the position is invalid, already occupied, or if there are no more pieces of the selected type available
     */
    private void setupPlayerPieces(Player player, Map<PieceType, Integer> piecesCount, PieceType chosenPieceType, int row, int col) throws IllegalArgumentException {
        if (player.getColor() == PieceColor.GOLDEN && row > 1) {
            throw new IllegalArgumentException("Golden player can only place pieces at row 0 or 1!");
        }
    
        if (player.getColor() == PieceColor.SILVER && row < 6) {
            throw new IllegalArgumentException("Silver player can only place pieces at row 6 or 7!");
        }
    
        if (board.getPieceAt(row, col) != null) {
            throw new IllegalArgumentException("There is already a piece at the selected position!");
        }
    
        if (piecesCount.get(chosenPieceType) <= 0) {
            throw new IllegalArgumentException("There are no more pieces of the selected type are available!");
        }
    
        Piece piece = new Piece(chosenPieceType, player.getColor());
        board.setPiece(piece, row, col);
        piecesCount.put(chosenPieceType, piecesCount.get(chosenPieceType) - 1);
    }
}