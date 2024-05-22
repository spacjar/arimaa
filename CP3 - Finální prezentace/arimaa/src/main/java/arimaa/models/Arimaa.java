package arimaa.models;
import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.utils.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Arimaa {
    // Game board
    private Board board;

    // Game state
    private boolean isSetupFinished = false;
    private boolean isGameRunning = false;

    // Golden player
    private Player goldenPlayer = new Player(PieceColor.GOLDEN);
    private int goldenPlayerMoves = 4;

    // Silver player
    private Player silverPlayer = new Player(PieceColor.SILVER);
    private int silverPlayerMoves = 4;

    // Current player (first is always the golden player)
    private Player currentPlayer = goldenPlayer;

    private Map<PieceType, Integer> goldenPieces = new HashMap<>();
    private Map<PieceType, Integer> silverPieces = new HashMap<>();
    private Map<PieceType, Integer> currentPieces = currentPlayer.getColor() == PieceColor.GOLDEN ? goldenPieces : silverPieces;
    private boolean allGoldenPiecesPlaced = false;
    private boolean allSilverPiecesPlaced = false;


    // ----- Getters and setters -----
    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean getIsSetupFinished() {
        return isSetupFinished;
    }
    
    public void setIsSetupFinished(boolean isSetupFinished) {
        this.isSetupFinished = isSetupFinished;
    }
    
    public boolean getIsGameRunning() {
        return isGameRunning;
    }
    
    public void setIsGameRunning(boolean isGameRunning) {
        this.isGameRunning = isGameRunning;
    }
    
    public Player getGoldenPlayer() {
        return goldenPlayer;
    }
    
    public void setGoldenPlayer(Player goldenPlayer) {
        this.goldenPlayer = goldenPlayer;
    }
    
    public int getGoldenPlayerMoves() {
        return goldenPlayerMoves;
    }
    
    public void setGoldenPlayerMoves(int goldenPlayerMoves) {
        this.goldenPlayerMoves = goldenPlayerMoves;
    }
    
    public Player getSilverPlayer() {
        return silverPlayer;
    }
    
    public void setSilverPlayer(Player silverPlayer) {
        this.silverPlayer = silverPlayer;
    }
    
    public int getSilverPlayerMoves() {
        return silverPlayerMoves;
    }
    
    public void setSilverPlayerMoves(int silverPlayerMoves) {
        this.silverPlayerMoves = silverPlayerMoves;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOtherPlayer() {
        return (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }
    
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Map<PieceType, Integer> getCurrentPieces(Player player) {
        return player.getColor() == PieceColor.GOLDEN ? goldenPieces : silverPieces;
    }

    public void setCurrentPieces(Player player) {
        this.currentPieces = getCurrentPieces(player);
    }

    public void changePlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer.getColor() == PieceColor.GOLDEN ? silverPlayer : goldenPlayer;
    }

    public void decrementCurrentPlayerMoves() {
        if (currentPlayer == goldenPlayer) {
            goldenPlayerMoves--;
        } else if (currentPlayer == silverPlayer) {
            silverPlayerMoves--;
        }
    }

    public boolean isCurrentPlayerOutOfMoves() {
        return (currentPlayer == goldenPlayer && goldenPlayerMoves == 0) || (currentPlayer == silverPlayer && silverPlayerMoves == 0);
    }
    

    
    public void resetCurrentPlayerMoves() {
        if (currentPlayer == goldenPlayer) {
            goldenPlayerMoves = 4;
        } else {
            silverPlayerMoves = 4;
        }
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


    public boolean areAllPiecesPlaced() {
        return allGoldenPiecesPlaced && allSilverPiecesPlaced;
    }


    public void placePiece(Player currentPlayer, PieceType chosenPieceType, int chosenRow, int chosenCol) {
        setCurrentPieces(currentPlayer);
        
        setupPlayerPieces(currentPlayer, currentPieces, chosenPieceType, chosenRow, chosenCol);


        allGoldenPiecesPlaced = !goldenPieces.values().stream().anyMatch(count -> count > 0);
        allSilverPiecesPlaced = !silverPieces.values().stream().anyMatch(count -> count > 0);
    }


    public void saveGame(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // TODO: Implement
        } catch (IOException e) {
            System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
        }
    }


    public void loadGame(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            // TODO: Implement
        } catch (IOException e) {
            System.err.println("\n----------\n(!) ERROR: " + e.getMessage() + "\n----------\n");
        }
    }

}