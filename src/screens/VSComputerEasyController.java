/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class VSComputerEasyController implements Initializable {

    @FXML
    private Text playerOneName;
    @FXML
    private Text gameStatus;
    @FXML
    private Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine;
    @FXML
    private Button restartButton;
    private char[][] board = new char[3][3];
    private Button[][] buttons;
    private char currentPlayer = 'X';
    private boolean gameOver = false;

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
        initializeGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttons = new Button[][]{{buttonOne, buttonTwo, buttonThree}, {buttonFour, buttonFive, buttonSix}, {buttonSeven, buttonEight, buttonNine}};
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
        gameStatus.setText("Game In Progress");
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
            System.out.println("Game over");
            return;
        }
        int[] bestMove = findBestMove();
        makeMove(bestMove[0], bestMove[1], 'O');
        currentPlayer = 'X';
    }

    private int[] findBestMove() {
        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int moveValue = minimax(0, false);
                    board[i][j] = ' ';
                    if (moveValue > bestValue) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWinner()) {
            return isMaximizing ? -10 : 10;
        }
        if (isBoardFull()) {
            return 0;
        }
        if (isMaximizing) {
            int bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        bestValue = Math.max(bestValue, minimax(depth + 1, false));
                        board[i][j] = ' ';
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        bestValue = Math.min(bestValue, minimax(depth + 1, true));
                        board[i][j] = ' ';
                    }
                }
            }
            return bestValue;
        }
    }
}

