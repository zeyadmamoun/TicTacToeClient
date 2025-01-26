package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VSComputerScreenController implements Initializable {

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
    private Recording recording;
    private boolean isRecording = false;
    private String gameId;
    private String currentPlayerName;
    private String playerTwoText;
    @FXML
    private Button backButton;
    @FXML
    private Button restartButton;
    @FXML
    private Button recordButton;
    @FXML
    private Pane gamePane;

    private Line winnerLine;

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
        enableBoard();
        removeWinnerLine();
        recordButton.setDisable(false);
        initializeGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttons = new Button[][]{
            {buttonOne, buttonTwo, buttonThree},
            {buttonFour, buttonFive, buttonSix},
            {buttonSeven, buttonEight, buttonNine}
        };
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

        recording.recordMove(row, col, player, currentPlayerName, gameId);
        System.out.println("record method1");
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

    public boolean checkWinner() {
        // Grid pane is at layout X: 258, Y: 174 with width 483 and height 386
        double cellWidth = 483.0 / 3.0;
        double cellHeight = 386.0 / 3.0;
        double gridX = 258;
        double gridY = 174;

        for (int i = 0; i < 3; i++) {
            // Horizontal lines
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                double y = gridY + (i + 0.5) * cellHeight;
                drawWinnerLine(gridX + 50, y, gridX + 483 - 50, y);
                return true;
            }

            // Vertical lines
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                double x = gridX + (i + 0.5) * cellWidth;
                drawWinnerLine(x, gridY + 50, x, gridY + 386 - 50);
                return true;
            }
        }

        // Diagonal from top-left to bottom-right
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            drawWinnerLine(gridX + 50, gridY + 50, gridX + 483 - 50, gridY + 386 - 50);
            return true;
        }

        // Diagonal from top-right to bottom-left
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            drawWinnerLine(gridX + 483 - 50, gridY + 50, gridX - 150 + 50, gridY + 386 - 50);
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
        int score = evaluate();
        if (score == 10) {
            return score - depth;
        }
        if (score == -10) {
            return score + depth;
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

    private int evaluate() {
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == 'O') {
                    return 10;
                } else if (board[row][0] == 'X') {
                    return -10;
                }
            }
        }
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == 'O') {
                    return 10;
                } else if (board[0][col] == 'X') {
                    return -10;
                }
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 'O') {
                return 10;
            } else if (board[0][0] == 'X') {
                return -10;
            }
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == 'O') {
                return 10;
            } else if (board[0][2] == 'X') {
                return -10;
            }
        }
        return 0;
    }

    private void drawWinnerLine(double x1, double y1, double x2, double y2) {
        System.out.printf("Drawing line from (%.2f, %.2f) to (%.2f, %.2f)%n", x1, y1, x2, y2);
        winnerLine = new Line(x1, y1, x2, y2);

        // Apply a solid color as stroke
        winnerLine.setStroke(Color.rgb(28, 147, 159)); // Middle color
        winnerLine.setStrokeWidth(4);

        // Add a glowing shadow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(28, 147, 159)); // Glow matches the line color
        glow.setRadius(20);
        glow.setSpread(0.7);

        winnerLine.setEffect(glow);

        // Add the line to the pane
        gamePane.getChildren().add(winnerLine);
    }

    private void removeWinnerLine() {
        gamePane.getChildren().removeIf(node -> node instanceof Line); // Remove all lines from the pane
        winnerLine = null; // Clear the reference
        System.out.println("All winner lines removed.");
    }

    public void disableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setDisable(true);
            }
        }
    }

    public void enableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setDisable(false);
            }
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
}