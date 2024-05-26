package arimaa.controllers;

import java.util.logging.Logger;

import arimaa.models.Arimaa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ArimaaSetupController {
    private Arimaa arimaa;

    public ArimaaSetupController(Arimaa arimaa) {
        this.arimaa = arimaa;
    }

    // State
    private boolean isGoldenPlayerSetupReady = false;
    private boolean isSilverPlayerSetupReady = false;

    // Logger
    private static final Logger logger = Logger.getLogger(ArimaaSetupController.class.getName());

    // UI Elements
    @FXML
    private Label setupCurrentPlayerLabel; 

    @FXML
    private Label setupfeedbackMessage; 
    
    @FXML
    private Button setupGoldenPlayerReadyButton;

    @FXML
    private Button setupSilverPlayerReadyButton;

    @FXML
    private Button setupAllPlayersReadyButton;


    // Init
    @FXML
    public void initialize() {
        isGoldenPlayerSetupReady = false;
        isSilverPlayerSetupReady = false;
        setupGoldenPlayerReadyButton.setDisable(false);
        setupSilverPlayerReadyButton.setDisable(false);
        displayCurrentPlayer();
    }


    @FXML
    public void handleGoldenPlayerReady(ActionEvent event) {
        if(!isGoldenPlayerSetupReady && !isSilverPlayerSetupReady) {
            System.out.println("Clicked");
            isGoldenPlayerSetupReady = true;
            setupGoldenPlayerReadyButton.setDisable(true);
        }
    }

    @FXML
    public void handleSilverPlayerReady(ActionEvent event) {
        if(isGoldenPlayerSetupReady && !isSilverPlayerSetupReady) {
            displayCurrentPlayer();
            System.out.println("Clicked");
            isSilverPlayerSetupReady = true;
            setupSilverPlayerReadyButton.setDisable(true);
        }
    }
    
    @FXML
    public void handleStartGame(ActionEvent event) {
        if(isGoldenPlayerSetupReady && isSilverPlayerSetupReady) {
            System.out.println("Clicked");
            // arimaa.setIsGameStart(true);
            arimaa.setIsGameSetup(true);
        }
    }


    private void displayCurrentPlayer() {
        setupCurrentPlayerLabel.setText("Current player: " + arimaa.getCurrentPlayer().toString());
    }


    // // ---------- Game setup ----------    
    // @FXML
    // // Input field for entering the type of piece during setup
    // private TextField pieceTypeInputSetup;
    
    // @FXML
    // // Input field for entering the row number during setup
    // private TextField rowInputSetup;
    
    // @FXML
    // // Input field for entering the column number during setup
    // private TextField colInputSetup;
    
    // @FXML
    // // Button for submitting the setup
    // private Button setupButton;
    
    // @FXML
    // // Label for displaying feedback messages during setup
    // private Label feedbackMessageSetup;
    
    // @FXML 
    // // Label for displaying the number of available pieces during setup
    // private Label availablePiecesSetup;
    
    
    // /**
    //  * Method for setting up the board automatically (in developer mode).
    //  * This method calls the setupBoardDev method in the boardController to set up the board.
    //  * It then sets the isSetupFinished flag in the arimaa object to true and renders the view.
    //  */
    // @FXML
    // public void devSetup() {
    //     boardController.setupBoardDev();
    //     // arimaa.setIsSetupFinished(true);
    //     arimaa.setIsGameSetup(true);
    // }
    

    // /**
    //  * Submits the game setup.
    //  * This method is called when the user submits the setup by clicking a button.
    //  * It parses the chosen row, column, and piece type from the input fields, places the chosen piece on the board, and updates the game state accordingly.
    //  * If all pieces for the golden player are placed, it starts the setup for the silver player.
    //  * If no pieces are left, it changes player and renders the game.
    //  * If an error occurs during the setup, it displays an error message.
    //  */
    // @FXML
    // public void submitSetup() {
    //     logger.info("Submitting game setup.");
        
    //     try {
    //         // Parse the chosen row, column, and piece type from the input fields
    //         int chosenSetupRow = Integer.parseInt(rowInputSetup.getText())-1;
    //         int chosenSetupCol = Integer.parseInt(colInputSetup.getText())-1;
    //         int chosenSetupPieceType = Integer.parseInt(pieceTypeInputSetup.getText());
       
    //         // Get the chosen piece type
    //         PieceType chosenPieceType = getChosenPieceType(chosenSetupPieceType);
    //         logger.info("Chosen piece type: " + chosenPieceType);
    
    //         // Get the current player
    //         Player currentPlayer = arimaa.getCurrentPlayer();
    //         logger.info("Current player: " + currentPlayer);
            
    //         // Place the chosen piece on the board
    //         arimaa.setupPiece(currentPlayer, chosenPieceType, chosenSetupRow, chosenSetupCol);
    //         logger.info("Placed piece on the board.");
            
    //         // Get the current pieces of the current player
    //         Map<PieceType, Integer> currentPieces = arimaa.getCurrentPieces(currentPlayer);
    //         // Check if there are any pieces left
    //         boolean areCurrentPiecesAvailable = currentPieces.values().stream().anyMatch(count -> count > 0);
            
    //         // Print the available pieces
    //         printAvailablePieces(currentPieces);
    
    //         // If all pieces are placed, finish the setup and change the player
    //         if (arimaa.areAllPiecesPlaced()) {
    //             // arimaa.setIsSetupFinished(true);
    //             arimaa.setIsGameSetup(true);
    //             arimaa.changePlayer(currentPlayer);
    //             logger.info("All pieces are placed. Setup is finished.");
    //             // renderView();
    //         }
    
    //         // If no pieces are left, change the player
    //         if (!areCurrentPiecesAvailable) {
    //             arimaa.changePlayer(currentPlayer);
    //             logger.info("No pieces available. Changing player.");
    //             return;
    //         }
    
    //         // Clear the feedback message
    //         feedbackMessageSetup.setText("");
    //     } catch (Exception e) {
    //         // Display the error message and log it
    //         feedbackMessage.setText(e.getMessage());
    //         logger.severe(e.getMessage());
    //     }
    // }


    // /**
    //  * Gets the piece based on the number provided
    //  * 
    //  * @param chosenSetupPieceType a piece number
    //  * @throws IllegalArgumentException the piece with the number provided does not exist
    //  */
    // private PieceType getChosenPieceType(int chosenSetupPieceType) throws IllegalArgumentException {
    //     logger.info("Getting chosen piece type: " + chosenSetupPieceType);
    //     switch(chosenSetupPieceType) {
    //         case 1: 
    //             return PieceType.RABBIT;
    //         case 2: 
    //             return PieceType.CAT;
    //         case 3: 
    //             return PieceType.DOG;
    //         case 4: 
    //             return PieceType.HORSE;
    //         case 5: 
    //             return PieceType.CAMEL;
    //         case 6: 
    //             return PieceType.ELEPHANT;
    //         default:    
    //             throw new IllegalArgumentException("The piece type with the number " + chosenSetupPieceType + " does not exist!" );
    //     }
    // }


    // /**
    //  * Prints the available pieces to the console.
    //  *
    //  * @param currentPieces a map containing the current piece types and their quantities
    //  */
    // private void printAvailablePieces(Map<PieceType, Integer> currentPieces) {
    //     logger.info("Printing available pieces.");
    //     StringBuilder piecesString = new StringBuilder("Currently available pieces: ");
    //     for (Map.Entry<PieceType, Integer> entry : currentPieces.entrySet()) {
    //         piecesString.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
    //     }
    //     // Remove the last comma and space
    //     if (piecesString.length() > 0) {
    //         piecesString.setLength(piecesString.length() - 2);
    //     }
    //     availablePiecesSetup.setText(piecesString.toString());
    // }
}
