package screens;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private String currentPlayerName;

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
        recording = new Recording();
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
        if (isRecording) {
            if (currentPlayerName == "Player") {
                currentPlayerName = "PC";
            } else {
                currentPlayerName = "Player";
            }
            recording.recordMove(row, col, currentPlayer, currentPlayerName, gameId);
        }
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setDisable(true);

        if (checkWinner()) {
            gameOver = true;
            if (currentPlayer == 'X') {
                displayAlert("You");
            } else {
                displayAlert("PC");
            }
            disableAllButtons();
        } else if (isBoardFull()) {
            displayAlert("draw");
            gameOver = true;
        }
    }

    public boolean checkWinner() {
        // Grid pane is at layoutX: 209.0, layoutY: 122.0 with width 597.0 and height 483.0
        double cellWidth = 597.0 / 3.0; // Width of each cell
        double cellHeight = 483.0 / 3.0; // Height of each cell
        double gridX = 209.0;
        double gridY = 122.0;

        for (int i = 0; i < 3; i++) {
            // Horizontal lines
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                double y = gridY + (i + 0.5) * cellHeight;
                drawWinnerLine(gridX + 50, y, gridX + 597 - 50, y);
                return true;
            }

            // Vertical lines
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                double x = gridX + (i + 0.5) * cellWidth;
                drawWinnerLine(x, gridY + 50, x, gridY + 483 - 50);
                return true;
            }
        }

        // Diagonal from top-left to bottom-right
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            drawWinnerLine(gridX + 50, gridY + 50, gridX + 597 - 50, gridY + 483 - 50);
            return true;
        }

        // Diagonal from top-right to bottom-left
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            drawWinnerLine(gridX + 597 - 50, gridY + 50, gridX + 50, gridY + 483 - 50);
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

    public void displayAlert(String winner) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.initOwner(recordButton.getScene().getWindow());
        a.setAlertType(Alert.AlertType.INFORMATION);
        ImageView icon = new ImageView(new Image("file:D:/downloads/strategic-plan.png"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        a.setGraphic(icon);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
        if ("draw".equals(winner)) {
            a.setHeaderText("Draw");
        } else {
            a.setHeaderText(winner + " won");
        }
        a.show();

    }

}
