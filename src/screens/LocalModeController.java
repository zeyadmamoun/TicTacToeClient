/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

/**
 * FXML Controller class
 *
 * @author User
 */
public class LocalModeController implements Initializable {

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
    @FXML
    private Pane gamePane;
    private Recording recording;
    private Line winnerLine;
    private String gameId;
    char[][] board = new char[3][3];
    Button[][] buttons = new Button[3][3];

    char currentPlayer = 'X';
    int row, col;
    private String currentPlayerName="player1";
    @FXML
    private Button recordButton;

    private boolean isRecording = false;
    private static int counter = 0;

    @FXML
    private Button restartButton;
    @FXML
    private Text playerOneName;
    @FXML
    private Text currentSymbol;
    @FXML
    private Button backButton;
    @FXML
    private Text playerTwoName;
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
        return "game" + System.currentTimeMillis();
    }

    @FXML
    private void restartButtonHandler(ActionEvent event) {
        enableBoard();
        removeWinnerLine();
        recordButton.setDisable(false);
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
        // Updated dimensions and positions to match the FXML
        double cellWidth = 582.0 / 3.0;
        double cellHeight = 478.0 / 3.0;
        double gridX = 214;
        double gridY = 116;

        for (int i = 0; i < 3; i++) {
            // Horizontal lines
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                double y = gridY + (i + 0.5) * cellHeight;
                drawWinnerLine(gridX + 50, y, gridX + 582 - 50, y);
                return true;
            }

            // Vertical lines
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                double x = gridX + (i + 0.5) * cellWidth;
                drawWinnerLine(x, gridY + 50, x, gridY + 478 - 50);
                return true;
            }
        }

        // Diagonal from top-left to bottom-right
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            drawWinnerLine(gridX + 50, gridY + 50, gridX + 582 - 50, gridY + 478 - 50);
            return true;
        }

        // Diagonal from top-right to bottom-left
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            drawWinnerLine(gridX + 582 - 50, gridY + 50, gridX + 50, gridY + 478 - 50);
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
        counterCurrentTurn++;//by mohamed
        recordButton.setDisable(true);
        if (isRecording) {
                    if(currentPlayerName=="player1"){
                        currentPlayerName="player2";
                    }else{
                        currentPlayerName="player1";
                    }
                    recording.recordMove(row, col, currentPlayer, currentPlayerName, gameId);
        }
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayer;
            draw(row, col);
            System.out.println(Arrays.toString(buttons));

            if (checkWinner() && currentPlayer == 'X') {
                displayAlert("player 1");
                System.out.println("Player 1 won");
                disableBoard();
                //initializeGame();
                // clearBoard();
            } else if (checkWinner() && currentPlayer == 'O') {
                displayAlert("player 2");
                System.out.println("Player 2 won");
                disableBoard();
                //initializeGame();
                // clearBoard();
            }else if (isBoardFull()) {
                displayAlert("draw");
            }
            ChangePlayer();
        }
        changeCurrentplayer();
    }

    public final boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    public void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
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
        if("draw".equals(winner)){
            a.setHeaderText("Draw");
        }else{
            a.setHeaderText(winner +" won");
        }
        a.show();

    }

    public void draw(int row, int col) {
        buttons[row][col].setText(Character.toString(currentPlayer));
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

    @FXML
    private void goBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ModesFXML.fxml"));  // Adjust the path as needed
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