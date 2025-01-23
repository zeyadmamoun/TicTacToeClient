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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VSComputerIntermediateController implements Initializable {

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
    @FXML
    private Button recordButton;
    private Recording recording;
    private boolean isRecording = false;
    private String gameId;
    @FXML
    private Pane gamePane;

    private Line winnerLine;

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
    }

    @FXML
    private void buttonClickHandler(ActionEvent event) {
        if (gameOver) {
            return;
        }

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
        recordButton.setDisable(true);

        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setDisable(true);

        if (checkWinner()) {
            gameOver = true;
            disableAllButtons();
        } else if (isBoardFull()) {
            gameOver = true;
        }
    }

    public boolean checkWinner() {
        double cellWidth = gamePane.getWidth() / 3.0;
        double cellHeight = gamePane.getHeight() / 3.0;
        double padding = 20;
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                double y = (i + 0.5) * cellHeight; // Center of the row
                drawWinnerLine(padding, y, gamePane.getWidth() - padding, y);
                return true;
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                double x = (i + 0.5) * cellWidth; // Center of the column
                drawWinnerLine(x, padding, x, gamePane.getHeight() - padding);
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            drawWinnerLine(padding, padding, gamePane.getWidth() - padding, gamePane.getHeight() - padding);
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            drawWinnerLine(gamePane.getWidth() - padding, padding, padding, gamePane.getHeight() - padding);
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
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }
        return null;
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

    @FXML
    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }

    @FXML
    private void resetButtonHandler(ActionEvent event) {
        enableBoard();
        removeWinnerLine();
        recordButton.setDisable(false);

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
