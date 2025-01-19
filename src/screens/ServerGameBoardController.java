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
import network.Client;

/**
 * FXML Controller class
 *
 * @author mahmo
 */
public class ServerGameBoardController implements Initializable, Client.ServerGameHandler{

    /**
     * Initializes the controller class.
     */
    Client client;

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
  
    @FXML
    private Button restartButton;

    @FXML
    private void buttonOneHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(0, 0);
        client.sendMoveToServer(0, 0, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonTwoHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(0, 1);
        client.sendMoveToServer(0, 1, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonThreeHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(0, 2);
        client.sendMoveToServer(0, 2, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonFourHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(1, 0);
        client.sendMoveToServer(1, 0, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonFiveHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(1, 1);
        client.sendMoveToServer(1, 1, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonSixHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(1, 2);
        client.sendMoveToServer(1, 2, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonSevenHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(2, 0);
        client.sendMoveToServer(2, 0, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonEightHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
        handleMove(2, 1);
        client.sendMoveToServer(2, 1, String.valueOf(currentPlayer));
    }

    @FXML
    private void buttonNineHandler(ActionEvent event) {
        if(playerTurn == false){ return; }
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
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setDisable(true);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        client = Client.getInstance();
        client.setServerGameHandler(this);
        buttons = new Button[][]{{buttonOne, buttonTwo, buttonThree}, {buttonFour, buttonFive, buttonSix}, {buttonSeven, buttonEight, buttonNine}};
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
        this.playerOneName.setText("Me");
        this.playerOneScore.setText(Integer.toString(playerOneScore));
        this.playerTwoName.setText(playerTwoName);
        this.playerTwoScore.setText(Integer.toString(playerTwoScore));
        System.out.println("playeres info "+client.getUserName());
    }
    @Override
    public void winnerAction() {
        System.out.println("you won");
       // playerOneName.setText("you won");
    }

    @Override
    public void loseAction() {
        System.out.println("you lose");
        //playerOneName.setText("you lost ");
    }

    @FXML
    private void restartButtonHandler(ActionEvent event) {
    }
} 