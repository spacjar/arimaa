package arimaa.models;

import arimaa.enums.PieceColor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Arimaa {
    private Board board;
    private Player silverPlayer;
    private Player goldenPlayer;

    public Arimaa(Board board) {
        this.board = board;
        this.goldenPlayer = new Player(PieceColor.GOLDEN);
        this.silverPlayer = new ComputerPlayer(PieceColor.SILVER, board);
    }

    // Game state
    private BooleanProperty isGameStart = new SimpleBooleanProperty(false);
    private BooleanProperty isGameSetup = new SimpleBooleanProperty(false);
    private BooleanProperty isGameEnd = new SimpleBooleanProperty(false);

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


    // Player playing against a robot or a human
    private boolean isPlayerPlayingAgainstComputer = false;

    public boolean getIsPlayingAgainstComputer() {
        return isPlayerPlayingAgainstComputer;
    }

    public void setIsPlayingAgainstComputer(boolean isPlayerPlayingAgainstComputer) {
        this.isPlayerPlayingAgainstComputer = isPlayerPlayingAgainstComputer;
    }

    
    // Players
    // Golden player
    private int goldenPlayerMoves = 4;

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

    // Silver player
    // private Player silverPlayer = isPlayerPlayingAgainstComputer ? new ComputerPlayer(PieceColor.SILVER, this, board) : new Player(PieceColor.SILVER);
    // private Player silverPlayer = new ComputerPlayer(PieceColor.SILVER, board);
    // private Player silverPlayer = new Player(PieceColor.SILVER);
    private int silverPlayerMoves = 4;
    
    public Player getSilverPlayer() {
        if (isPlayerPlayingAgainstComputer) {
            return (ComputerPlayer) silverPlayer;
        } else {
            return silverPlayer;
        }
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

    // Current player (first is always the golden player)
    private Player currentPlayer = goldenPlayer;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
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

    public Player getOtherPlayer(Player currentPlayer) {
        return (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }

    public void switchPlayers() {
        this.currentPlayer = (currentPlayer == goldenPlayer) ? silverPlayer : goldenPlayer;
    }

    public void changePlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer.getColor() == PieceColor.GOLDEN ? silverPlayer : goldenPlayer;
    }

    // End game (winner)
    private Player winner;

    public void setWinner(Player player) {
        this.winner = player;
    }

    public Player getWinner() {
        return this.winner;
    }


    // Pushing
    private boolean isPushing = false;

    public boolean getIsPushing() {
        return isPushing;
    }

    public void setIsPushing(boolean isPushing) {
        this.isPushing = isPushing;
    }


    // Previous move positions
    private Integer previousMoveFromRow = null;
    private Integer previousMoveFromCol = null;
    private Integer previousMoveToRow = null;
    private Integer previousMoveToCol = null;

    public Integer[] getPreviousMove() {
        return new Integer[] {this.previousMoveFromRow, this.previousMoveFromCol, this.previousMoveToRow, this.previousMoveToCol};
    }

    public void setPreviousMove(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        this.previousMoveFromRow = fromRow;
        this.previousMoveFromCol = fromCol;
        this.previousMoveToRow = toRow;
        this.previousMoveToCol = toCol;
    }


    // Load upload
    private boolean isGameUploaded = false;

    public boolean getIsGameUploaded() {
        return isGameUploaded;
    }

    public void setIsGameUploaded(boolean isGameUploaded) {
        this.isGameUploaded = isGameUploaded;
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
}