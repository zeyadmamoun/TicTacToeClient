/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.Client;

/**
 * FXML Controller class
 *
 * @author mahmo
 */
public class ServerGameBoardController implements Initializable, Client.ServerGameHandler {

    /**
     * Initializes the controller class.
     */
    Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine;

    private char currentPlayer = 'O';

    private char[][] board = new char[3][3];
    private Button[][] buttons;
    boolean playerTurn = false;
    @FXML
    private Text playerOneName;
    @FXML

    private Text gameStatus;
    @FXML
    private Text playerOneScore;
    @FXML
    private Text playerTwoName;
    @FXML
    private Text playerTwoScore;

    private Button exit_btn;
    @FXML
    private Text currentSymbol;
    @FXML
    private Button recordBtn;
    private Recording recording;
    private boolean isRecording = false;
    private String gameId;
    private String currentPlayerName;
    private String playerTwoText;
    @FXML
    private void recordBtnHandler(ActionEvent event) {
        recordBtn.setDisable(true);
        isRecording = !isRecording;
        gameId = generateNewGameId();
        System.out.println("New game started with game ID: " + gameId);
        recordBtn.setText(isRecording ? "Recording" : "Start Recording");
        if (isRecording) {
            System.out.println("Recording started.");
        } else {
            System.out.println("Recording stopped.");
        }
    }

    private String generateNewGameId() {
        return "game" + System.currentTimeMillis();
    }
    
    @FXML
    private void buttonOneHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(0, 0);
        client.sendMoveToServer(0, 0, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonTwoHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(0, 1);
        client.sendMoveToServer(0, 1, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonThreeHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(0, 2);
        client.sendMoveToServer(0, 2, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonFourHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(1, 0);
        client.sendMoveToServer(1, 0, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonFiveHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(1, 1);
        client.sendMoveToServer(1, 1, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonSixHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(1, 2);
        client.sendMoveToServer(1, 2, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonSevenHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(2, 0);
        client.sendMoveToServer(2, 0, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonEightHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(2, 1);
        client.sendMoveToServer(2, 1, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonNineHandler(ActionEvent event) {
        if (playerTurn == false) {
            return;
        }
        handleMove(2, 2);
        client.sendMoveToServer(2, 2, String.valueOf(currentPlayer));
    }

    private void handleMove(int row, int col) {
        if (board[row][col] == ' ' && currentPlayer == 'X') {
            makeMove(row, col, 'X');
        } else if (board[row][col] == ' ' && currentPlayer == 'O') {
            makeMove(row, col, 'O');
        }
        playerTurn = false;
    }

    private void makeMove(int row, int col, char player) {
        switchPlayer();
        recordBtn.setDisable(true);
        if (isRecording) {
            recording.recordMove(row, col, player, currentPlayerName, gameId);
        }
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setDisable(true);
    }
    private void switchPlayer(){
        if(currentPlayerName == client.getUserName()){
            currentPlayerName = playerTwoText;
        }else{
            currentPlayerName = client.getUserName();
        }
    }
    private void initializeGame() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'O';

    }

    void exitGame() {
        client.exitGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        client = Client.getInstance();
        currentPlayerName = client.getUserName();
        client.setServerGameHandler(this);
        buttons = new Button[][]{{buttonOne, buttonTwo, buttonThree}, {buttonFour, buttonFive, buttonSix}, {buttonSeven, buttonEight, buttonNine}};
        recording = new Recording();
        gameId = generateNewGameId();
        initializeGame();
    }

    @Override
    public void drawMoveFromServer(int row, int col, String token) {
        makeMove(row, col, token.charAt(0));
        playerTurn = true;
    }

    @Override
    public void startGame() {
        currentPlayer = 'X';
        playerTurn = true;
    }

    @Override
    public void playersInfo(String playerTwoName, int playerOneScore, int playerTwoScore) {
        this.playerTwoText = playerTwoName;
        this.playerOneName.setText("Me");
        this.playerOneScore.setText(Integer.toString(playerOneScore));
        this.playerTwoName.setText(playerTwoName);
        this.playerTwoScore.setText(Integer.toString(playerTwoScore));
        System.out.println("playeres info " + client.getUserName());
    }

    @Override
    public void winnerAction() {
        System.out.println("you won");

        // playerOneName.setText("you won");
        playerOneName.setText("you won");
        showVideoForResult("winner");

    }

    @Override
    public void loseAction() {
        System.out.println("you lose");
        //playerOneName.setText("you lost ");
        playerOneName.setText("you lost ");
        showVideoForResult("looser");
    }

    @Override
    public void exitSession() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/DashBoardScreen.fxml"));
            root = loader.load();
            // Get the current stage and set the new scene
            stage = (Stage) playerOneName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ServerGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showVideoForResult(String result) {
    String videoPath;

    if ("winner".equals(result)) {
        videoPath = "file:/C:/Downloads/winner.mp4";
    } else if ("looser".equals(result)) {
        videoPath = "file:/C:/Downloads/looser.mp4";     
    } else if ("draw".equals(result)) {
        videoPath = "file:/D:/Downloads/draw.mp4";
    } else {
        videoPath = "file:/D:/Downloads/noonewin.mp4";
    }

    try {
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        StackPane videoRoot = new StackPane(mediaView);
        videoRoot.setStyle("-fx-background-color: black;"); 

        Scene videoScene = new Scene(videoRoot, 800, 600); // Adjust size as needed
        Stage currentStage = (Stage) playerOneName.getScene().getWindow();
        currentStage.setScene(videoScene);
        currentStage.setTitle("Game Result");

        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("Video ended.");
            currentStage.setScene(scene); 
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Error playing video: " + mediaPlayer.getError().getMessage());
        });
    } catch (Exception e) {
        System.out.println("Error loading video: " + e.getMessage());
    }
}


    @FXML
    private void restartButtonHandler(ActionEvent event) {
    }
}
