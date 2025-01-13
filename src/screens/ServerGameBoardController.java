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

/**
 * FXML Controller class
 *
 * @author mahmo
 */
public class ServerGameBoardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine;

    private char currentPlayer = 'X';

    private char[][] board = new char[3][3];
    private Button[][] buttons;

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

    private void handleMove(int row, int col) {
        if (board[row][col] == ' ' && currentPlayer == 'X') {
            makeMove(row, col, 'X');
        } else if (board[row][col] == ' ' && currentPlayer == 'O') {
            makeMove(row, col, 'O');
        }
    }

    private void makeMove(int row, int col, char player) {
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        swtichPlayer();

    }

    private void swtichPlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }

    private void initializeGame() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X';

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        buttons = new Button[][]{{buttonOne, buttonTwo, buttonThree}, {buttonFour, buttonFive, buttonSix}, {buttonSeven, buttonEight, buttonNine}};

        initializeGame();
    }

}
