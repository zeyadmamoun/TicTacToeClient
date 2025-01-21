/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author User
 */
public class LocalModeController implements Initializable {

    @FXML
    private Text playerOneName;
    @FXML
    private Button buttonOne;
    @FXML
    private Button buttonTwo;
    @FXML
    private Button buttonThree;
    @FXML
    private Button buttonFour;
    @FXML
    private Button buttonFive;
    @FXML
    private Button buttonSix;
    @FXML
    private Button buttonSeven;
    @FXML
    private Button buttonEight;
    @FXML
    private Button buttonNine;

    private Recording recording;

    private String gameId;
    char[][] board = new char[3][3];
    Button[][] buttons = new Button[3][3];

    char currentPlayer = 'X';
    int row, col;
    @FXML
    private Text playerTwoName;
    @FXML
    private Button restartButton;
    @FXML
    private Button recordButton;

    private boolean isRecording = false;
    private static int counter = 0;

    @FXML
    private void recordButtonHandler(ActionEvent event) {
        isRecording = !isRecording;
        gameId = generateNewGameId();

        System.out.println("New game started with game ID: " + gameId);
        recordButton.setText(isRecording ? "Stop Recording" : "Start Recording");
        if (isRecording) {
            System.out.println("Recording started.");
        } else {
            System.out.println("Recording stopped.");
        }
    }

    @FXML
    private void buttonOneHandler(ActionEvent event) {
        makeMove(0, 0);
    }

    @FXML
    private void buttonTwoHandler(ActionEvent event) {
        makeMove(0, 1);
    }

    @FXML
    private void buttonThreeHandler(ActionEvent event) {
        makeMove(0, 2);
    }

    @FXML
    private void buttonFourHandler(ActionEvent event) {
        makeMove(1, 0);
    }

    @FXML
    private void buttonFiveHandler(ActionEvent event) {
        makeMove(1, 1);
    }

    @FXML
    private void buttonSixHandler(ActionEvent event) {
        makeMove(1, 2);
    }

    @FXML
    private void buttonSevenHandler(ActionEvent event) {
        makeMove(2, 0);
    }

    @FXML
    private void buttonEightHandler(ActionEvent event) {
        makeMove(2, 1);
    }

    @FXML
    private void buttonNineHandler(ActionEvent event) {
        makeMove(2, 2);
    }

    private String generateNewGameId() {
        return "game" + counter++;
    }

    @FXML
    private void restartButtonHandler(ActionEvent event) {
        clearBoard();
        initializeGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttons = new Button[][]{{buttonOne, buttonTwo, buttonThree},
        {buttonFour, buttonFive, buttonSix},
        {buttonSeven, buttonEight, buttonNine}};

        recording = new Recording();
        gameId = generateNewGameId();
        initializeGame();
    }

    public void initializeGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }

    public void ChangePlayer() {
        switch (currentPlayer) {
            case 'X':
                currentPlayer = 'O';
                break;
            case 'O':
                currentPlayer = 'X';
                break;
        }
    }

    public void makeMove(int row, int col) {
        if (isRecording) {
            recording.recordMove(row, col, currentPlayer, playerOneName.getText(), gameId);
        }
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayer;
            draw(row, col);
            System.out.println(Arrays.toString(buttons));

            if (checkWinner() && currentPlayer == 'X') {
                System.out.println("Player 1 won");
                initializeGame();
                clearBoard();
            } else if (checkWinner() && currentPlayer == 'O') {
                System.out.println("Player 2 won");
                initializeGame();
                clearBoard();
            }
            ChangePlayer();
        }
    }

    public void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    public void draw(int row, int col) {
        buttons[row][col].setText(Character.toString(currentPlayer));
    }

}
