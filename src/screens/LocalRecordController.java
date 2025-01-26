/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import static screens.Recording.findGameById;
import static screens.Recording.readFromFile;

/**
 * FXML Controller class
 *
 * @author zeyad_maamoun
 */
public class LocalRecordController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public Button buttonOne;
    @FXML
    public Button buttonTwo;
    @FXML
    public Button buttonThree;
    @FXML
    public Button buttonFour;
    @FXML
    public Button buttonFive;
    @FXML
    public Button buttonSix;
    @FXML
    public Button buttonSeven;
    @FXML
    public Button buttonEight;
    @FXML
    public Button buttonNine;
    static Thread th;
    @FXML
    private ListView<String> recordsList;

    private final Map<String, String> gameMap = new HashMap<>(); // Maps display names to game IDs
    @FXML
    private Button back_btn;

    @FXML
    private void buttonOneHandler(ActionEvent event) {
    }

    @FXML
    private void buttonTwoHandler(ActionEvent event) {
    }

    @FXML
    private void buttonThreeHandler(ActionEvent event) {
    }

    @FXML
    private void buttonFourHandler(ActionEvent event) {
    }

    @FXML
    private void buttonFiveHandler(ActionEvent event) {
    }

    @FXML
    private void buttonSixHandler(ActionEvent event) {
    }

    @FXML
    private void buttonSevenHandler(ActionEvent event) {
    }

    @FXML
    private void buttonEightHandler(ActionEvent event) {
    }

    @FXML
    private void buttonNineHandler(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadGameRecords();

            if (recordsList != null) {
                recordsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        String gameId = gameMap.get(newVal); // Retrieve the game ID using the display name
                        if (gameId != null && th != null && th.isAlive()) {
                            th.stop();
                        }
                        if (gameId != null) {
                            replayMoves(gameId, this);
                        }

                        Platform.runLater(() -> {
                            recordsList.getSelectionModel().clearSelection();
                        });

                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error initializing records: " + e.getMessage());
        }
    }

    public static void replayMoves(String gameId, LocalRecordController controller) {
        th = new Thread(() -> {
            try {
                JSONArray gameList = readFromFile();
                JSONObject gameRecord = findGameById(gameList, gameId);

                if (gameRecord != null) {
                    JSONArray moves = gameRecord.getJSONArray("moves");

                    // Clear the board first
                    Platform.runLater(() -> {
                        controller.buttonOne.setText("");
                        controller.buttonTwo.setText("");
                        controller.buttonThree.setText("");
                        controller.buttonFour.setText("");
                        controller.buttonFive.setText("");
                        controller.buttonSix.setText("");
                        controller.buttonSeven.setText("");
                        controller.buttonEight.setText("");
                        controller.buttonNine.setText("");
                    });

                    // Play each move with delay
                    for (int i = 0; i < moves.length(); i++) {
                        JSONObject move = moves.getJSONObject(i);
                        final int row = move.getInt("row");
                        final int column = move.getInt("column");
                        final String character = move.getString("character");

                        Thread.sleep(1000); // 2 second delay

                        Platform.runLater(() -> {
                            // Convert row/column to button number (0-based to 1-based)
                            int buttonNum = (row * 3) + column;
                            switch (buttonNum) {
                                case 0:
                                    controller.buttonOne.setText(character);
                                    break;
                                case 1:
                                    controller.buttonTwo.setText(character);
                                    break;
                                case 2:
                                    controller.buttonThree.setText(character);
                                    break;
                                case 3:
                                    controller.buttonFour.setText(character);
                                    break;
                                case 4:
                                    controller.buttonFive.setText(character);
                                    break;
                                case 5:
                                    controller.buttonSix.setText(character);
                                    break;
                                case 6:
                                    controller.buttonSeven.setText(character);
                                    break;
                                case 7:
                                    controller.buttonEight.setText(character);
                                    break;
                                case 8:
                                    controller.buttonNine.setText(character);
                                    break;
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Replay interrupted: " + e.getMessage());
            }
        });
        th.start();
    }

    private void loadGameRecords() {
        try {
            if (recordsList != null) {
                JSONArray games = Recording.readFromFile();
                for (int i = 0; i < games.length(); i++) {
                    JSONObject game = games.getJSONObject(i);
                    String gameId = game.getString("gameId");
                    String displayName = "Game " + (i + 1);
                    gameMap.put(displayName, gameId); // Map the display name to the game ID
                    recordsList.getItems().add(displayName); // Add only the display name to the ListView
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading game records: " + e.getMessage());
        }
    }

    @FXML
    private void backtoPrevouisScreen() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ModesFXML.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) back_btn.getScene().getWindow();

            // Set the new scene to the current stage
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("MainModes"); // Optional: Change the stage title if needed

        } catch (IOException ex) {
            Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
