package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

public class VSComputerIntermediateController implements Initializable {

    @FXML
    private Text gameStatus;
    @FXML
    private Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine;

    private char[][] board = new char[3][3];
    private Button[][] buttons;
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private Random random = new Random();

    @FXML
    private Text player1Name;
    @FXML
    private Text player1Score;
    @FXML
    private Text currentSymbol;
    @FXML
    private Text player2Name;
    @FXML
    private Text player2Score;
    private Button recordButton;
    private Recording recording;
    private boolean isRecording = false;
    private String gameId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttons = new Button[][]{
            {buttonOne, buttonTwo, buttonThree},
            {buttonFour, buttonFive, buttonSix},
            {buttonSeven, buttonEight, buttonNine}
        };
        initializeGame();
    }

    private void initializeGame() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText("");
                buttons[i][j].setDisable(false);
            }
        }
        currentPlayer = 'X';
        gameOver = false;
        gameStatus.setText("Game In Progress");
    }

    @FXML
    private void buttonClickHandler(ActionEvent event) {
        if (gameOver) return;

        Button clickedButton = (Button) event.getSource();
        int row = -1, col = -1;

        // Find the button's position
        outerLoop:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    break outerLoop;
                }
            }
        }

        if (row != -1 && col != -1 && board[row][col] == ' ') {
            makeMove(row, col, currentPlayer);
            if (!checkWinner() && !isBoardFull()) {
                currentPlayer = 'O';
                computerMove();
            }
        }
    }

    private void makeMove(int row, int col, char player) {
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setDisable(true);

        if (checkWinner()) {
            gameOver = true;
            gameStatus.setText(player + " Wins!");
            disableAllButtons();
        } else if (isBoardFull()) {
            gameOver = true;
            gameStatus.setText("It's a Draw!");
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
        }
        return (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
               (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    private void computerMove() {
        if (gameOver) return;

        // Try to make a winning or blocking move
        int[] move = findBestMove();
        if (move != null) {
            makeMove(move[0], move[1], 'O');
            currentPlayer = 'X';
        }
    }

    private int[] findBestMove() {
        // Check for winning moves or blocking moves
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    if (checkWinner()) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = 'X';
                    if (checkWinner()) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }

        // Random move if no strategic move found
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') emptyCells.add(new int[]{i, j});
            }
        }
        if (!emptyCells.isEmpty()) {
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }
        return null;
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }
    @FXML
    private void resetButtonHandler(ActionEvent event) {
        initializeGame();
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
    
    
    
}
