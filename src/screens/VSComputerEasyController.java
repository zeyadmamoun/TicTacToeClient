/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VSComputerEasyController implements Initializable {

    @FXML
    private Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine;
    private char[][] board = new char[3][3];
    private Button[][] buttons;
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    @FXML
    private Text player1Name;
    @FXML
    private Text currentSymbol;
    @FXML
    private Text player2Name;

    @FXML
    private Button recordButton;
    private Recording recording;
    private boolean isRecording = false;
    private String gameId;
    private String currentPlayerName;
    private String playerTwoText;
    private Random random = new Random();
    @FXML
    private Button backButton;
    @FXML
    private Button restartButton;

    @FXML
    private void recordButtonHandler(ActionEvent event) {
        recordButton.setDisable(true);
        isRecording = !isRecording;
        gameId = generateNewGameId();

        System.out.println("New game started with game ID: " + gameId);
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
        handleMove(0, 0);
    }

    @FXML
    private void buttonTwoHandler(ActionEvent event) {
        handleMove(0, 1);
    }

    @FXML
    private void buttonThreeHandler(ActionEvent event) {
        handleMove(0, 2);
    }

    @FXML
    private void buttonFourHandler(ActionEvent event) {
        handleMove(1, 0);
    }

    @FXML
    private void buttonFiveHandler(ActionEvent event) {
        handleMove(1, 1);
    }

    @FXML
    private void buttonSixHandler(ActionEvent event) {
        handleMove(1, 2);
    }

    @FXML
    private void buttonSevenHandler(ActionEvent event) {
        handleMove(2, 0);
    }

    @FXML
    private void buttonEightHandler(ActionEvent event) {
        handleMove(2, 1);
    }

    @FXML
    private void buttonNineHandler(ActionEvent event) {
        handleMove(2, 2);
    }

    @FXML
    private void restartButtonHandler(ActionEvent event) {
        recordButton.setDisable(false);

        initializeGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttons = new Button[][]{{buttonOne, buttonTwo, buttonThree}, {buttonFour, buttonFive, buttonSix}, {buttonSeven, buttonEight, buttonNine}};
        recording = new Recording();
        gameId = generateNewGameId();
        initializeGame();
    }

    private void initializeGame() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText("");
            }
        }
        currentPlayer = 'X';
        gameOver = false;
    }

    private void handleMove(int row, int col) {
        if (gameOver) {
            System.out.println("Game over");
            return;
        }
        if (board[row][col] == ' ' && currentPlayer == 'X') {
            makeMove(row, col, 'X');
            if (!checkWinner() && !isBoardFull()) {
                currentPlayer = 'O';
                computerMove();
            }
        }
    }

    private void makeMove(int row, int col, char player) {
        recordButton.setDisable(true);

        if (isRecording) {
            recording.recordMove(row, col, player, currentPlayerName, gameId);
            System.out.println("record method1");
        }
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        if (checkWinner()) {
            gameOver = true;
            System.out.println(player + " Wins!");
        } else if (isBoardFull()) {
            gameOver = true;
            System.out.println("It's a Draw!");
        }
    }

    private boolean checkWinner() {
        // Check rows, columns and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                return true;
            }
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void computerMove() {
        if (gameOver) {
            return;
        }

        // 70% chance to make the best move, 30% chance to make a random move
        if (random.nextDouble() < 0.7) {
            // Try to win
            int[] winningMove = findWinningMove('O');
            if (winningMove != null) {
                makeMove(winningMove[0], winningMove[1], 'O');
            } // Block player's winning move
            else {
                int[] blockingMove = findWinningMove('X');
                if (blockingMove != null) {
                    makeMove(blockingMove[0], blockingMove[1], 'O');
                } // Make a strategic move
                else {
                    makeStrategicMove();
                }
            }
        } else {
            makeRandomMove();
        }

        currentPlayer = 'X';
    }

    private int[] findWinningMove(char player) {
        // Check each empty cell for a winning move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    if (checkWinner()) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return null;
    }

    private void makeStrategicMove() {
        // Try to take center if available
        if (board[1][1] == ' ') {
            makeMove(1, 1, 'O');
            return;
        }

        // Try to take corners
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == ' ') {
                makeMove(corner[0], corner[1], 'O');
                return;
            }
        }

        // Take any available side
        int[][] sides = {{0, 1}, {1, 0}, {1, 2}, {2, 1}};
        for (int[] side : sides) {
            if (board[side[0]][side[1]] == ' ') {
                makeMove(side[0], side[1], 'O');
                return;
            }
        }

        // If no strategic move is available, make a random move
        makeRandomMove();
    }

    private void makeRandomMove() {
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int[] move = emptyCells.get(random.nextInt(emptyCells.size()));
            makeMove(move[0], move[1], 'O');
        }
    }
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void goBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LevelsScreen.fxml"));
        root = loader.load();

        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
