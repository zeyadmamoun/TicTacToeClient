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
    private Pane gamePane;

    private Line winnerLine;
    @FXML
    private Button restartButton;
    @FXML
    private Button backButton;
    private int counterCurrentTurn;//by mohamed

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
        enableBoard();
        removeWinnerLine();
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
        counterCurrentTurn = 0;
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
        counterCurrentTurn++;//by mohamed
        recordButton.setDisable(true);

        if (isRecording) {
            recording.recordMove(row, col, player, currentPlayerName, gameId);
            System.out.println("record method1");
        }
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        if (checkWinner()) {
            gameOver = true;
            if (player == 'X') {
                displayAlert("You");
            } else {
                displayAlert("PC");
            }
        } else if (isBoardFull()) {
            gameOver = true;
            displayAlert("draw");
        }
        changeCurrentplayer();
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
    private void goBack(javafx.event.ActionEvent event) throws IOException {
        System.out.println("back button");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/LevelsScreen.fxml"));
        root = loader.load();

        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void changeCurrentplayer() { //by mohamed
        if (counterCurrentTurn % 2 != 0) {
            currentSymbol.setText("O");
        } else if (counterCurrentTurn % 2 == 0) {
            currentSymbol.setText("X");
        }
    }

}
