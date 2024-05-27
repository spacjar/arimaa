package arimaa.controllers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import arimaa.enums.PieceColor;
import arimaa.enums.PieceType;
import arimaa.models.Arimaa;
import arimaa.models.Board;
import arimaa.models.Piece;
import arimaa.models.Player;
import arimaa.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ArimaaStartController {
    private Arimaa arimaa;
    private Board board;

    public ArimaaStartController(Arimaa arimaa, Board board) {
        this.arimaa = arimaa;
        this.board = board;
    }

    // Logger
    private static final Logger logger = Logger.getLogger(ArimaaStartController.class.getName());
    

    @FXML
    public void initialize() {}


    // File utils
    FileUtils fileUtils = new FileUtils();


    @FXML
    private Button playerVsPlayerButton;

    @FXML
    private Button playerVsComputerButton;

    @FXML
    private Button loadGameButton;

    
    @FXML
    public void handleStartPlayerVsPlayer(ActionEvent event) {
        System.out.println("Start game player vs player");
        arimaa.setIsPlayingAgainstHuman(true);
        arimaa.setIsGameUploaded(false);
        arimaa.setIsGameStart(true);
    }

    @FXML
    public void handleStartPlayerVsComputer(ActionEvent event) {
        System.out.println("Start game player vs robot");
        arimaa.setIsPlayingAgainstComputer(true);
        arimaa.setIsGameUploaded(false);
        arimaa.setIsGameStart(true);
    }

    @FXML
    public void handleLoadGame(ActionEvent event) {
        System.out.println("Handle game load");
        
        String filename = "arimaaGameState_d1776cbb-89be-4612-a5a7-801b8caef73c.json"; // replace with your filename

        try {
            Map<String, Object> gameState = fileUtils.loadGame(filename);
            // Order is important
            // Set that the game is uploading
            arimaa.setIsGameUploaded(true);

            // Set the players moves
            arimaa.setGoldenPlayerMoves((Integer) gameState.get("goldenPlayerMoves"));
            arimaa.setSilverPlayerMoves((Integer) gameState.get("silverPlayerMoves"));
            // arimaa.setGoldenPlayerMoves(1);
            // arimaa.setSilverPlayerMoves(2);

            // Set if the users is playing against human or a computer
            arimaa.setIsPlayingAgainstHuman((Boolean) gameState.get("isPlayerPlayingAgainstHuman"));
            arimaa.setIsPlayingAgainstComputer((Boolean) gameState.get("isPlayerPlayingAgainstComputer"));

            // Set the current player
            HashMap<String, String> currentPlayer = (HashMap<String, String>) gameState.get("currentPlayer");
            String colorString = currentPlayer.get("color").toUpperCase();
            PieceColor color = PieceColor.valueOf(colorString);
            
            if (color == PieceColor.GOLDEN) {
                arimaa.setCurrentPlayer(arimaa.getGoldenPlayer());
            } else if (color == PieceColor.SILVER) {
                arimaa.setCurrentPlayer(arimaa.getSilverPlayer());
            }

            // Set board
            ArrayList<ArrayList<HashMap<String, Object>>> boardArray = (ArrayList<ArrayList<HashMap<String, Object>>>) gameState.get("board");
            Piece[][] boardPieces = new Piece[boardArray.size()][];

            for (int i = 0; i < boardArray.size(); i++) {
                ArrayList<HashMap<String, Object>> rowArray = boardArray.get(i);
                boardPieces[i] = new Piece[rowArray.size()];

                for (int j = 0; j < rowArray.size(); j++) {
                    HashMap<String, Object> pieceObject = rowArray.get(j);
                    if (pieceObject != null) {
                        String colorStringBoard = ((String) pieceObject.get("color")).toUpperCase();
                        PieceColor colorType = PieceColor.valueOf(colorStringBoard);

                        String pieceString = (String) pieceObject.get("type");
                        PieceType pieceType = PieceType.valueOf(pieceString);
                        boardPieces[i][j] = new Piece(pieceType, colorType);
                    }
                }
            }

            board.setBoard(boardPieces);

            // Set the game state
            arimaa.setIsGameStart((Boolean) gameState.get("isGameStart"));
            arimaa.setIsGameSetup((Boolean) gameState.get("isGameSetup"));
            arimaa.setIsGameEnd((Boolean) gameState.get("isGameEnd"));
        } catch (Exception e) {
            // feedbackMessage.setText("Error while loading file: " + e.getMessage());
            logger.severe("(!) Error while loading: " + e.getMessage());
        }
    }
}
